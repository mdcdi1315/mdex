package com.github.mdcdi1315.mdex.aggressivespawners;

import com.github.mdcdi1315.DotNetLayer.System.ArgumentNullException;
import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.MaybeNull;

import com.github.mdcdi1315.basemodslib.BaseModsLib;
import com.github.mdcdi1315.basemodslib.utils.Extensions;

import com.github.mdcdi1315.mdex.MDEXModInstance;

import net.minecraft.core.Holder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.*;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.List;
import java.util.EnumMap;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.BiFunction;

public final class MDEXAggressiveSpawnerImpl
    implements CustomSpawner
{
    private static final int IMPLICIT_MOB_CAP_COUNT = 80;

    private int tick_time;
    private final List<AggressiveSpawner> spawners;
    private final EnumMap<MobCategory, Integer> mob_cap_values;

    public MDEXAggressiveSpawnerImpl(List<AggressiveSpawner> spawners)
    {
        ArgumentNullException.ThrowIfNull(spawners, "spawners");
        this.spawners = spawners;
        mob_cap_values = new EnumMap<>(MobCategory.class);
    }

    @Override
    public int tick(ServerLevel server_level, boolean spawn_hostiles, boolean spawn_friendlies)
    {
        tick_time++;
        if (tick_time > AggressivenessLevel.BOSS.Ticks) {
            mob_cap_values.clear();
            tick_time = 0;
            return 0;
        }

        if (tick_time > 0 && spawn_hostiles)
        {
            for (AggressivenessLevel l : AggressivenessLevel.values())
            {
                if (tick_time % l.Ticks == 0) {
                    if (!SpawnForDifficulty(server_level, l)) {
                        // If somehow the mob cap of any category hits the value of the IMPLICIT_MOB_CAP_COUNT field, specify a cooldown of 2340 ticks and then retry.
                        mob_cap_values.clear();
                        tick_time = -2340;
                    }
                    break;
                }
            }
        }

        // Seems that the return value is not used at all...
        return 0;
    }

    private static BlockPos GetRandomPositionWithin(RandomSource random, BlockPos position)
    {
        int x = position.getX() + random.nextInt(9) - random.nextInt(9),
            z = position.getZ() + random.nextInt(9) - random.nextInt(9);
        return new BlockPos(x, position.getY() + random.nextInt(5) - random.nextInt(5), z);
    }

    private static Mob GetMobForSpawn(ServerLevel level, EntityType<?> entityType)
    {
        try {
            Entity entity = entityType.create(level);
            if (entity instanceof Mob) { return (Mob)entity; }

            MDEXModInstance.LOGGER.warn("Can't spawn entity of type: {}", BuiltInRegistries.ENTITY_TYPE.getKey(entityType));
        } catch (Exception exception) {
            MDEXModInstance.LOGGER.warn("Failed to create mob", exception);
        }

        return null;
    }

    // Spawns mobs of the specified difficulty specified in the spawner data
    // Returns: A value whether ALL the entries for ALL the players were processed.
    // It returns false if a mob category hit the implicit mob cap count.
    private boolean SpawnForDifficulty(ServerLevel level, AggressivenessLevel desired_aggressiveness_level)
    {
        for (ServerPlayer p : level.getPlayers(BaseModsLib.IsDevelopmentEnvironment() ? new DevPredicate() : new SurvivalModePredicate()))
        {
            if (level.structureManager().hasAnyStructureAt(p.blockPosition())) { continue; }
            for (AggressiveSpawner g : spawners)
            {
                boolean spawn_disallowed = true;
                for (Holder<Biome> h : g.GetApplicableBiomes())
                {
                    if (h == level.getBiome(p.blockPosition())) {
                        spawn_disallowed = false;
                        break;
                    }
                }
                if (spawn_disallowed) { break; }
                for (AggressiveSpawnerEntryList lst : g.Spawning_Entries)
                {
                    if (lst.Difficulty == desired_aggressiveness_level)
                    {
                        int spawned = SpawnCode(level, p, lst);
                        if (mob_cap_values.compute(lst.Category, new MobCapValueCompute(spawned)) > IMPLICIT_MOB_CAP_COUNT) { return false; }
                    }
                }
            }
        }
        return true;
    }

    private int SpawnCode(ServerLevel level, ServerPlayer p, AggressiveSpawnerEntryList lst)
    {
        Optional<AggressiveSpawnerEntry> e;
        int spawned = 0;
        boolean friendly = false;
        BlockPos position;
        int tries = Extensions.Ceiling(level.random.nextFloat() * 4.0F);
        for (int I = 0; I < tries; I++)
        {
            e = lst.Entries.GetRandom(level.random);
            if (e.isEmpty()) { continue; }
            EntityType<?> t = e.get().Entity.Entity;
            if (friendly = t.getCategory().isFriendly()) {
                MDEXModInstance.LOGGER.warn("MDEXAggressiveSpawnerImpl: Friendly mobs are not allowed to be spawned with the aggressive spawner! Entity: {}", BuiltInRegistries.ENTITY_TYPE.getKey(t));
                continue;
            }
            position = GetRandomPositionWithin(level.random, p.blockPosition());
            spawned += SpawnDirect(level, t, position, lst.Category, e.get().GetRandomNumberOfMobsToSpawn(level.random));
        }
        if (!friendly && spawned == 0) {
            e = lst.Entries.GetRandom(level.random);
            if (e.isPresent()) {
                position = GetRandomPositionWithin(level.random, BlockPos.containing(p.pick(20f, 0f, false).getLocation()));
                spawned += SpawnDirect(level, e.get().Entity.Entity, position, lst.Category, e.get().GetRandomNumberOfMobsToSpawn(level.random));
            }
        }
        return spawned;
    }

    private int SpawnDirect(ServerLevel level, EntityType<?> entity_type, BlockPos position, MobCategory category, int times_to_spawn)
    {
        SpawnGroupData sgp = null;
        int mobs_of_type_spawned = 0;
        int rx = position.getX();
        int ry = position.getY();
        int rz = position.getZ();
        for (int I = 0; I < times_to_spawn; I++)
        {
            rx += level.random.nextInt(4) - level.random.nextInt(4);
            ry += level.random.nextInt(4) - level.random.nextInt(3);
            rz += level.random.nextInt(4) - level.random.nextInt(4);
            if (!SpawnPlacements.isSpawnPositionOk(entity_type, level, new BlockPos(rx, ry, rz))) { continue; }
            // We can spawn the current mob.
            Mob m = GetMobForSpawn(level, entity_type);
            if (m == null) { break; }
            m.moveTo(rx + (level.random.nextDouble() * 0.38D), ry + 0.08D, rz + (level.random.nextDouble() * 0.38D), level.random.nextFloat() * 360.0F, 0.0F);
            if (m.checkSpawnObstruction(level))
            {
                sgp = m.finalizeSpawn(level, level.getCurrentDifficultyAt(position), MobSpawnType.NATURAL, sgp);
                level.addFreshEntityWithPassengers(m);
                if (++mobs_of_type_spawned >= m.getMaxSpawnClusterSize()) { break; }
                else if (m.isMaxGroupSizeReached(mobs_of_type_spawned)) { break; }
            }
        }
        return mobs_of_type_spawned;
    }

    private record MobCapValueCompute(int current_value)
        implements BiFunction<MobCategory, Integer, Integer>
    {
        @Override
        public Integer apply(MobCategory cat, @MaybeNull Integer integer)
        {
            if (integer == null) {
                return current_value;
            } else {
                return current_value + integer;
            }
        }
    }

    private record DevPredicate()
        implements Predicate<ServerPlayer>
    {
        @Override
        public boolean test(ServerPlayer player) { return true; }
    }

    private record SurvivalModePredicate()
        implements Predicate<ServerPlayer>
    {
        @Override
        public boolean test(ServerPlayer player) { return player.gameMode.getGameModeForPlayer().isSurvival() || player.isSpectator(); }
    }
}

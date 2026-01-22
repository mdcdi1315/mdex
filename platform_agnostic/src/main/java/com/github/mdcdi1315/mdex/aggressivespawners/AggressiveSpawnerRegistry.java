package com.github.mdcdi1315.mdex.aggressivespawners;

import com.github.mdcdi1315.basemodslib.registries.IRegistryRegistrar;

import com.github.mdcdi1315.mdex.MDEXModInstance;
import com.github.mdcdi1315.mdex.mixin.ServerLevelAccessor;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;

import java.util.Objects;
import java.util.Optional;
import java.util.ArrayList;

public final class AggressiveSpawnerRegistry
{
    private AggressiveSpawnerRegistry() {}

    public static final ResourceKey<Registry<AggressiveSpawner>> REGISTRY_KEY;

    static {
        REGISTRY_KEY = ResourceKey.createRegistryKey(Objects.requireNonNull(ResourceLocation.tryParse("worldgen/mdex_aggressive_spawners")));
    }

    public static void Initialize(IRegistryRegistrar registrar) {
        registrar.RegisterDatapackRegistry(REGISTRY_KEY, AggressiveSpawner.GetCodec());
    }

    public static void OnStarted(MinecraftServer server)
    {
        MDEXModInstance.LOGGER.info("AggressiveSpawnerRegistry: Initializing the Aggressive Spawner subsystem.");
        ServerLevel lvl = server.getLevel(ResourceKey.create(Registries.DIMENSION, MDEXModInstance.MINING_DIM_IDENTIFIER));

        if (lvl == null) {
            MDEXModInstance.LOGGER.warn("AggressiveSpawnerRegistry: The mod cannot access the Mining Dimension level! This subsystem will be inactive.");
            return;
        }

        Registry<Biome> biomes = server.registryAccess().lookupOrThrow(Registries.BIOME);
        Optional<Registry<AggressiveSpawner>> reg = server.registryAccess().lookup(REGISTRY_KEY);

        if (reg.isEmpty()) {
            MDEXModInstance.LOGGER.warn("AggressiveSpawnerRegistry: The mod cannot access the aggressive spawners data pack registry! This subsystem will be inactive.");
        } else {
            MDEXModInstance.LOGGER.info("AggressiveSpawnerRegistry: Compiling {} aggressive spawner definitions...", reg.get().size());
            ArrayList<AggressiveSpawner> spawners = new ArrayList<>(reg.get().size());
            for (AggressiveSpawner ags : reg.get())
            {
                ags.Compile(biomes);
                if (ags.IsCompiled()) { spawners.add(ags); }
            }
            spawners.trimToSize();
            MDEXModInstance.LOGGER.info("AggressiveSpawnerRegistry: Compiled {} aggressive spawner definitions!", spawners.size());

            if (spawners.isEmpty()) {
                MDEXModInstance.LOGGER.info("AggressiveSpawnerRegistry: Disabling the Aggressive Spawner subsystem since we do not have any aggressive spawner entries to apply.");
                return;
            }

            ArrayList<CustomSpawner> patched = new ArrayList<>(((ServerLevelAccessor)lvl).GetCustomSpawners());
            patched.add(new MDEXAggressiveSpawnerImpl(spawners));
            ((ServerLevelAccessor)lvl).SetCustomSpawners(patched);
        }
    }
}

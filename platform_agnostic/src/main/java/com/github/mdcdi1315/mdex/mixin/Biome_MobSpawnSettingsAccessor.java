package com.github.mdcdi1315.mdex.mixin;

import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(MobSpawnSettings.class)
public interface Biome_MobSpawnSettingsAccessor
{
    @Accessor("spawners")
    Map<MobCategory, WeightedRandomList<MobSpawnSettings.SpawnerData>> GetSpawners();

    @Mutable
    @Accessor("spawners")
    void SetSpawners(Map<MobCategory, WeightedRandomList<MobSpawnSettings.SpawnerData>> map);

    @Accessor("mobSpawnCosts")
    Map<EntityType<?>, MobSpawnSettings.MobSpawnCost> GetMobSpawnCosts();

    @Mutable
    @Accessor("mobSpawnCosts")
    void SetMobSpawnCosts(Map<EntityType<?>, MobSpawnSettings.MobSpawnCost> costs);
}

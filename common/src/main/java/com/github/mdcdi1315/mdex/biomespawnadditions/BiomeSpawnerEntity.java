package com.github.mdcdi1315.mdex.biomespawnadditions;

import com.github.mdcdi1315.DotNetLayer.System.ArgumentException;

import com.github.mdcdi1315.mdex.codecs.CodecUtils;
import com.github.mdcdi1315.mdex.util.weight.Weight;
import com.github.mdcdi1315.mdex.util.weight.IWeightedEntry;
import com.github.mdcdi1315.mdex.util.EntityTypeNotFoundException;

import com.mojang.serialization.Codec;

import net.minecraft.util.random.Weighted;
import net.minecraft.world.entity.EntityType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.biome.MobSpawnSettings;

import java.util.Optional;

public final class BiomeSpawnerEntity
    implements IWeightedEntry
{
    public ResourceLocation EntityID;
    public int MinCount , MaxCount;
    public Weight Weight;

    public static Codec<BiomeSpawnerEntity> GetCodec()
    {
        return CodecUtils.CreateCodecDirect(
                ResourceLocation.CODEC.fieldOf("id").forGetter((BiomeSpawnerEntity e) -> e.EntityID),
                // This can be zero but max_count must have at least 1 so that the entry is valid
                CodecUtils.ZERO_OR_POSITIVE_INTEGER.fieldOf("min_count").forGetter((BiomeSpawnerEntity e) -> e.MinCount),
                CodecUtils.POSITIVE_INTEGER.fieldOf("max_count").forGetter((BiomeSpawnerEntity e) -> e.MaxCount),
                com.github.mdcdi1315.mdex.util.weight.Weight.CODEC.fieldOf("weight").forGetter((BiomeSpawnerEntity e) -> e.Weight),
                BiomeSpawnerEntity::new
        );
    }

    public BiomeSpawnerEntity(ResourceLocation l , int mincount , int maxcount , Weight wt)
    {
        EntityID = l;
        MinCount = mincount;
        if ((MaxCount = maxcount) < MinCount) {
            throw new ArgumentException("Maximum pack spawn value must be larger than the minimum pack spawn value.");
        }
        Weight = wt;
    }

    @Override
    public Weight getWeight() {
        return Weight;
    }

    public Weighted<MobSpawnSettings.SpawnerData> getData()
            throws EntityTypeNotFoundException
    {
        Optional<EntityType<?>> o = BuiltInRegistries.ENTITY_TYPE.getOptional(EntityID);
        if (o.isEmpty()) {
            throw new EntityTypeNotFoundException(EntityID);
        }
        return new Weighted<>(new MobSpawnSettings.SpawnerData(o.get() , MinCount , MaxCount) , getWeight().getValue());
    }
}

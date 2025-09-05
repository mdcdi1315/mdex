package com.github.mdcdi1315.mdex.biomespawnadditions;

import com.github.mdcdi1315.mdex.codecs.CodecUtils;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.ListCodec;

import net.minecraft.world.entity.MobCategory;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public final class BiomeSpawnAdditions
{
    public List<String> ModIds;
    public ResourceLocation BiomeID;
    public MobCategory Category;
    public List<BiomeSpawnerEntity> Entries;

    public static Codec<BiomeSpawnAdditions> GetCodec()
    {
        return CodecUtils.CreateCodecDirect(
                new ListCodec<>(Codec.STRING).optionalFieldOf("modids" , List.of()).forGetter((BiomeSpawnAdditions a) -> a.ModIds),
                ResourceLocation.CODEC.fieldOf("biome_id").forGetter((BiomeSpawnAdditions a) -> a.BiomeID),
                MobCategory.CODEC.fieldOf("category").forGetter((BiomeSpawnAdditions a) -> a.Category),
                new ListCodec<>(BiomeSpawnerEntity.GetCodec()).fieldOf("spawners").forGetter((BiomeSpawnAdditions a) -> a.Entries),
                BiomeSpawnAdditions::new
        );
    }

    public BiomeSpawnAdditions(List<String> modids, ResourceLocation biomeid , MobCategory c , List<BiomeSpawnerEntity> entries)
    {
        ModIds = modids;
        Category = c;
        BiomeID = biomeid;
        Entries = entries;
    }

    public void Destroy()
    {
        ModIds = null;
        BiomeID = null;
        Category = null;
        Entries = null;
    }
}

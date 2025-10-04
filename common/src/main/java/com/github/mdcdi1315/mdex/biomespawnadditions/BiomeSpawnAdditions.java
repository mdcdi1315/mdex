package com.github.mdcdi1315.mdex.biomespawnadditions;

import com.github.mdcdi1315.mdex.codecs.CodecUtils;
import com.github.mdcdi1315.DotNetLayer.System.IDisposable;

import com.mojang.datafixers.util.Either;

import com.mojang.serialization.Codec;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public final class BiomeSpawnAdditions
    implements IDisposable
{
    public List<String> ModIds;
    public BiomeEntitySpawnList Entries;
    public Either<TagKey<Biome> , List<ResourceLocation>> Biomes;

    public static Codec<BiomeSpawnAdditions> GetCodec()
    {
        return CodecUtils.CreateCodecDirect(
                Codec.STRING.listOf().optionalFieldOf("modids" , List.of()).forGetter((BiomeSpawnAdditions a) -> a.ModIds),
                Codec.either(TagKey.hashedCodec(Registries.BIOME) , ResourceLocation.CODEC.listOf()).fieldOf("biomes").forGetter((BiomeSpawnAdditions a) -> a.Biomes),
                BiomeEntitySpawnList.GetCodec().fieldOf("spawners").forGetter((BiomeSpawnAdditions a) -> a.Entries),
                BiomeSpawnAdditions::new
        );
    }

    public BiomeSpawnAdditions(List<String> modids, Either<TagKey<Biome> , List<ResourceLocation>> b , BiomeEntitySpawnList entries)
    {
        ModIds = modids;
        Biomes = b;
        Entries = entries;
    }

    public void Dispose()
    {
        ModIds = null;
        Biomes = null;
        if (Entries != null)
        {
            Entries.Dispose();
            Entries = null;
        }
    }
}

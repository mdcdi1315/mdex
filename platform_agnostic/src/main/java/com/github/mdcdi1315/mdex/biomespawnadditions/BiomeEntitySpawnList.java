package com.github.mdcdi1315.mdex.biomespawnadditions;

import com.github.mdcdi1315.basemodslib.codecs.CodecUtils;

import com.github.mdcdi1315.DotNetLayer.System.IDisposable;

import com.mojang.serialization.Codec;

import net.minecraft.world.entity.MobCategory;

import java.util.List;

public record BiomeEntitySpawnList(MobCategory category, List<BiomeEntitySpawnEntry> entries)
    implements IDisposable
{
    public static Codec<BiomeEntitySpawnList> GetCodec()
    {
        return CodecUtils.CreateCodecDirect(
                MobCategory.CODEC.fieldOf("category").forGetter(BiomeEntitySpawnList::category),
                BiomeEntitySpawnEntry.GetCodec().listOf().fieldOf("entities").forGetter(BiomeEntitySpawnList::entries),
                BiomeEntitySpawnList::new
        );
    }

    public boolean IsEmpty()
    {
        // External code other than the mod's one may poorly code the list, check if not null first.
        return entries != null && entries.isEmpty();
    }

    public void Dispose()
    {
        if (IsEmpty()) { return; }
        for (var e : entries) {
            e.Dispose();
        }
    }
}

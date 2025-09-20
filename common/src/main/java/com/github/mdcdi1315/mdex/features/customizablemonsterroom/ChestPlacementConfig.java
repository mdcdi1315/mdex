package com.github.mdcdi1315.mdex.features.customizablemonsterroom;

import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;
import com.github.mdcdi1315.mdex.codecs.CodecUtils;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;

public record ChestPlacementConfig(ResourceLocation LootTable, IntProvider Count)
{
    public static Codec<ChestPlacementConfig> GetCodec()
    {
        return CodecUtils.CreateCodecDirect(
                ResourceLocation.CODEC.fieldOf("loot_table_id").forGetter(ChestPlacementConfig::LootTable),
                IntProvider.codec(0, 32).optionalFieldOf("tries", ConstantInt.of(6)).forGetter(ChestPlacementConfig::Count),
                ChestPlacementConfig::new
        );
    }
}
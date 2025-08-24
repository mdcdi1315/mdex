package com.github.mdcdi1315.mdex.features.customizablemonsterroom;

import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;
import com.github.mdcdi1315.mdex.codecs.CodecUtils;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;

public final class ChestPlacementConfig
{
    public ResourceLocation LootTable;
    public IntProvider Count;

    public static Codec<ChestPlacementConfig> GetCodec()
    {
        return CodecUtils.CreateCodecDirect(
                ResourceLocation.CODEC.fieldOf("loot_table_id").forGetter((ChestPlacementConfig c) -> c.LootTable),
                IntProvider.codec(0 , 32).optionalFieldOf("tries" , ConstantInt.of(6)).forGetter((ChestPlacementConfig c) -> c.Count),
                ChestPlacementConfig::new
        );
    }

    public ChestPlacementConfig(ResourceLocation lt , IntProvider ct)
    {
        LootTable = lt;
        Count = ct;
    }
}
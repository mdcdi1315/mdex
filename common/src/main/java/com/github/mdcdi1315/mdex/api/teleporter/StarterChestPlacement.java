package com.github.mdcdi1315.mdex.api.teleporter;

import com.github.mdcdi1315.mdex.codecs.CodecUtils;
import com.github.mdcdi1315.mdex.block.blockstateproviders.AbstractBlockStateProvider;

import com.mojang.serialization.Codec;

import net.minecraft.resources.ResourceLocation;

public final class StarterChestPlacement
{
    public AbstractBlockStateProvider ContainerState;
    public ResourceLocation LootTable;
    public float Probability;

    public StarterChestPlacement(AbstractBlockStateProvider container , ResourceLocation table , float p)
    {
        ContainerState = container;
        LootTable = table;
        Probability = p;
    }

    public static Codec<StarterChestPlacement> GetCodec()
    {
        return CodecUtils.CreateCodecDirect(
                AbstractBlockStateProvider.CODEC.fieldOf("container_state_provider").forGetter((p) -> p.ContainerState),
                ResourceLocation.CODEC.fieldOf("loot_table").forGetter((p) -> p.LootTable),
                CodecUtils.FLOAT_PROBABILITY.fieldOf("placement_probability").forGetter((p) -> p.Probability),
                StarterChestPlacement::new
        );
    }
}

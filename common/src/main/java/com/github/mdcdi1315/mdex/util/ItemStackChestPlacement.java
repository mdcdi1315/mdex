package com.github.mdcdi1315.mdex.util;

import com.github.mdcdi1315.DotNetLayer.System.ArgumentNullException;

import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.NotNull;
import com.github.mdcdi1315.mdex.codecs.CodecUtils;

import com.mojang.serialization.Codec;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;

public final class ItemStackChestPlacement
{
    private final ItemStack itemstack;
    private final int index;
    private final float probability;

    public static Codec<ItemStackChestPlacement> CreateCodec(Codec<ItemStack> stackcodec)
    {
        ArgumentNullException.ThrowIfNull(stackcodec , "stackcodec");
        return CodecUtils.CreateCodecDirect(
                stackcodec.fieldOf("stack").forGetter((ItemStackChestPlacement cp) -> cp.itemstack),
                CodecUtils.ZERO_OR_POSITIVE_INTEGER.fieldOf("index").forGetter((ItemStackChestPlacement cp) -> cp.index),
                CodecUtils.FLOAT_PROBABILITY.optionalFieldOf("probability" , 1f).forGetter((ItemStackChestPlacement cp) -> cp.probability),
                ItemStackChestPlacement::new
        );
    }

    private ItemStackChestPlacement(ItemStack is , int idx , float p)
    {
        itemstack = is;
        index = idx;
        probability = p;
    }

    @NotNull
    public ItemStack GetStack() {
        return itemstack;
    }

    @NotNull
    public int GetPlacementIndex() {
        return index;
    }

    @NotNull
    public float GetProbability() {
        return probability;
    }

    public boolean ShouldBeSelected(RandomSource rs) {
        return rs.nextFloat() < probability;
    }
}

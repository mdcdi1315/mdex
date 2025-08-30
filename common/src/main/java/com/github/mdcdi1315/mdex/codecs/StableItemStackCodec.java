package com.github.mdcdi1315.mdex.codecs;

import com.mojang.serialization.Codec;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.registries.BuiltInRegistries;

/**
 * Defines a codec for {@link ItemStack}s that it's object data will not be changed between versions of the mod and Minecraft versions. <br />
 * Its instance is retrieved through the {@link StableItemStackCodec#INSTANCE} field.
 */
public final class StableItemStackCodec
{
    private StableItemStackCodec() {}

    public static Codec<ItemStack> INSTANCE =
            CodecUtils.CreateCodecDirect(
                    BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").forGetter(ItemStack::getItem),
                    Codec.INT.fieldOf("count").forGetter(ItemStack::getCount),
                    CompoundTag.CODEC.optionalFieldOf("tag" , new CompoundTag()).forGetter((ItemStack is) -> null),
                    DataComponentPatch.CODEC.optionalFieldOf("components", DataComponentPatch.EMPTY).forGetter(ItemStack::getComponentsPatch),
                    StableItemStackCodec::CreateItemStack
            );

    private static ItemStack CreateItemStack(Item item , int count , CompoundTag ct , DataComponentPatch p)
    {
        return new ItemStack(Holder.direct(item) , count , p);
    }
}
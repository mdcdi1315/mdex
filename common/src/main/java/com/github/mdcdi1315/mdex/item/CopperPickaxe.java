package com.github.mdcdi1315.mdex.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.level.block.state.BlockState;

public final class CopperPickaxe
    extends PickaxeItem
{
    public CopperPickaxe(CopperFamilyTier tier, int attackDamageModifier, float attackSpeedModifier) {
        super(tier, new Item.Properties().attributes(PickaxeItem.createAttributes(tier , attackDamageModifier , attackSpeedModifier)));
    }

    @Override
    public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
        return stack.is(ModItems.COPPER_PICKAXE) && isCorrectToolForDrops(state);
    }

    public boolean isCorrectToolForDrops(BlockState block)
    {
        return ModItems.IsCorrectToolForDropsCopper(block);
    }
}

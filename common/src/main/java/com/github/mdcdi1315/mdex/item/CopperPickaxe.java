package com.github.mdcdi1315.mdex.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.level.block.state.BlockState;

public final class CopperPickaxe
    extends PickaxeItem
{
    public CopperPickaxe(CopperFamilyTier tier, int attackDamageModifier, float attackSpeedModifier) {
        super(tier, attackDamageModifier, attackSpeedModifier, new Item.Properties());
    }

    public boolean isCorrectToolForDrops(BlockState block)
    {
        return ModItems.IsCorrectToolForDropsCopper(block);
    }
}

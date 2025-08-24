package com.github.mdcdi1315.mdex.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.level.block.state.BlockState;

public final class CopperAxe
    extends AxeItem
{
    public CopperAxe(CopperFamilyTier tier, float attackDamageModifier, float attackSpeedModifier) {
        super(tier, attackDamageModifier, attackSpeedModifier, new Item.Properties());
    }

    @Override
    public boolean isCorrectToolForDrops(BlockState block) {
        return ModItems.IsCorrectToolForDropsCopper(block);
    }
}

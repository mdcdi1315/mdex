package com.github.mdcdi1315.mdex.item;

import net.minecraft.world.item.HoeItem;
import net.minecraft.world.level.block.state.BlockState;

public final class CopperHoe
    extends HoeItem
{
    public CopperHoe(CopperFamilyTier tier, int attackDamageModifier, float attackSpeedModifier) {
        super(tier, attackDamageModifier, attackSpeedModifier, new Properties());
    }

    @Override
    public boolean isCorrectToolForDrops(BlockState block) {
        return ModItems.IsCorrectToolForDropsCopper(block);
    }
}

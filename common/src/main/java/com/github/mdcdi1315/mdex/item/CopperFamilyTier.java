package com.github.mdcdi1315.mdex.item;

import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

public class CopperFamilyTier
    implements Tier
{
    @Override
    public int getUses() {
        // A stone tool has 131 uses and the iron tool has 250 - thus say somewhere around, 188 uses are good.
        return 188;
    }

    @Override
    public float getSpeed() {
        // Attack speed. Stone tools are around 4 and iron tools are around 6.
        return 4.57F;
    }

    @Override
    public float getAttackDamageBonus() {
        return 1.4894f;
    }

    @Override
    public int getLevel() {
        return 1;
    }

    @Override
    public int getEnchantmentValue() {
        return 8;
    }

    // Can be repaired with copper ingots
    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.of(Items.COPPER_INGOT);
    }
}

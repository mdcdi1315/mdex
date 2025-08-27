package com.github.mdcdi1315.mdex.item;

import com.github.mdcdi1315.DotNetLayer.System.Func2;
import com.github.mdcdi1315.mdex.MDEXBalmLayer;
import com.github.mdcdi1315.mdex.tag.ModBlockTags;
import net.blay09.mods.balm.api.item.BalmItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.block.state.BlockState;

public final class ModItems
{
    private ModItems() {}

    public static final String CREATIVE_COMBAT_TAB_CATEGORY = "combat";
    public static final String CREATIVE_INGREDIENTS_TAB_CATEGORY = "ingredients";
    public static final String CREATIVE_TOOLS_AND_UTILITIES_CATEGORY = "tools_and_utilities";

    public static CopperFamilyTier COPPER_TIER_DEFAULT;

    public static Item COPPER_PICKAXE;
    public static Item COPPER_AXE;
    public static Item COPPER_SHOVEL;
    public static Item COPPER_HOE;
    public static Item COPPER_SWORD;
    public static Item FUSED_COPPER_AND_IRON_INGOT;

    private static Func2<BlockState , Boolean> correcttoolfordropsfunc;

    public static void Initialize(BalmItems items)
    {
        COPPER_TIER_DEFAULT = new CopperFamilyTier();
        items.registerItem(
                (id) -> COPPER_PICKAXE = new CopperPickaxe(COPPER_TIER_DEFAULT, 1 , -2.873F)
                , MDEXBalmLayer.id("copper_pickaxe"),
                ResourceLocation.tryParse(CREATIVE_TOOLS_AND_UTILITIES_CATEGORY));
        items.registerItem(
                (id) -> COPPER_SWORD = new SwordItem(COPPER_TIER_DEFAULT , new Item.Properties().attributes(SwordItem.createAttributes(COPPER_TIER_DEFAULT , 3, -2.4F)))
                , MDEXBalmLayer.id("copper_sword"),
                ResourceLocation.tryParse(CREATIVE_COMBAT_TAB_CATEGORY));
        items.registerItem(
                (id) -> COPPER_SHOVEL = new ShovelItem(COPPER_TIER_DEFAULT , new Item.Properties().attributes(ShovelItem.createAttributes(COPPER_TIER_DEFAULT , 1.5F, -3F)))
                , MDEXBalmLayer.id("copper_shovel"),
                ResourceLocation.tryParse(CREATIVE_TOOLS_AND_UTILITIES_CATEGORY));
        items.registerItem(
                (id) -> COPPER_AXE = new CopperAxe(COPPER_TIER_DEFAULT, 5.8F , -3.0104F)
                , MDEXBalmLayer.id("copper_axe"),
                ResourceLocation.tryParse(CREATIVE_COMBAT_TAB_CATEGORY));
        items.registerItem(
                (id) -> COPPER_HOE = new CopperHoe(COPPER_TIER_DEFAULT ,  -1 , -1.75F)
                , MDEXBalmLayer.id("copper_hoe"),
                ResourceLocation.tryParse(CREATIVE_TOOLS_AND_UTILITIES_CATEGORY));
        items.registerItem(
                (id) -> FUSED_COPPER_AND_IRON_INGOT = new Item(new Item.Properties().stacksTo(64))
                , MDEXBalmLayer.id("fused_copper_and_iron_ingot"),
                ResourceLocation.tryParse(CREATIVE_INGREDIENTS_TAB_CATEGORY));
        correcttoolfordropsfunc = ModBlockTags.NEEDS_COPPER_TOOL == null ? ModItems::IsCorrectToolForDropsCopper_TagAbsent : ModItems::IsCorrectToolForDropsCopper_TagExist;
    }

    public static boolean IsCorrectToolForDropsCopper(BlockState block)
    {
        return correcttoolfordropsfunc.function(block);
    }

    private static boolean IsCorrectToolForDropsCopper_TagExist(BlockState block)
    {
        return  block.is(BlockTags.NEEDS_STONE_TOOL) ||
                block.is(ModBlockTags.NEEDS_COPPER_TOOL) ||
                (block.is(BlockTags.NEEDS_IRON_TOOL) == false &&
                 block.is(BlockTags.NEEDS_DIAMOND_TOOL) == false);
    }

    private static boolean IsCorrectToolForDropsCopper_TagAbsent(BlockState block)
    {
        return  block.is(BlockTags.NEEDS_STONE_TOOL) ||
                (block.is(BlockTags.NEEDS_IRON_TOOL) == false &&
                 block.is(BlockTags.NEEDS_DIAMOND_TOOL) == false);
    }
}

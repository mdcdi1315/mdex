package com.github.mdcdi1315.mdex.item;

import com.github.mdcdi1315.DotNetLayer.System.Func2;

import com.github.mdcdi1315.basemodslib.item.ItemHelpers;
import com.github.mdcdi1315.basemodslib.item.IItemRegistrar;
import com.github.mdcdi1315.basemodslib.item.ItemRegistrationInformation;

import com.github.mdcdi1315.mdex.tag.ModBlockTags;

import net.minecraft.world.item.*;
import net.minecraft.tags.BlockTags;
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

    public static void Initialize(IItemRegistrar items)
    {
        COPPER_TIER_DEFAULT = new CopperFamilyTier();
        CreativeModeTab tools_and_utils = ItemHelpers.GetMinecraftCreativeModeTabChecked(CREATIVE_TOOLS_AND_UTILITIES_CATEGORY),
            combat = ItemHelpers.GetMinecraftCreativeModeTabChecked(CREATIVE_COMBAT_TAB_CATEGORY);

        items.Register("copper_pickaxe", new ItemRegistrationInformation(
                (id) -> COPPER_PICKAXE = new CopperPickaxe(COPPER_TIER_DEFAULT , 1 , -2.873F),
                tools_and_utils
        ));
        items.Register("copper_sword", new ItemRegistrationInformation(
                (id) -> COPPER_SWORD = new SwordItem(COPPER_TIER_DEFAULT , 3, -2.4F , new Item.Properties()),
                combat
        ));
        items.Register("copper_shovel", new ItemRegistrationInformation(
                (id) -> COPPER_SHOVEL = new ShovelItem(COPPER_TIER_DEFAULT , 1.5F, -3F , new Item.Properties()),
                tools_and_utils
        ));
        items.Register("copper_axe", new ItemRegistrationInformation(
                (id) -> COPPER_AXE = new CopperAxe(COPPER_TIER_DEFAULT , 6.5F , -3.0104F),
                tools_and_utils, combat
        ));
        items.Register("copper_hoe", new ItemRegistrationInformation(
                (id) -> COPPER_HOE = new CopperHoe(COPPER_TIER_DEFAULT , -1 , -1.75F),
                tools_and_utils
        ));
        items.Register("fused_copper_and_iron_ingot", new ItemRegistrationInformation(
                (id) -> FUSED_COPPER_AND_IRON_INGOT = new Item(new Item.Properties().stacksTo(64)),
                ItemHelpers.GetMinecraftCreativeModeTabChecked(CREATIVE_INGREDIENTS_TAB_CATEGORY)
        ));
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

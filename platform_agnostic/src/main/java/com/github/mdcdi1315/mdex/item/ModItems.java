package com.github.mdcdi1315.mdex.item;

import com.github.mdcdi1315.basemodslib.item.ItemHelpers;
import com.github.mdcdi1315.basemodslib.item.IItemRegistrar;
import com.github.mdcdi1315.basemodslib.item.ItemRegistrationInformation;

import com.github.mdcdi1315.mdex.tag.ModItemTags;
import com.github.mdcdi1315.mdex.tag.ModBlockTags;

import net.minecraft.world.item.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;

public final class ModItems
{
    private ModItems() {}

    public static final String CREATIVE_COMBAT_TAB_CATEGORY = "combat";
    public static final String CREATIVE_INGREDIENTS_TAB_CATEGORY = "ingredients";
    public static final String CREATIVE_TOOLS_AND_UTILITIES_CATEGORY = "tools_and_utilities";

    public static Item COPPER_PICKAXE;
    public static Item COPPER_AXE;
    public static Item COPPER_SHOVEL;
    public static Item COPPER_HOE;
    public static Item COPPER_SWORD;
    public static Item FUSED_COPPER_AND_IRON_INGOT;

    public static ToolMaterial MDEX_COPPER_TOOLS;

    public static void Initialize(IItemRegistrar items)
    {
        MDEX_COPPER_TOOLS = new ToolMaterial(ModBlockTags.INCORRECT_BLOCKS_FOR_DROPS_COPPER , 181 , 4.8f , 1.43f , 9 , ModItemTags.COPPER_TOOLS_CAN_BE_REPAIRED_WITH);

        CreativeModeTab tools_and_utils = ItemHelpers.GetMinecraftCreativeModeTabChecked(CREATIVE_TOOLS_AND_UTILITIES_CATEGORY),
            combat = ItemHelpers.GetMinecraftCreativeModeTabChecked(CREATIVE_COMBAT_TAB_CATEGORY);

        items.Register("copper_pickaxe", new ItemRegistrationInformation(
                (id) -> COPPER_PICKAXE = new Item(new Item.Properties().pickaxe(MDEX_COPPER_TOOLS , 1f, -2.8f).setId(ResourceKey.create(Registries.ITEM , id))),
                tools_and_utils
        ));
        items.Register("copper_sword", new ItemRegistrationInformation(
                (id) -> COPPER_SWORD = new Item(new Item.Properties().sword(MDEX_COPPER_TOOLS,3f,-2.4f).setId(ResourceKey.create(Registries.ITEM , id))),
                combat
        ));
        items.Register("copper_shovel", new ItemRegistrationInformation(
                (id) -> COPPER_SHOVEL = new ShovelItem(MDEX_COPPER_TOOLS , 1.5F, -3.0F , new Item.Properties().setId(ResourceKey.create(Registries.ITEM , id))),
                tools_and_utils
        ));
        items.Register("copper_axe", new ItemRegistrationInformation(
                (id) -> COPPER_AXE = new AxeItem(MDEX_COPPER_TOOLS , 6.2F, -3.1F , new Item.Properties().setId(ResourceKey.create(Registries.ITEM , id))),
                tools_and_utils, combat
        ));
        items.Register("copper_hoe", new ItemRegistrationInformation(
                (id) -> COPPER_HOE = new HoeItem(MDEX_COPPER_TOOLS , -1.5f , -1.75F , new Item.Properties().setId(ResourceKey.create(Registries.ITEM , id))),
                tools_and_utils
        ));
        items.Register("fused_copper_and_iron_ingot", new ItemRegistrationInformation(
                (id) -> FUSED_COPPER_AND_IRON_INGOT = new Item(new Item.Properties().stacksTo(64).setId(ResourceKey.create(Registries.ITEM , id))),
                ItemHelpers.GetMinecraftCreativeModeTabChecked(CREATIVE_INGREDIENTS_TAB_CATEGORY)
        ));
    }
}

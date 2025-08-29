package com.github.mdcdi1315.mdex.item;

import com.github.mdcdi1315.mdex.MDEXBalmLayer;
import com.github.mdcdi1315.mdex.tag.ModBlockTags;
import com.github.mdcdi1315.mdex.tag.ModItemTags;
import net.blay09.mods.balm.api.item.BalmItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;

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

    public static void Initialize(BalmItems items)
    {
        MDEX_COPPER_TOOLS = new ToolMaterial(ModBlockTags.INCORRECT_BLOCKS_FOR_DROPS_COPPER , 181 , 4.8f , 1.43f , 9 , ModItemTags.COPPER_TOOLS_CAN_BE_REPAIRED_WITH);
        items.registerItem(
                (id) -> COPPER_PICKAXE = new Item(new Item.Properties().pickaxe(MDEX_COPPER_TOOLS , 1f, -2.8f).setId(ResourceKey.create(Registries.ITEM , id)))
                , MDEXBalmLayer.ItemID("copper_pickaxe"),
                ResourceLocation.tryParse(CREATIVE_TOOLS_AND_UTILITIES_CATEGORY));
        items.registerItem(
                (id) -> COPPER_SWORD = new Item(new Item.Properties().sword(MDEX_COPPER_TOOLS,3f,-2.4f).setId(ResourceKey.create(Registries.ITEM , id)))
                , MDEXBalmLayer.ItemID("copper_sword"),
                ResourceLocation.tryParse(CREATIVE_COMBAT_TAB_CATEGORY));
        items.registerItem(
                (id) -> COPPER_SHOVEL = new ShovelItem(MDEX_COPPER_TOOLS , 1.5F, -3.0F , new Item.Properties().setId(ResourceKey.create(Registries.ITEM , id)))
                , MDEXBalmLayer.ItemID("copper_shovel"),
                ResourceLocation.tryParse(CREATIVE_TOOLS_AND_UTILITIES_CATEGORY));
        items.registerItem(
                (id) -> COPPER_AXE = new AxeItem(MDEX_COPPER_TOOLS , 6.2F, -3.1F , new Item.Properties().setId(ResourceKey.create(Registries.ITEM , id)))
                , MDEXBalmLayer.ItemID("copper_axe"),
                ResourceLocation.tryParse(CREATIVE_COMBAT_TAB_CATEGORY));
        items.registerItem(
                (id) -> COPPER_HOE = new HoeItem(MDEX_COPPER_TOOLS ,  -1.5f , -1.75F , new Item.Properties().setId(ResourceKey.create(Registries.ITEM , id)))
                , MDEXBalmLayer.ItemID("copper_hoe"),
                ResourceLocation.tryParse(CREATIVE_TOOLS_AND_UTILITIES_CATEGORY));
        items.registerItem(
                (id) -> FUSED_COPPER_AND_IRON_INGOT = new Item(new Item.Properties().stacksTo(64).setId(ResourceKey.create(Registries.ITEM , id)))
                , MDEXBalmLayer.ItemID("fused_copper_and_iron_ingot"),
                ResourceLocation.tryParse(CREATIVE_INGREDIENTS_TAB_CATEGORY));
    }
}

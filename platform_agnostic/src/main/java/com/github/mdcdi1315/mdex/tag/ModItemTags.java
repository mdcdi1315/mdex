package com.github.mdcdi1315.mdex.tag;

import com.github.mdcdi1315.mdex.MDEXModInstance;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;

public final class ModItemTags
{
    private ModItemTags() {}

    public static TagKey<Item> COPPER_TOOLS_CAN_BE_REPAIRED_WITH;

    public static void Initialize()
    {
        ResourceLocation loc = ResourceLocation.tryBuild(MDEXModInstance.COMPATIBILITY_NAMESPACE , "copper_tools_can_be_repaired_with");
        if (loc != null)
        {
            COPPER_TOOLS_CAN_BE_REPAIRED_WITH = TagKey.create(
                    Registries.ITEM,
                    loc
            );
        }
    }
}
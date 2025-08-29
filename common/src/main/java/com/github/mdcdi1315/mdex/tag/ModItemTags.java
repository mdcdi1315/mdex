package com.github.mdcdi1315.mdex.tag;

import com.github.mdcdi1315.mdex.MDEXBalmLayer;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public final class ModItemTags
{
    private ModItemTags() {}

    public static TagKey<Item> COPPER_TOOLS_CAN_BE_REPAIRED_WITH;

    public static void Initialize()
    {
        ResourceLocation loc = ResourceLocation.tryBuild(MDEXBalmLayer.COMPATIBILITY_NAMESPACE , "item/copper_tools_can_be_repaired_with");
        if (loc != null)
        {
            COPPER_TOOLS_CAN_BE_REPAIRED_WITH = TagKey.create(
                    Registries.ITEM,
                    loc
            );
        }
    }
}

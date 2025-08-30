package com.github.mdcdi1315.mdex.tag;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import com.github.mdcdi1315.mdex.MDEXBalmLayer;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;

public final class ModBlockTags
{
    private ModBlockTags() {}

    public static TagKey<Block> NEEDS_COPPER_TOOL;

    public static void Initialize()
    {
        ResourceLocation loc = ResourceLocation.tryBuild(MDEXBalmLayer.COMPATIBILITY_NAMESPACE , "needs_copper_tool");
        if (loc != null)
        {
            NEEDS_COPPER_TOOL = TagKey.create(
                    Registries.BLOCK,
                    loc
            );
        }
    }
}

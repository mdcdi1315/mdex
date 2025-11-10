package com.github.mdcdi1315.mdex.tag;

import com.github.mdcdi1315.mdex.MDEXModInstance;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;

public final class ModBlockTags
{
    private ModBlockTags() {}

    public static TagKey<Block> NEEDS_COPPER_TOOL;

    public static TagKey<Block> INCORRECT_BLOCKS_FOR_DROPS_COPPER;

    public static void Initialize()
    {
        ResourceLocation loc = ResourceLocation.tryBuild(MDEXModInstance.COMPATIBILITY_NAMESPACE , "needs_copper_tool");
        if (loc != null)
        {
            NEEDS_COPPER_TOOL = TagKey.create(
                    Registries.BLOCK,
                    loc
            );
        }
        loc = ResourceLocation.tryBuild(MDEXModInstance.COMPATIBILITY_NAMESPACE , "incorrect_blocks_for_drops_copper_tools");
        if (loc != null)
        {
            INCORRECT_BLOCKS_FOR_DROPS_COPPER = TagKey.create(
                    Registries.BLOCK,
                    loc
            );
        }
    }
}
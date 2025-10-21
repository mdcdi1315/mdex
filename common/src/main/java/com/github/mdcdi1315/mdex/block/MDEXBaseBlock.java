package com.github.mdcdi1315.mdex.block;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;

public class MDEXBaseBlock
        extends Block
{
    public MDEXBaseBlock(Properties properties , ResourceLocation location) {
        super(
                properties
                        .overrideDescription(BlockUtils.ConstructExactDescriptionID(location))
                        .setId(ResourceKey.create(Registries.BLOCK , location))
        );
    }
}

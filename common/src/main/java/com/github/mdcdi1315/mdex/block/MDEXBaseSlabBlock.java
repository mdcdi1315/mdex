package com.github.mdcdi1315.mdex.block;

import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.SlabBlock;

public class MDEXBaseSlabBlock
    extends SlabBlock
{
    public MDEXBaseSlabBlock(Properties properties , ResourceLocation location) {
        super(
                properties
                        .overrideDescription(BlockUtils.ConstructExactDescriptionID(location))
                        .setId(ResourceKey.create(Registries.BLOCK , location))
        );
    }
}

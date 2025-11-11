package com.github.mdcdi1315.mdex.block;

import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;

public class MDEXBaseStairBlock
        extends StairBlock
{
    public MDEXBaseStairBlock(BlockState baseState, Properties properties , ResourceLocation location) {
        super(
                baseState,
                properties
                        .overrideDescription(BlockUtils.ConstructExactDescriptionID(location))
                        .setId(ResourceKey.create(Registries.BLOCK , location))
        );
    }
}
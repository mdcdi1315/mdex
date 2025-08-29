package com.github.mdcdi1315.mdex.block;

import com.github.mdcdi1315.mdex.MDEXBalmLayer;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;

public class MDEXBaseStairBlock
    extends StairBlock
{
    public MDEXBaseStairBlock(BlockState baseState, Properties properties , String id) {
        super(baseState, properties.overrideDescription(BlockUtils.ConstructExactDescriptionID(MDEXBalmLayer.MODID , id)).setId(ResourceKey.create(Registries.BLOCK , MDEXBalmLayer.BlockID(id))));
    }
}

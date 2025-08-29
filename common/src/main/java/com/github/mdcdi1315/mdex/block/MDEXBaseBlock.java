package com.github.mdcdi1315.mdex.block;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import com.github.mdcdi1315.mdex.MDEXBalmLayer;

public class MDEXBaseBlock
        extends Block
{
    public MDEXBaseBlock(Properties properties , String id) {
        super(properties.overrideDescription(BlockUtils.ConstructExactDescriptionID(MDEXBalmLayer.MODID , id)).setId(ResourceKey.create(Registries.BLOCK , MDEXBalmLayer.BlockID(id))));
    }
}

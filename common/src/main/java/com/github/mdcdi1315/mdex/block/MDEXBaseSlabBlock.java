package com.github.mdcdi1315.mdex.block;

import com.github.mdcdi1315.mdex.MDEXBalmLayer;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.SlabBlock;

public class MDEXBaseSlabBlock
    extends SlabBlock
{
    public MDEXBaseSlabBlock(Properties properties , String id) {
        super(properties.overrideDescription(BlockUtils.ConstructExactDescriptionID(MDEXBalmLayer.MODID, id)).setId(ResourceKey.create(Registries.BLOCK , MDEXBalmLayer.BlockID(id))));
    }
}

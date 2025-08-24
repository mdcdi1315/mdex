package com.github.mdcdi1315.mdex.block;

import com.github.mdcdi1315.mdex.MDEXBalmLayer;
import net.minecraft.world.level.block.SlabBlock;

public class MDEXBaseSlabBlock
    extends SlabBlock
{
    private final String fulldescid;

    public MDEXBaseSlabBlock(Properties properties , String descid) {
        super(properties);
        fulldescid = BlockUtils.ConstructExactDescriptionID(MDEXBalmLayer.MODID, descid);
    }

    @Override
    public String getDescriptionId() {
        return fulldescid;
    }
}

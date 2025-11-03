package com.github.mdcdi1315.mdex.block;

import com.github.mdcdi1315.mdex.MDEXModInstance;

import net.minecraft.world.level.block.SlabBlock;

public class MDEXBaseSlabBlock
    extends SlabBlock
{
    private final String fulldescid;

    public MDEXBaseSlabBlock(Properties properties , String descid) {
        super(properties);
        fulldescid = BlockUtils.ConstructExactDescriptionID(MDEXModInstance.MOD_ID, descid);
    }

    @Override
    public String getDescriptionId() {
        return fulldescid;
    }
}

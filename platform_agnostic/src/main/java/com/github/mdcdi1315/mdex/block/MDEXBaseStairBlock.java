package com.github.mdcdi1315.mdex.block;

import com.github.mdcdi1315.mdex.MDEXModInstance;

import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;

public class MDEXBaseStairBlock
    extends StairBlock
{
    private final String fulldescid;

    public MDEXBaseStairBlock(BlockState baseState, Properties properties , String descid) {
        super(baseState, properties);
        fulldescid = BlockUtils.ConstructExactDescriptionID(MDEXModInstance.MOD_ID , descid);
    }

    @Override
    public String getDescriptionId() {
        return fulldescid;
    }
}

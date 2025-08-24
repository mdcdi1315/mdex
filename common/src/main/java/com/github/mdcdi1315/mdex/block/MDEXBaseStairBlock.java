package com.github.mdcdi1315.mdex.block;

import com.github.mdcdi1315.mdex.MDEXBalmLayer;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;

public class MDEXBaseStairBlock
    extends StairBlock
{
    private final String fulldescid;

    public MDEXBaseStairBlock(BlockState baseState, Properties properties , String descid) {
        super(baseState, properties);
        fulldescid = BlockUtils.ConstructExactDescriptionID(MDEXBalmLayer.MODID , descid);
    }

    @Override
    public String getDescriptionId() {
        return fulldescid;
    }
}

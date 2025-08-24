package com.github.mdcdi1315.mdex.block;

import net.minecraft.world.level.block.Block;
import com.github.mdcdi1315.mdex.MDEXBalmLayer;

public class MDEXBaseBlock
        extends Block
{
    private final String descid;

    public MDEXBaseBlock(Properties properties , String descid) {
        super(properties);
        this.descid = BlockUtils.ConstructExactDescriptionID(MDEXBalmLayer.MODID , descid);
    }

    @Override
    public final String getDescriptionId() {
        return descid;
    }
}

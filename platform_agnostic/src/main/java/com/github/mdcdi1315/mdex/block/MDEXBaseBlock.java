package com.github.mdcdi1315.mdex.block;

import com.github.mdcdi1315.mdex.MDEXModInstance;

import net.minecraft.world.level.block.Block;

public class MDEXBaseBlock
        extends Block
{
    private final String descid;

    public MDEXBaseBlock(Properties properties , String descid) {
        super(properties);
        this.descid = BlockUtils.ConstructExactDescriptionID(MDEXModInstance.MOD_ID , descid);
    }

    @Override
    public final String getDescriptionId() {
        return descid;
    }
}

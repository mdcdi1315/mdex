package com.github.mdcdi1315.mdex.block.entity;

import com.github.mdcdi1315.mdex.block.ModBlocks;
import net.blay09.mods.balm.common.BalmBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TeleporterTileEntity
    extends BalmBlockEntity
{
    public TeleporterTileEntity(BlockPos pos, BlockState state) {
        super(ModBlocks.TELEPORTER_TILE_ENTITY.get(), pos, state);
    }
}

package com.github.mdcdi1315.mdex.block.entity;

import com.github.mdcdi1315.mdex.block.ModBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;

public class TeleporterTileEntity
    extends BlockEntity
{
    public TeleporterTileEntity(BlockPos pos, BlockState state) {
        super(ModBlocks.TELEPORTER_TILE_ENTITY, pos, state);
    }
}

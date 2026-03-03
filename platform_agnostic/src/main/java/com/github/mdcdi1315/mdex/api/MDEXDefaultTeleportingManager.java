package com.github.mdcdi1315.mdex.api;

import com.github.mdcdi1315.DotNetLayer.System.ArgumentNullException;

import com.github.mdcdi1315.mdex.MDEXModInstance;
import com.github.mdcdi1315.mdex.block.ModBlocks;
import com.github.mdcdi1315.mdex.api.teleporter.PlayerLogicalData;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.portal.DimensionTransition;

public final class MDEXDefaultTeleportingManager
    extends TeleportingManagerV2
{
    public MDEXDefaultTeleportingManager(MinecraftServer server)
            throws ArgumentNullException
    {
        super(server, new TeleportingManagerConfiguration(MDEXModInstance.CONFIG));
    }

    @Override
    protected boolean TeleporterExists(BlockState state) { return state != null && state.is(ModBlocks.TELEPORTER); }

    @Override
    protected boolean TeleportImpl(ServerPlayer player, ServerLevel target, PlayerLogicalData data, boolean playteleportsound)
    {
        return player.changeDimension(
                new DimensionTransition(target, data.current_position, Vec3.ZERO, data.y_rotation, data.x_rotation, playteleportsound ? DimensionTransition.PLAY_PORTAL_SOUND : DimensionTransition.DO_NOTHING)
        ) != null;
    }

    @Override
    protected BlockPos AdjustTeleporterPositionForFeature(BlockPos position, ServerLevel target) { return position.above(); }
}

package com.github.mdcdi1315.mdex.api;

import com.github.mdcdi1315.DotNetLayer.System.ArgumentNullException;

import com.github.mdcdi1315.mdex.MDEXModInstance;
import com.github.mdcdi1315.mdex.block.ModBlocks;
import com.github.mdcdi1315.mdex.api.teleporter.PlayerRotationInformation;

import net.minecraft.world.phys.Vec3;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.portal.DimensionTransition;

public final class MDEXDefaultTeleportingManager
    extends TeleportingManager
{
    public MDEXDefaultTeleportingManager(MinecraftServer server)
            throws ArgumentNullException
    {
        super(server, new TeleportingManagerConfiguration(MDEXModInstance.CONFIG));
    }

    @Override
    protected boolean TeleporterIsExisting(BlockState state) {
        if (state == null) { return false; }
        return state.is(ModBlocks.TELEPORTER);
    }

    @Override
    protected boolean TeleportImpl(ServerPlayer player, ServerLevel target, Vec3 placement_position, PlayerRotationInformation rot_info, boolean playteleportsound)
    {
        if (rot_info != null) {
            return player.changeDimension(new DimensionTransition(target , placement_position , Vec3.ZERO , rot_info.GetYRotation() , rot_info.GetXRotation(), playteleportsound ? DimensionTransition.PLAY_PORTAL_SOUND : DimensionTransition.DO_NOTHING)) != null;
        } else {
            return player.changeDimension(new DimensionTransition(target , placement_position , Vec3.ZERO , player.getYRot() , player.getXRot(), playteleportsound ? DimensionTransition.PLAY_PORTAL_SOUND : DimensionTransition.DO_NOTHING)) != null;
        }
    }
}

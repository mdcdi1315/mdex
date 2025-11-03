package com.github.mdcdi1315.mdex.fabric;

import com.github.mdcdi1315.DotNetLayer.System.ArgumentNullException;

import com.github.mdcdi1315.mdex.api.TeleportingManager;
import com.github.mdcdi1315.mdex.api.TeleportingManagerConfiguration;
import com.github.mdcdi1315.mdex.api.teleporter.PlayerRotationInformation;

import com.github.mdcdi1315.mdex.block.ModBlocks;

import net.minecraft.world.phys.Vec3;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.level.block.state.BlockState;

import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;

public final class FabricTeleportingManager
    extends TeleportingManager
{
    /**
     * Creates a new teleporting manager, defining the server it will operate on.
     *
     * @param server The server where this teleporting manager was created on.
     * @param cfg    The object that provides configuration data for the current teleporting manager.
     * @throws ArgumentNullException <em>server</em> was null.
     */
    public FabricTeleportingManager(MinecraftServer server, TeleportingManagerConfiguration cfg)
            throws ArgumentNullException
    {
        super(server, cfg);
    }

    @Override
    protected boolean TeleporterIsExisting(BlockState state) {
        if (state == null) { return false; }
        return state.is(ModBlocks.TELEPORTER);
    }

    @Override
    protected boolean TeleportImpl(ServerPlayer player, ServerLevel target, Vec3 placement_position, PlayerRotationInformation rot_info, boolean play_teleport_sound) {
        if (rot_info == null) {
            return FabricDimensions.teleport(player, target, new PortalInfo(placement_position , Vec3.ZERO , player.getYRot() , player.getXRot())) != null;
        } else {
            return FabricDimensions.teleport(player, target, new PortalInfo(placement_position , Vec3.ZERO , rot_info.GetYRotation(), rot_info.GetXRotation())) != null;
        }
    }
}

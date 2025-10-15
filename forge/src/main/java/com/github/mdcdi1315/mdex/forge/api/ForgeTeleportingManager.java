package com.github.mdcdi1315.mdex.forge.api;

import com.github.mdcdi1315.mdex.block.ModBlocks;
import com.github.mdcdi1315.mdex.api.TeleportingManager;
import com.github.mdcdi1315.mdex.api.teleporter.PlayerRotationInformation;

import net.minecraft.world.phys.Vec3;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.portal.TeleportTransition;

public final class ForgeTeleportingManager
    extends TeleportingManager
{
    public ForgeTeleportingManager(MinecraftServer server)
    {
        super(server);
    }

    @Override
    protected boolean TeleporterIsExisting(BlockState state) {
        if (state == null) { return false; }
        return state.is(ModBlocks.TELEPORTER);
    }

    @Override
    protected boolean TeleportImpl(ServerPlayer player, ServerLevel target, Vec3 placement_position, PlayerRotationInformation rot_info, boolean play_teleport_sound) {
        if (rot_info == null) {
            return player.teleport(new TeleportTransition(target , placement_position , Vec3.ZERO , 0f , 0f, play_teleport_sound ? TeleportTransition.PLAY_PORTAL_SOUND : TeleportTransition.DO_NOTHING)) != null;
        } else {
            return player.teleport(new TeleportTransition(target , placement_position , Vec3.ZERO , rot_info.GetYRotation(), rot_info.GetXRotation(), play_teleport_sound ? TeleportTransition.PLAY_PORTAL_SOUND : TeleportTransition.DO_NOTHING)) != null;
        }
    }
}

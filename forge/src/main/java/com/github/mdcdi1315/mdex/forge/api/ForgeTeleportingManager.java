package com.github.mdcdi1315.mdex.forge.api;

import com.github.mdcdi1315.mdex.block.ModBlocks;
import com.github.mdcdi1315.mdex.api.TeleportingManager;

import net.minecraft.core.BlockPos;
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
    protected boolean TeleportImpl(ServerPlayer player, ServerLevel target, BlockPos teleporterposition, boolean playteleportsound) {
        return player.teleport(new TeleportTransition(target , new Vec3(teleporterposition.getX() , teleporterposition.getY() , teleporterposition.getZ()) , Vec3.ZERO , 0f , 0f, playteleportsound ? TeleportTransition.PLAY_PORTAL_SOUND : TeleportTransition.DO_NOTHING)) != null;
    }
}

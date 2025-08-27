package com.github.mdcdi1315.mdex.neoforge.api;

import com.github.mdcdi1315.mdex.api.TeleportingManager;
import com.github.mdcdi1315.mdex.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;

public final class NeoForgeTeleportingManager
    extends TeleportingManager
{
    public NeoForgeTeleportingManager(MinecraftServer server) {
        super(server);
    }

    @Override
    protected boolean TeleporterIsExisting(BlockState state) {
        if (state == null) { return false; }
        return state.is(ModBlocks.TELEPORTER);
    }

    @Override
    protected boolean TeleportImpl(ServerPlayer player, ServerLevel target, BlockPos teleporterposition, boolean playteleportsound) {
       return player.changeDimension(new DimensionTransition(target , new Vec3(teleporterposition.getX() , teleporterposition.getY() , teleporterposition.getZ()) , Vec3.ZERO , 0f , 0f, playteleportsound ? DimensionTransition.PLAY_PORTAL_SOUND : DimensionTransition.DO_NOTHING)) != null;
    }
}

package com.github.mdcdi1315.mdex.forge.api;

import com.github.mdcdi1315.mdex.api.TeleportingManager;
import com.github.mdcdi1315.mdex.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.ITeleporter;

import java.util.function.Function;

public final class ForgeTeleportingManager
    extends TeleportingManager
{
    public ForgeTeleportingManager(MinecraftServer server)
    {
        super(server);
    }

    private static class TeleporterImpl
        implements ITeleporter
    {
        private BlockPos teleporterpos;
        private boolean playsound;

        public TeleporterImpl(BlockPos p , boolean playsound)
        {
            teleporterpos = p;
            this.playsound = playsound;
        }

        @Override
        public Entity placeEntity(Entity entity, ServerLevel currentWorld, ServerLevel destWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
            Entity e = repositionEntity.apply(false);
            if (!(e instanceof ServerPlayer player)) {
                return e;
            }
            player.giveExperienceLevels(0);
            player.teleportTo(teleporterpos.getX() , teleporterpos.getY() , teleporterpos.getZ());
            return player;
        }

        @Override
        public boolean playTeleportSound(ServerPlayer player, ServerLevel sourceWorld, ServerLevel destWorld) {
            return playsound;
        }
    }

    @Override
    protected boolean TeleporterIsExisting(BlockState state) {
        if (state == null) { return false; }
        return state.is(ModBlocks.TELEPORTER);
    }

    @Override
    protected boolean TeleportImpl(ServerPlayer player, ServerLevel target, BlockPos teleporterposition, boolean playteleportsound) {
        return player.changeDimension(target , new TeleporterImpl(teleporterposition , playteleportsound)) != null;
    }
}
package com.github.mdcdi1315.mdex.forge.api;

import com.github.mdcdi1315.mdex.block.ModBlocks;
import com.github.mdcdi1315.mdex.api.TeleportingManager;
import com.github.mdcdi1315.mdex.api.teleporter.PlayerRotationInformation;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.portal.PortalInfo;
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
        private final Vec3 pos;
        private final boolean playsound;
        private final PlayerRotationInformation rot_info;

        public TeleporterImpl(Vec3 p , PlayerRotationInformation rot , boolean play_sound)
        {
            pos = p;
            rot_info = rot;
            this.playsound = play_sound;
        }

        @Override
        public Entity placeEntity(Entity entity, ServerLevel currentWorld, ServerLevel destWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
            Entity e = repositionEntity.apply(false);
            if (!(e instanceof ServerPlayer player)) {
                return e;
            }
            player.giveExperienceLevels(0);
            player.teleportTo(pos.x() , pos.y() , pos.z());
            return player;
        }

        @Override
        public PortalInfo getPortalInfo(Entity entity, ServerLevel destWorld, Function<ServerLevel, PortalInfo> defaultPortalInfo) {
            if (rot_info != null) {
                return new PortalInfo(pos , Vec3.ZERO , rot_info.GetYRotation() , rot_info.GetXRotation());
            } else {
                return new PortalInfo(pos , Vec3.ZERO , entity.getYRot() , entity.getXRot());
            }
        }

        @Override
        public boolean isVanilla() {
            return false;
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
    protected boolean TeleportImpl(ServerPlayer player, ServerLevel target, Vec3 position, PlayerRotationInformation rot_info, boolean play_teleport_sound) {
        return player.changeDimension(target , new TeleporterImpl(position , rot_info , play_teleport_sound)) != null;
    }
}
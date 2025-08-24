package com.github.mdcdi1315.mdex.forge.api;

import com.github.mdcdi1315.DotNetLayer.System.ArgumentNullException;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraftforge.common.util.ITeleporter;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

/**
 * A container for translating modloader-agnostic and forge ITeleporter instances.
 */
public final class TeleporterForgeTranslator
    implements ITeleporter
{
    private final com.github.mdcdi1315.mdex.api.ITeleporter original;

    public TeleporterForgeTranslator(com.github.mdcdi1315.mdex.api.ITeleporter instance)
    {
        ArgumentNullException.ThrowIfNull(instance , "instance");
        original = instance;
    }

    @Override
    public boolean isVanilla() {
        return original.isVanilla();
    }

    @Override
    public boolean playTeleportSound(ServerPlayer player, ServerLevel sourceWorld, ServerLevel destWorld) {
        return original.playTeleportSound(player, sourceWorld, destWorld);
    }

    @Override
    public @Nullable PortalInfo getPortalInfo(Entity entity, ServerLevel destWorld, Function<ServerLevel, PortalInfo> defaultPortalInfo) {
        return original.getPortalInfo(entity, destWorld, defaultPortalInfo);
    }

    @Override
    public Entity placeEntity(Entity entity, ServerLevel currentWorld, ServerLevel destWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
        return original.placeEntity(entity, currentWorld, destWorld, yaw, repositionEntity);
    }
}

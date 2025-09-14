package com.github.mdcdi1315.mdex.permissions;

import com.github.mdcdi1315.mdex.MDEXBalmLayer;

import com.github.mdcdi1315.DotNetLayer.System.InvalidOperationException;

import net.blay09.mods.balm.api.permission.BalmPermissions;
import net.blay09.mods.balm.api.permission.PermissionContext;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

public final class PermissionsSubsystem
{
    private PermissionsSubsystem() {}

    public static ResourceLocation MODIFY_SPAWN_DATA_PERM;

    public static void Initialize(BalmPermissions perms)
    {
        MODIFY_SPAWN_DATA_PERM = ResourceLocation.tryBuild(MDEXBalmLayer.MODID , "commands.can_modify_spawn_data");
        if (MODIFY_SPAWN_DATA_PERM == null) {
            throw new InvalidOperationException("Cannot configure command permission, because the permission could not be built.");
        }
        perms.registerPermission(MODIFY_SPAWN_DATA_PERM , PermissionsSubsystem::ModifyTeleporterSpawnDataPermissionHandler);
    }

    private static boolean ModifyTeleporterSpawnDataPermissionHandler(PermissionContext cxt)
    {
        Optional<ServerPlayer> spo = cxt.getPlayer();
        if (spo.isEmpty()) {
            return false;
        }
        ServerPlayer sp = spo.get();
        if (sp.server.isSingleplayer()) {
            return sp.isCreative() || sp.isSpectator();
        } else {
            return sp.server.getPlayerList().isOp(sp.getGameProfile());
        }
    }
}

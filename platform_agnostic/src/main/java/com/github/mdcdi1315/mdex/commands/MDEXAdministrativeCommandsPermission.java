package com.github.mdcdi1315.mdex.commands;

import com.github.mdcdi1315.basemodslib.BaseModsLib;
import com.github.mdcdi1315.basemodslib.ModdingEnvironment;
import com.github.mdcdi1315.basemodslib.commands.CommandPermission;

import net.minecraft.commands.CommandSourceStack;

public final class MDEXAdministrativeCommandsPermission
    extends CommandPermission
{
    @Override
    protected boolean IsSatisfied(CommandSourceStack stack) {
        var server = stack.getServer();
        var player = stack.getPlayer();
        if (player == null) {
            return !server.isSingleplayer();
        } else if (BaseModsLib.GetEnvironment() == ModdingEnvironment.CLIENT && server.isSingleplayer()) {
            return true;
        } else {
            return server.getPlayerList().isOp(player.getGameProfile());
        }
    }
}

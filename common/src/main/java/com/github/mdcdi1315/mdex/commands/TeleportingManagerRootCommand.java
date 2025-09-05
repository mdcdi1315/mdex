package com.github.mdcdi1315.mdex.commands;

import com.github.mdcdi1315.mdex.api.commands.RegistersSubCommandsAbstractCommand;

public final class TeleportingManagerRootCommand
    extends RegistersSubCommandsAbstractCommand
{
    public TeleportingManagerRootCommand() {
        super("teleporting_manager" , new TeleportUsingTeleportingManagerCommand());
    }
}

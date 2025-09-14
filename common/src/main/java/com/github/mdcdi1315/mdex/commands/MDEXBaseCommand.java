package com.github.mdcdi1315.mdex.commands;

import com.github.mdcdi1315.mdex.MDEXBalmLayer;
import com.github.mdcdi1315.mdex.api.commands.RegistersSubCommandsAbstractCommand;

public final class MDEXBaseCommand
    extends RegistersSubCommandsAbstractCommand
{
    public MDEXBaseCommand() {
        super(
                MDEXBalmLayer.MODID ,
                new TeleporterSpawnDataRootCommand(),
                new TeleportingManagerRootCommand()
        );
    }
}

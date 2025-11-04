package com.github.mdcdi1315.mdex.commands;

import com.github.mdcdi1315.mdex.MDEXModInstance;
import com.github.mdcdi1315.basemodslib.commands.RegistersSubCommandsAbstractCommand;

public final class MDEXBaseCommand
    extends RegistersSubCommandsAbstractCommand
{
    public MDEXBaseCommand() {
        super(
                MDEXModInstance.MOD_ID ,
                new TeleporterSpawnDataRootCommand(),
                new TeleportingManagerRootCommand()
        );
    }
}

package com.github.mdcdi1315.mdex.commands;

import com.github.mdcdi1315.basemodslib.commands.RegistersSubCommandsAbstractCommand;

public final class TeleporterSpawnDataRootCommand
    extends RegistersSubCommandsAbstractCommand
{
    public TeleporterSpawnDataRootCommand() {
        super(
                "teleporter_spawn_data" ,
                new MDEXAdministrativeCommandsPermission(),
                new RetrieveSpawnDataForAllPlayersCommand() ,
                new RetrieveSpawnDataForPlayerCommand() ,
                new SetNewSpawnDataForPlayerCommand(),
                new ResetStarterChestPlacementCommand(),
                new GetStarterChestPlacementStatusCommand()
        );
    }
}

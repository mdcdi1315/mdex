package com.github.mdcdi1315.mdex.commands;

import com.github.mdcdi1315.mdex.permissions.PermissionsSubsystem;
import com.github.mdcdi1315.mdex.api.commands.RegistersSubCommandsAbstractCommand;

public final class TeleporterSpawnDataRootCommand
    extends RegistersSubCommandsAbstractCommand
{
    public TeleporterSpawnDataRootCommand() {
        super(
                "teleporter_spawn_data" ,
                PermissionsSubsystem.MODIFY_SPAWN_DATA_PERM ,
                new RetrieveSpawnDataForAllPlayersCommand() ,
                new RetrieveSpawnDataForPlayerCommand() ,
                new SetNewSpawnDataForPlayerCommand(),
                new ResetStarterChestPlacementCommand(),
                new GetStarterChestPlacementStatusCommand()
        );
    }
}

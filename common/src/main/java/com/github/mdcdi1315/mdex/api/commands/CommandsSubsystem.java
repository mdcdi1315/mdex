package com.github.mdcdi1315.mdex.api.commands;

import com.github.mdcdi1315.mdex.commands.MDEXBaseCommand;

import net.blay09.mods.balm.api.command.BalmCommands;

public final class CommandsSubsystem
{
    private CommandsSubsystem() {}

    public static void Initialize(BalmCommands registrar)
    {
        registrar.register(new MDEXBaseCommand()::RegisterToDispatcher);
    }
}

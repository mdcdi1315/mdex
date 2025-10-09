package com.github.mdcdi1315.mdex.api.commands;

import com.github.mdcdi1315.mdex.commands.MDEXBaseCommand;

import com.mojang.brigadier.CommandDispatcher;

import net.blay09.mods.balm.api.command.BalmCommands;

import net.minecraft.commands.CommandSourceStack;

public final class CommandsSubsystem
{
    private CommandsSubsystem() {}

    public static void Initialize(BalmCommands registrar)
    {
        registrar.register(CommandsSubsystem::MDEXCommandsInitializer);
    }

    private static void MDEXCommandsInitializer(CommandDispatcher<CommandSourceStack> dispatcher) {
        new MDEXBaseCommand().RegisterToDispatcher(dispatcher);
    }
}

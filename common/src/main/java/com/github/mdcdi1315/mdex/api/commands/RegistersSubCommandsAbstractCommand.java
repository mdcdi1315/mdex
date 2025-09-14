package com.github.mdcdi1315.mdex.api.commands;

import com.github.mdcdi1315.DotNetLayer.System.ArgumentNullException;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import com.github.mdcdi1315.DotNetLayer.System.ArgumentException;

import net.blay09.mods.balm.api.command.BalmCommands;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.resources.ResourceLocation;

public abstract class RegistersSubCommandsAbstractCommand
    extends AbstractCommand
{
    private ResourceLocation[] permissions;
    private AbstractCommand[] commands;

    protected RegistersSubCommandsAbstractCommand(String commandname , AbstractCommand... cmds)
            throws ArgumentException {
        super(commandname);
        commands = cmds;
        permissions = null;
    }

    protected RegistersSubCommandsAbstractCommand(String commandname , ResourceLocation permission , AbstractCommand... cmds)
            throws ArgumentException
    {
        super(commandname);
        ArgumentNullException.ThrowIfNull(permission , "permission");
        commands = cmds;
        this.permissions = new ResourceLocation[] { permission };
    }

    protected RegistersSubCommandsAbstractCommand(String commandname , ResourceLocation[] permissions , AbstractCommand... cmds)
            throws ArgumentException {
        super(commandname);
        ArgumentNullException.ThrowIfNull(permissions , "permissions");
        commands = cmds;
        this.permissions = permissions;
    }

    @Override
    protected final LiteralArgumentBuilder<CommandSourceStack> CommandImplementation(LiteralArgumentBuilder<CommandSourceStack> builder) {
        LiteralArgumentBuilder<CommandSourceStack> b = builder;
        if (permissions != null)
        {
            for (var c : permissions)
            {
                b = b.requires(BalmCommands.requirePermission(c));
            }
            // permissions = null;
        }
        for (var i : commands)
        {
            b = i.RegisterByBuilder(b); // Recursively apply the commands, if needed
        }
        // Found to cause issues, do not clean the array
        // commands = null; // Aggressively clean the command array to sweep up any unused mem
        return b;
    }
}

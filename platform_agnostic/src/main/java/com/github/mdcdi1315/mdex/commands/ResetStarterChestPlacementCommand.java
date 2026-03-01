package com.github.mdcdi1315.mdex.commands;

import com.github.mdcdi1315.DotNetLayer.System.ArgumentException;

import com.github.mdcdi1315.basemodslib.commands.AbstractCommand;
import com.github.mdcdi1315.basemodslib.utils.ChatComponentSupplier;

import com.github.mdcdi1315.mdex.MDEXModInstance;
import com.github.mdcdi1315.mdex.api.teleporter.StarterChestPlacementInfo;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

import net.minecraft.network.chat.Component;
import net.minecraft.commands.CommandSourceStack;

public final class ResetStarterChestPlacementCommand
    extends AbstractCommand
{
    public ResetStarterChestPlacementCommand() { super("reset_starter_chest_placement"); }

    @Override
    protected LiteralArgumentBuilder<CommandSourceStack> CommandImplementation(LiteralArgumentBuilder<CommandSourceStack> builder) {
        return builder.executes(ResetStarterChestPlacementCommand::ResetStarterChestImpl);
    }

    private static int ResetStarterChestImpl(CommandContext<CommandSourceStack> c)
            throws CommandSyntaxException
    {
        var d = MDEXModInstance.MANAGER.GetData();
        try {
            d.SetChestPlacementToValue(StarterChestPlacementInfo.NOT_PLACED);
            c.getSource().sendSuccess(ChatComponentSupplier.FromTranslatable("mdex.commands.msg.starterchestplacement.success"), true);
        } catch (ArgumentException e) {
            throw new CommandSyntaxException(new SimpleCommandExceptionType(Component.literal("Error")) , Component.literal(String.format("Cannot execute the reset command: %s" , e.getMessage())));
        }
        return 0;
    }
}

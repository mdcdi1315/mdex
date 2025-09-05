package com.github.mdcdi1315.mdex.commands;

import com.github.mdcdi1315.mdex.api.MDEXModAPI;
import com.github.mdcdi1315.mdex.api.commands.AbstractCommand;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.DimensionArgument;

public final class TeleportUsingTeleportingManagerCommand
    extends AbstractCommand
{
    public TeleportUsingTeleportingManagerCommand() {
        super("teleport");
    }

    @Override
    protected LiteralArgumentBuilder<CommandSourceStack> CommandImplementation(LiteralArgumentBuilder<CommandSourceStack> builder)
    {
        return builder.then(
                Commands.argument("dimension" , DimensionArgument.dimension())
                        .then(
                                Commands.argument("player" , EntityArgument.player())
                                        .executes(TeleportUsingTeleportingManagerCommand::MoveOtherPlayer)
                        )
                        .executes(TeleportUsingTeleportingManagerCommand::MoveCurrentPlayer)
        );
    }

    private static int MoveOtherPlayer(CommandContext<CommandSourceStack> c)
            throws CommandSyntaxException
    {
        ServerLevel sl = DimensionArgument.getDimension(c , "dimension");
        ServerPlayer sp = EntityArgument.getPlayer(c , "player");
        int v;
        return switch (v = MovePlayer(sp, sl)) {
            case -1 -> {
                c.getSource().sendFailure(Component.translatable("mdex.commands.errormsg.teleportbymanager.same_teleporting_dims"));
                yield -1;
            }
            case -2 -> {
                c.getSource().sendFailure(Component.translatable("mdex.commands.errormsg.teleportbymanager.cannot_teleport_internal_error"));
                yield -2;
            }
            default -> {
                c.getSource().sendSuccess(() -> Component.translatable("mdex.commands.msg.teleportbymanager.success") , true);
                yield v;
            }
        };
    }

    private static int MoveCurrentPlayer(CommandContext<CommandSourceStack> c)
            throws CommandSyntaxException
    {
        ServerLevel sl = DimensionArgument.getDimension(c , "dimension");
        var p = c.getSource().getPlayer();
        if (p == null)
        {
            c.getSource().sendFailure(Component.translatable("mdex.commands.errormsg.teleportbymanager.noplayerselected"));
            return -3;
        }
        int v;
        return switch (v = MovePlayer(p, sl)) {
            case -1 -> {
                c.getSource().sendFailure(Component.translatable("mdex.commands.errormsg.teleportbymanager.same_teleporting_dims"));
                yield -1;
            }
            case -2 -> {
                c.getSource().sendFailure(Component.translatable("mdex.commands.errormsg.teleportbymanager.cannot_teleport_internal_error"));
                yield -2;
            }
            default -> {
                c.getSource().sendSuccess(() -> Component.translatable("mdex.commands.msg.teleportbymanager.success") , true);
                yield v;
            }
        };
    }

    private static int MovePlayer(ServerPlayer sp , ServerLevel targetlevel)
    {
        if (sp.level() == targetlevel) {
            return -1;
        }
        var t = MDEXModAPI.getMethodImplementation().GetTeleportingManager();
        t.SetTargetDimension(targetlevel.dimension().location());
        return t.Teleport(sp , sp.getOnPos()) ? 0 : -2;
    }
}

package com.github.mdcdi1315.mdex.commands;

import com.github.mdcdi1315.basemodslib.utils.ElementSupplier;
import com.github.mdcdi1315.basemodslib.commands.AbstractCommand;

import com.github.mdcdi1315.mdex.MDEXModInstance;
import com.github.mdcdi1315.mdex.api.teleporter.PlayerLogicalData;
import com.github.mdcdi1315.mdex.api.teleporter.TeleporterLogicalEntry;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;

public final class RetrieveSpawnDataForPlayerCommand
    extends AbstractCommand
{
    public RetrieveSpawnDataForPlayerCommand() { super("get_for_player"); }

    @Override
    protected LiteralArgumentBuilder<CommandSourceStack> CommandImplementation(LiteralArgumentBuilder<CommandSourceStack> builder)
    {
        return builder.then(
                Commands.argument("player" , EntityArgument.player())
                        .executes(RetrieveSpawnDataForPlayerCommand::RetrieveCommandContext)
                );
    }

    private static int RetrieveCommandContext(CommandContext<CommandSourceStack> c)
            throws CommandSyntaxException
    {
        ServerPlayer sp = EntityArgument.getPlayer(c , "player");
        var list = MDEXModInstance.MANAGER.GetData();
        PlayerLogicalData lld = list.GetPlayerLogicalData(sp);
        if (lld.used_teleporter_index == -1) {
            c.getSource().sendFailure(Component.translatable("mdex.commands.errormsg.player_no_data" , sp.getName().getString()));
            return -10;
        }
        TeleporterLogicalEntry t_logical_entry = list.GetTeleporterEntry(lld.used_teleporter_index);
        c.getSource().sendSuccess(new ElementSupplier<>(Component.translatable("mdex.commands.msg.getspdatacmdp.success" , sp.getName().getString(), t_logical_entry.teleporter_position.level().location().toString(), lld.current_position.x, lld.current_position.y, lld.current_position.z, lld.x_rotation, lld.y_rotation)), true);
        return 0;
    }
}

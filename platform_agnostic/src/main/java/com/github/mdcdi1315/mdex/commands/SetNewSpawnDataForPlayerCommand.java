package com.github.mdcdi1315.mdex.commands;

import com.github.mdcdi1315.basemodslib.utils.ElementSupplier;
import com.github.mdcdi1315.basemodslib.commands.AbstractCommand;

import com.github.mdcdi1315.mdex.MDEXModInstance;
import com.github.mdcdi1315.mdex.api.teleporter.PlayerLogicalData;
import com.github.mdcdi1315.mdex.api.teleporter.TeleporterLogicalEntry;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.commands.Commands;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceKey;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.DimensionArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;

public final class SetNewSpawnDataForPlayerCommand
    extends AbstractCommand
{
    public SetNewSpawnDataForPlayerCommand() { super("set_for_player"); }

    @Override
    protected LiteralArgumentBuilder<CommandSourceStack> CommandImplementation(LiteralArgumentBuilder<CommandSourceStack> builder) {
        return builder.then(
            Commands.argument("dimension" , DimensionArgument.dimension())
                    .then(
                            Commands.argument("player" , EntityArgument.player())
                                    .then(
                                            Commands.argument("position" , BlockPosArgument.blockPos())
                                                    .executes(SetNewSpawnDataForPlayerCommand::CommandExecutor)
                                    )
                    )
        );
    }

    private static int CommandExecutor(CommandContext<CommandSourceStack> c)
            throws CommandSyntaxException
    {
        ServerPlayer sp = EntityArgument.getPlayer(c , "player");
        BlockPos newpos = BlockPosArgument.getBlockPos(c , "position");
        ServerLevel sl = DimensionArgument.getDimension(c, "dimension");

        var d = MDEXModInstance.MANAGER.GetData();

        PlayerLogicalData pld = d.GetPlayerLogicalData(sp);
        Vec3 old = pld.used_teleporter_index == -1 ? null : pld.current_position;
        pld.current_position = Vec3.upFromBottomCenterOf(newpos, 0.6f);

        int t = d.GetTeleporters();
        TeleporterLogicalEntry tle;
        ResourceKey<Level> dim = sl.dimension();
        for (int I = 0; I < t; I++)
        {
            tle = d.GetTeleporterEntry(I);
            if (tle.teleporter_position.level().equals(dim)) {
                pld.used_teleporter_index = I;
                t = -1;
                break;
            }
        }
        if (t == -1) {
            c.getSource().sendSuccess(new ElementSupplier<>(
                    (old == null) ?
                            Component.translatable("mdex.commands.msg.setspdatacmd.success" , sp.getName().getString() , newpos.getX() , newpos.getY() , newpos.getZ()) :
                            Component.translatable("mdex.commands.msg.setspdatacmd.successandshowoldtoo" , sp.getName().getString() , newpos.getX() , newpos.getY() , newpos.getZ() , old.x() , old.y() , old.z())), true);
            return 0;
        } else {
            c.getSource().sendFailure(
                    Component.translatable("mdex.commands.errormsg.setspdatacmd.no_teleporter_entry_for_dim", dim.location().toString())
            );
            return -10;
        }
    }
}

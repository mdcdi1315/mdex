package com.github.mdcdi1315.mdex.commands;

import com.github.mdcdi1315.mdex.api.TeleportingManager;
import com.github.mdcdi1315.mdex.api.commands.AbstractCommand;
import com.github.mdcdi1315.mdex.api.teleporter.TeleporterSpawnData;
import com.github.mdcdi1315.mdex.api.saveddata.PerDimensionWorldDataManager;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.core.BlockPos;
import net.minecraft.commands.Commands;
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
    public SetNewSpawnDataForPlayerCommand() {
        super("set_for_player");
    }

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
        ServerLevel sl = DimensionArgument.getDimension(c, "dimension");
        BlockPos newpos = BlockPosArgument.getBlockPos(c , "position");
        TeleporterSpawnData tsd = new PerDimensionWorldDataManager(sl).ComputeIfAbsent(TeleportingManager.TELEPORTER_DATA_DIMFILE_NAME , TeleporterSpawnData::new);
        var p = tsd.GetOrUpdateEntry(sp);
        BlockPos old = p.GetTeleporterPosition();
        p.SetTeleporterPosition(newpos);
        Component chatc;
        if (old == null) {
            chatc = Component.translatable("mdex.commands.msg.setspdatacmd.success" , sp.getName().getString() , newpos.getX() , newpos.getY() , newpos.getZ());
        } else {
            chatc = Component.translatable("mdex.commands.msg.setspdatacmd.successandshowoldtoo" , sp.getName().getString() , newpos.getX() , newpos.getY() , newpos.getZ() , old.getX() , old.getY() , old.getZ());
        }
        c.getSource().sendSuccess(() -> chatc , true);
        return 0;
    }
}

package com.github.mdcdi1315.mdex.commands;

import com.github.mdcdi1315.mdex.api.TeleportingManager;
import com.github.mdcdi1315.mdex.api.commands.AbstractCommand;
import com.github.mdcdi1315.mdex.api.saveddata.PerDimensionWorldDataManager;
import com.github.mdcdi1315.mdex.api.teleporter.TeleporterSpawnData;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.core.BlockPos;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.DimensionArgument;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

public final class RetrieveSpawnDataForPlayerCommand
    extends AbstractCommand
{
    public RetrieveSpawnDataForPlayerCommand() {
        super("get_for_player");
    }

    @Override
    protected LiteralArgumentBuilder<CommandSourceStack> CommandImplementation(LiteralArgumentBuilder<CommandSourceStack> builder)
    {
        return builder.then(
                Commands.argument("dimension" , DimensionArgument.dimension())
                        .then(
                                Commands.argument("player" , EntityArgument.player())
                                        .executes(RetrieveSpawnDataForPlayerCommand::RetrieveCommandContext)
                        )
                );
    }

    private static int RetrieveCommandContext(CommandContext<CommandSourceStack> c)
            throws CommandSyntaxException
    {
        ServerPlayer sp = EntityArgument.getPlayer(c , "player");
        ServerLevel sl = DimensionArgument.getDimension(c, "dimension");
        TeleporterSpawnData d = new PerDimensionWorldDataManager(sl).Get(TeleportingManager.TELEPORTER_DATA_DIMFILE_NAME, TeleporterSpawnData::new);
        if (d == null) {
            c.getSource().sendFailure(Component.translatable("mdex.commands.errormsg.no_teleporting_spawn_data" , sl.dimension().location()));
            return -10;
        }
        var p = d.GetLastSpawnInfo(sp);
        BlockPos pos = p.GetTeleporterPosition();
        if (p == null || pos == null) {
            c.getSource().sendFailure(Component.translatable("mdex.commands.errormsg.getspdatacmdp.nodataforplayer"));
            return -12;
        }
        var cmp = Component.translatable("mdex.commands.msg.getspdatacmdp.success" , sp.getName().getString() , sl.dimension().location() , pos.getX() , pos.getY() , pos.getZ());
        c.getSource().sendSuccess(() -> cmp , true);
        return 0;
    }
}

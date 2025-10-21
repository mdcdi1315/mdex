package com.github.mdcdi1315.mdex.commands;

import com.github.mdcdi1315.DotNetLayer.System.ArgumentException;
import com.github.mdcdi1315.mdex.api.TeleportingManager;
import com.github.mdcdi1315.mdex.api.commands.AbstractCommand;
import com.github.mdcdi1315.mdex.api.saveddata.PerDimensionWorldDataManager;
import com.github.mdcdi1315.mdex.api.teleporter.TeleporterSpawnData;
import com.github.mdcdi1315.mdex.api.teleporter.StarterChestPlacementInfo;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.DimensionArgument;

public final class ResetStarterChestPlacementCommand
    extends AbstractCommand
{
    public ResetStarterChestPlacementCommand() {
        super("reset_starter_chest_placement");
    }

    @Override
    protected LiteralArgumentBuilder<CommandSourceStack> CommandImplementation(LiteralArgumentBuilder<CommandSourceStack> builder) {
        return builder.then(
                Commands.argument("dimension", DimensionArgument.dimension())
                        .executes(ResetStarterChestPlacementCommand::ResetStarterChestImpl)
        );
    }

    private static int ResetStarterChestImpl(CommandContext<CommandSourceStack> c)
            throws CommandSyntaxException
    {
        ServerLevel sl = DimensionArgument.getDimension(c , "dimension");
        var ds = new PerDimensionWorldDataManager(sl).Get(TeleportingManager.TELEPORTER_DATA_DIMFILE_NAME , TeleporterSpawnData::new);
        if (ds == null) {
            c.getSource().sendFailure(Component.translatable("mdex.commands.errormsg.no_teleporting_spawn_data" , sl.dimension().location().toString()));
            return -1;
        }
        if (ds.GetPlacementInfo() == StarterChestPlacementInfo.IRRELEVANT) {
            c.getSource().sendFailure(Component.translatable("mdex.commands.errormsg.starterchestplacement.irrelevant_placement" , sl.dimension().location().toString()));
            return -2;
        }
        try {
            ds.SetChestPlacementToValue(StarterChestPlacementInfo.NOT_PLACED);
            c.getSource().sendFailure(Component.translatable("mdex.commands.msg.starterchestplacement.success"));
        } catch (ArgumentException e) {
            throw new CommandSyntaxException(new SimpleCommandExceptionType(Component.literal("Error")) , Component.literal(String.format("Cannot execute the reset command: %s" , e.getMessage())));
        }
        return 0;
    }
}

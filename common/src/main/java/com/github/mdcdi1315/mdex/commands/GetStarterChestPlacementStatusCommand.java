package com.github.mdcdi1315.mdex.commands;

import com.github.mdcdi1315.mdex.api.MDEXModAPI;
import com.github.mdcdi1315.mdex.api.TeleportingManager;
import com.github.mdcdi1315.mdex.api.commands.AbstractCommand;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.DimensionArgument;

public final class GetStarterChestPlacementStatusCommand
    extends AbstractCommand
{
    public GetStarterChestPlacementStatusCommand() {
        super("get_starter_chest_placement_status");
    }

    @Override
    protected LiteralArgumentBuilder<CommandSourceStack> CommandImplementation(LiteralArgumentBuilder<CommandSourceStack> builder) {
        return builder.then(
                Commands.argument("dimension" , DimensionArgument.dimension())
                        .executes(GetStarterChestPlacementStatusCommand::GetStatus)
        );
    }

    private static int GetStatus(CommandContext<CommandSourceStack> c)
            throws CommandSyntaxException
    {
        ServerLevel sl = DimensionArgument.getDimension(c , "dimension");
        var ds = sl.getDataStorage().get(MDEXModAPI.getMethodImplementation().GetTeleportingManager().GetSavedTeleporterDataFactory(), TeleportingManager.TELEPORTER_DATA_DIMFILE_NAME);
        if (ds == null) {
            c.getSource().sendFailure(Component.translatable("mdex.commands.errormsg.no_teleporting_spawn_data" , sl.dimension().location().toString()));
            return -1;
        }
        switch (ds.GetPlacementInfo())
        {
            case PLACED -> {
                Component co = Component.translatable("mdex.commands.msg.getstarterchestplacement.success.placed" , sl.dimension().location().toString());
                c.getSource().sendSuccess(() -> co , true);
            }
            case NOT_PLACED -> {
                Component co = Component.translatable("mdex.commands.msg.getstarterchestplacement.success.not_placed" , sl.dimension().location().toString());
                c.getSource().sendSuccess(() -> co , true);
            }
            case IRRELEVANT -> c.getSource().sendFailure(Component.translatable("mdex.commands.errormsg.getstarterchestplacement.irrelevant"));
        }
        return 0;
    }
}

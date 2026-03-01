package com.github.mdcdi1315.mdex.commands;

import com.github.mdcdi1315.basemodslib.utils.ElementSupplier;
import com.github.mdcdi1315.basemodslib.commands.AbstractCommand;

import com.github.mdcdi1315.mdex.MDEXModInstance;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.commands.CommandSourceStack;

public final class GetStarterChestPlacementStatusCommand
    extends AbstractCommand
{
    public GetStarterChestPlacementStatusCommand() { super("get_starter_chest_placement_status"); }

    @Override
    protected LiteralArgumentBuilder<CommandSourceStack> CommandImplementation(LiteralArgumentBuilder<CommandSourceStack> builder) {
        return builder.executes(GetStarterChestPlacementStatusCommand::GetStatus);
    }

    private static int GetStatus(CommandContext<CommandSourceStack> c)
    {
        ServerLevel sl = MDEXModInstance.MANAGER.GetMiningDimensionLevel();
        switch (MDEXModInstance.MANAGER.GetData().GetPlacementInfo())
        {
            case PLACED -> c.getSource().sendSuccess(new ElementSupplier<>(Component.translatable("mdex.commands.msg.getstarterchestplacement.success.placed" , sl.dimension().location().toString())), true);
            case NOT_PLACED -> c.getSource().sendSuccess(new ElementSupplier<>(Component.translatable("mdex.commands.msg.getstarterchestplacement.success.not_placed" , sl.dimension().location().toString())) , true);
            case IRRELEVANT -> c.getSource().sendFailure(Component.translatable("mdex.commands.errormsg.getstarterchestplacement.irrelevant"));
        }
        return 0;
    }
}

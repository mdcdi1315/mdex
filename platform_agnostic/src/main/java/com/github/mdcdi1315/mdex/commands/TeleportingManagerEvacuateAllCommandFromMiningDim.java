package com.github.mdcdi1315.mdex.commands;

import com.github.mdcdi1315.basemodslib.utils.ElementSupplier;
import com.github.mdcdi1315.basemodslib.commands.AbstractCommand;

import com.github.mdcdi1315.mdex.MDEXModInstance;
import com.github.mdcdi1315.mdex.api.TeleportingManager;
import com.github.mdcdi1315.mdex.api.TeleportRequestState;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.commands.Commands;
import net.minecraft.resources.ResourceKey;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.core.registries.Registries;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.DimensionArgument;

public final class TeleportingManagerEvacuateAllCommandFromMiningDim
    extends AbstractCommand
{
    public TeleportingManagerEvacuateAllCommandFromMiningDim() {
        super("evacuate_all_players_to");
    }

    @Override
    protected LiteralArgumentBuilder<CommandSourceStack> CommandImplementation(LiteralArgumentBuilder<CommandSourceStack> builder) {
        return builder.then(
                Commands.argument("dimension" , DimensionArgument.dimension())
                        .executes(TeleportingManagerEvacuateAllCommandFromMiningDim::EvacuateAllExecutor)
        );
    }

    private static int EvacuateAllExecutor(CommandContext<CommandSourceStack> c)
            throws CommandSyntaxException
    {
        ServerLevel evacuate_to_level = DimensionArgument.getDimension(c , "dimension"),
                mining_dim = c.getSource().getServer().getLevel(ResourceKey.create(Registries.DIMENSION , MDEXModInstance.MINING_DIM_IDENTIFIER));

        if (mining_dim == null) {
            return -1;
        }

        TeleportingManager mgr = MDEXModInstance.MANAGER;

        int evacuated = 0;

        for (ServerPlayer p : mining_dim.getPlayers((ServerPlayer sp) -> true))
        {
            if (mgr.TeleportTo(p , p.getOnPos() , evacuate_to_level) == TeleportRequestState.COMPLETED) {
                evacuated++;
            }
        }

        // c.getSource().sendSuccess(new SuccessMessageSupplier(evacuated , evacuate_to_level.dimension().location().toString()), true);

        c.getSource().sendSuccess(new ElementSupplier<>(Component.translatable("mdex.commands.msg.evacuate_all_mining_dim_players.success", evacuated , evacuate_to_level.dimension().location().toString())), true);

        return evacuated;
    }
}

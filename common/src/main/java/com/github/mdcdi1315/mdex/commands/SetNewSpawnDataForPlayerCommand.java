package com.github.mdcdi1315.mdex.commands;

import com.github.mdcdi1315.mdex.api.TeleportingManager;
import com.github.mdcdi1315.mdex.api.commands.AbstractCommand;
import com.github.mdcdi1315.mdex.api.teleporter.TeleporterSpawnData;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.HolderLookup;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.level.saveddata.SavedData;
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
        TeleporterSpawnData tsd = sl.getDataStorage().computeIfAbsent(new SavedData.Factory<>(
                SetNewSpawnDataForPlayerCommand::CREATE,
                SetNewSpawnDataForPlayerCommand::LDR,
                DataFixTypes.LEVEL
        ) , TeleportingManager.TELEPORTER_DATA_DIMFILE_NAME);
        BlockPos p = tsd.AddEntry(sp , newpos);
        Component chatc;
        if (p == null) {
            chatc = Component.translatable("mdex.commands.msg.setspdatacmd.success" , sp.getName().getString() , newpos.getX() , newpos.getY() , newpos.getZ());
        } else {
            chatc = Component.translatable("mdex.commands.msg.setspdatacmd.successandshowoldtoo" , sp.getName().getString() , newpos.getX() , newpos.getY() , newpos.getZ() , p.getX() , p.getY() , p.getZ());
        }
        c.getSource().sendSuccess(() -> chatc , true);
        return 0;
    }

    private static TeleporterSpawnData LDR(CompoundTag ct , HolderLookup.Provider provider)
    {
        TeleporterSpawnData t = new TeleporterSpawnData();
        t.FromDeserialized(ct);
        return t;
    }

    private static TeleporterSpawnData CREATE()
    {
        TeleporterSpawnData t = new TeleporterSpawnData();
        // Explicitly declaring starter chest placement as irrelevant.
        // Doing this does not cause problems generating a chest when not needed to.
        // This can definitely cause an issue if the selected dimension is the Mining Dimension,
        // and when the spawn data are not created yet (The teleporter has not been used),
        // but when doing such things, you know that already.
        t.SetChestPlacementAsIrrelevant();
        return t;
    }
}

package com.github.mdcdi1315.mdex.commands;

import com.github.mdcdi1315.DotNetLayer.System.Collections.Generic.KeyValuePair;

import com.github.mdcdi1315.mdex.api.TeleportingManager;
import com.github.mdcdi1315.mdex.api.commands.AbstractCommand;
import com.github.mdcdi1315.mdex.api.teleporter.TeleporterSpawnData;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.DimensionArgument;

import java.util.List;
import java.util.UUID;

public final class RetrieveSpawnDataForAllPlayersCommand
    extends AbstractCommand
{
    public RetrieveSpawnDataForAllPlayersCommand() {
        super("get_for_all_players");
    }

    @Override
    protected LiteralArgumentBuilder<CommandSourceStack> CommandImplementation(LiteralArgumentBuilder<CommandSourceStack> builder) {
        return builder.then(
                Commands.argument("dimension" , DimensionArgument.dimension())
                    .executes(RetrieveSpawnDataForAllPlayersCommand::SpawnDataExecutor)
        );
    }

    private static int SpawnDataExecutor(CommandContext<CommandSourceStack> c)
            throws CommandSyntaxException
    {
        ServerLevel sl = DimensionArgument.getDimension(c , "dimension");
        TeleporterSpawnData d = sl.getDataStorage().get(RetrieveSpawnDataForAllPlayersCommand::LDR , TeleportingManager.TELEPORTER_DATA_DIMFILE_NAME);
        if (d == null) {
            c.getSource().sendFailure(Component.translatable("mdex.commands.errormsg.no_teleporting_spawn_data" , sl.dimension().location()));
            return 10;
        }
        Component cp = Component.literal(ListSpawnData(sl , d.GetSpawnPositionsForAllPlayers()));
        c.getSource().sendSuccess(() -> cp , true);
        return 0;
    }

    private static TeleporterSpawnData LDR(CompoundTag ct)
    {
        TeleporterSpawnData t = new TeleporterSpawnData();
        t.FromDeserialized(ct);
        return t;
    }

    private static String ListSpawnData(ServerLevel level , List<KeyValuePair<UUID , BlockPos>> list)
    {
        StringBuilder sb = new StringBuilder(1024);
        sb.append(Component.translatable("mdex.commands.msg.getspdatacmd.list.header" , list.size() , level.dimension().location()).getString());
        sb.append('\n');
        MinecraftServer s = level.getServer();
        int plc = 0;
        for (var i : list)
        {
            BlockPos p = i.getValue();
            if (p == null) { continue; }
            ServerPlayer sp = s.getPlayerList().getPlayer(i.getKey());
            sb.append(
                    Component.translatable("mdex.commands.msg.getspdatacmd.list.entry" ,
                            i.getKey() ,
                            sp == null ?
                                    Component.translatable("mdex.commands.msg.getspdatacmd.list.entry.playername.unidentified").getString() :
                                    sp.getName().getString() ,
                            p.getX(),
                            p.getY(),
                            p.getZ()
                    ).getString()
            );
            sb.append('\n');
            plc++;
        }
        sb.append(Component.translatable("mdex.commands.msg.getspdatacmd.list.footer" , plc).getString());
        return sb.toString();
    }
}

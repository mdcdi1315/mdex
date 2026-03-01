package com.github.mdcdi1315.mdex.commands;

import com.github.mdcdi1315.basemodslib.commands.AbstractCommand;
import com.github.mdcdi1315.basemodslib.utils.ChatComponentSupplier;

import com.github.mdcdi1315.mdex.MDEXModInstance;
import com.github.mdcdi1315.mdex.api.teleporter.PlayerLogicalData;
import com.github.mdcdi1315.mdex.api.teleporter.TeleporterManagerData;
import com.github.mdcdi1315.mdex.api.teleporter.TeleporterLogicalEntry;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.commands.CommandSourceStack;

public final class RetrieveSpawnDataForAllPlayersCommand
    extends AbstractCommand
{
    public RetrieveSpawnDataForAllPlayersCommand() { super("get_for_all_players"); }

    @Override
    protected LiteralArgumentBuilder<CommandSourceStack> CommandImplementation(LiteralArgumentBuilder<CommandSourceStack> builder) {
        return builder.executes(RetrieveSpawnDataForAllPlayersCommand::SpawnDataExecutor);
    }

    private static int SpawnDataExecutor(CommandContext<CommandSourceStack> c)
    {
        c.getSource().sendSuccess(ChatComponentSupplier.FromLiteral(ListSpawnData(c.getSource().getServer() , MDEXModInstance.MANAGER.GetData())), true);
        return 0;
    }

    private static String ListSpawnData(MinecraftServer serv, TeleporterManagerData list)
    {
        StringBuilder sb = new StringBuilder(1024);
        sb.append(Component.translatable("mdex.commands.msg.getspdatacmd.list.header" , serv.getPlayerList().getPlayerCount()).getString());
        sb.append('\n');
        int plc = 0;
        PlayerLogicalData dt;
        for (var e : list.GetPlayerLogicalDataEntries())
        {
            dt = e.getValue();
            if (dt.used_teleporter_index == -1) { continue; }
            TeleporterLogicalEntry t_logical_entry = list.GetTeleporterEntry(dt.used_teleporter_index);
            ServerPlayer sp = serv.getPlayerList().getPlayer(e.getKey());
            sb.append(
                    Component.translatable("mdex.commands.msg.getspdatacmd.list.entry",
                        e.getKey().toString(),
                        (sp == null ? Component.translatable("mdex.commands.msg.getspdatacmd.list.entry.playername.unidentified") : sp.getName()).getString(),
                        dt.current_position.x,
                        dt.current_position.y,
                        dt.current_position.z,
                        dt.x_rotation,
                        dt.y_rotation,
                        t_logical_entry.teleporter_position.level().location().toString()
                    ).getString()
            );
            sb.append('\n');
            plc++;
        }
        sb.append(Component.translatable("mdex.commands.msg.getspdatacmd.list.footer" , plc).getString());
        return sb.toString();
    }
}

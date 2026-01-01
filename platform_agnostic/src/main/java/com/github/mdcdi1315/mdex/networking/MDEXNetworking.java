package com.github.mdcdi1315.mdex.networking;

import com.github.mdcdi1315.basemodslib.network.NetworkManager;
import com.github.mdcdi1315.basemodslib.network.INetworkBuilder;
import com.github.mdcdi1315.basemodslib.network.ServerSideNetworkPacketRegistrationInfo;

import com.github.mdcdi1315.mdex.MDEXModInstance;
import com.github.mdcdi1315.mdex.api.TeleportRequestState;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public final class MDEXNetworking
{
    private MDEXNetworking() {}

    private static NetworkManager NETWORKING;

    public static void Initialize(NetworkManager manager)
    {
        NETWORKING = manager;
        INetworkBuilder builder = manager.GetBuilder();
        builder.DefineNetworkVersion("1.0");
        builder.RegisterServerBoundPacket(new ServerSideNetworkPacketRegistrationInfo<>(
                TeleportingRequestPacket.class,
                TeleportingRequestPacket.TYPE,
                TeleportingRequestPacket.GetNetworkCodec(),
                MDEXNetworking::OnTeleportingPacketDispatched
        ));
    }

    public static TeleportRequestState HandleTeleportRequest(Player p , BlockPos teleport_pos)
    {
        if (p instanceof ServerPlayer) {
            TeleportRequestState state = MDEXModInstance.MANAGER.Teleport(p , teleport_pos);
            if (state == TeleportRequestState.SCHEDULED) {
                p.displayClientMessage(Component.translatable("mdex.teleportmanager.msg.teleport_scheduled") , true);
            }
            return state;
        } else {
            NETWORKING.SendToServer(new TeleportingRequestPacket(teleport_pos));
            return TeleportRequestState.COMPLETED;
        }
    }

    private static void OnTeleportingPacketDispatched(ServerPlayer player, TeleportingRequestPacket packet)
    {
        TeleportRequestState state = MDEXModInstance.MANAGER.Teleport(player , packet.GetTeleporterPosition());
        if (state == TeleportRequestState.SCHEDULED) {
            player.displayClientMessage(Component.translatable("mdex.teleportmanager.msg.teleport_scheduled") , true);
        }
    }
}

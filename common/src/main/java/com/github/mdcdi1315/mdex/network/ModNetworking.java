package com.github.mdcdi1315.mdex.network;

import com.github.mdcdi1315.DotNetLayer.System.Action2;
import com.github.mdcdi1315.DotNetLayer.System.ArgumentNullException;
import com.github.mdcdi1315.mdex.network.packet.AbstractPacket;
import com.github.mdcdi1315.mdex.network.serializer.PacketSerializer;
import com.github.mdcdi1315.mdex.util.MDEXException;
import net.blay09.mods.balm.api.network.BalmNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public final class ModNetworking
{
    private ModNetworking() {}

    public static void Initialize(BalmNetworking networking)
    {
        //RegisterServerBoundPacket(networking , PlayerTeleportationPacket.class , new PlayerTeleportationPacketSerializer() , null);
    }

    public static <TD extends AbstractPacket<TD>> CustomPacketPayload.Type<TD> GetPayloadType(Class<TD> cls)
            throws MDEXException
    {
        try {
            return (CustomPacketPayload.Type<TD>) cls.getField("PACKET_TYPE").get(null);
        } catch (NoSuchFieldException nsfe) {
            throw new MDEXException("Cannot find the payload type field: PACKET_TYPE.");
        } catch (IllegalAccessException iae) {
            throw new MDEXException("Cannot access the payload type field: PACKET_TYPE.");
        } catch (ClassCastException cce) {
            throw new MDEXException("PACKET_TYPE is not Type<TSelf>.");
        }
    }

    public static <TD extends AbstractPacket<TD> , TS extends PacketSerializer<TD>> void RegisterServerBoundPacket(BalmNetworking networking , Class<TD> packetclass , TS serializer , Action2<ServerPlayer , TD> action)
    {
        ArgumentNullException.ThrowIfNull(packetclass , "packetclass");
        ArgumentNullException.ThrowIfNull(serializer , "serializer");
        ArgumentNullException.ThrowIfNull(action , "action");
        networking.registerServerboundPacket(GetPayloadType(packetclass) , packetclass , serializer.GetStreamCodec() , action::action);
    }

    public static <TD extends AbstractPacket<TD> , TS extends PacketSerializer<TD>> void RegisterClientBoundPacket(BalmNetworking networking , Class<TD> packetclass , TS serializer , Action2<Player, TD> action)
    {
        ArgumentNullException.ThrowIfNull(packetclass , "packetclass");
        ArgumentNullException.ThrowIfNull(serializer , "serializer");
        ArgumentNullException.ThrowIfNull(action , "action");
        networking.registerClientboundPacket(GetPayloadType(packetclass) , packetclass , serializer.GetStreamCodec() , action::action);
    }

}

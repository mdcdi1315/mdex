package com.github.mdcdi1315.mdex.network.serializer;

import com.github.mdcdi1315.mdex.network.packet.PlayerTeleportationPacket;
import com.mojang.serialization.Codec;

public final class PlayerTeleportationPacketSerializer
    extends JsonPacketSerializer<PlayerTeleportationPacket>
{
    @Override
    protected Codec<PlayerTeleportationPacket> GetSerializationCodec() {
        return PlayerTeleportationPacket.GetSerializationCodec();
    }
}

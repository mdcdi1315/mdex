package com.github.mdcdi1315.mdex.network.serializer;

import com.github.mdcdi1315.mdex.network.packet.AbstractPacket;
import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;

public abstract class JsonPacketSerializer<TD extends AbstractPacket<TD>>
    extends PacketSerializer<TD>
{
    private Codec<TD> codec;

    protected abstract Codec<TD> GetSerializationCodec();

    public JsonPacketSerializer() {
        codec = GetSerializationCodec();
    }

    @Override
    protected final TD Read(RegistryFriendlyByteBuf bytebuffer) {
        return bytebuffer.readJsonWithCodec(codec);
    }

    @Override
    protected final void Write(TD object, RegistryFriendlyByteBuf bytebuffer) {
        bytebuffer.writeJsonWithCodec(codec , object);
    }
}

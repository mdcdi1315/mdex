package com.github.mdcdi1315.mdex.network.serializer;

import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.DisallowNull;
import com.github.mdcdi1315.mdex.network.packet.AbstractPacket;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public abstract class PacketSerializer<T extends AbstractPacket<T>>
{
    protected abstract T Read(@DisallowNull RegistryFriendlyByteBuf bytebuffer);

    protected abstract void Write(T object , @DisallowNull RegistryFriendlyByteBuf bytebuffer);

    public StreamCodec<RegistryFriendlyByteBuf , T> GetStreamCodec() {
        return CustomPacketPayload.codec(this::Write , this::Read);
    }
}

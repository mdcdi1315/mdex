package com.github.mdcdi1315.mdex.networking;

import com.github.mdcdi1315.DotNetLayer.System.ArgumentNullException;

import com.github.mdcdi1315.mdex.MDEXModInstance;

import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public final class TeleportingRequestPacket
    implements CustomPacketPayload
{
    public static final Type<TeleportingRequestPacket> TYPE = new Type<>(
            MDEXModInstance.ID("teleporting_request_packet")
    );

    public static StreamCodec<RegistryFriendlyByteBuf, TeleportingRequestPacket> GetNetworkCodec() { return new NetCodec(); }

    private static final class NetCodec
        implements StreamCodec<RegistryFriendlyByteBuf, TeleportingRequestPacket>
    {
        @Override
        public TeleportingRequestPacket decode(RegistryFriendlyByteBuf buffer) {
            return new TeleportingRequestPacket(BlockPos.STREAM_CODEC.decode(buffer));
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buffer, TeleportingRequestPacket packet) {
            BlockPos.STREAM_CODEC.encode(buffer, packet.GetTeleporterPosition());
        }
    }

    private final BlockPos position;

    public TeleportingRequestPacket(BlockPos pos)
    {
        ArgumentNullException.ThrowIfNull(pos, "pos");
        position = pos;
    }

    public BlockPos GetTeleporterPosition() { return position; }

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }
}

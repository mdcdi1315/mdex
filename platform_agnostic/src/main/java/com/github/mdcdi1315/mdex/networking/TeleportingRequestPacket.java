package com.github.mdcdi1315.mdex.networking;

import com.github.mdcdi1315.DotNetLayer.System.ArgumentNullException;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;

public final class TeleportingRequestPacket
{
    private final BlockPos position;

    public TeleportingRequestPacket(BlockPos pos)
    {
        ArgumentNullException.ThrowIfNull(pos, "pos");
        position = pos;
    }

    public static TeleportingRequestPacket Decode(FriendlyByteBuf bbf) { return new TeleportingRequestPacket(bbf.readBlockPos()); }

    public static void Encode(TeleportingRequestPacket packet, FriendlyByteBuf bbf) { bbf.writeBlockPos(packet.position); }

    public BlockPos GetTeleporterPosition() { return position; }
}
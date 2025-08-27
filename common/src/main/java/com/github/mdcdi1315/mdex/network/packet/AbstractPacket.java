package com.github.mdcdi1315.mdex.network.packet;

import com.github.mdcdi1315.DotNetLayer.System.ArgumentNullException;
import com.github.mdcdi1315.mdex.MDEXBalmLayer;
import com.github.mdcdi1315.mdex.network.ModNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public abstract class AbstractPacket<TSelf extends AbstractPacket<TSelf>>
        implements CustomPacketPayload
{
    private Type<TSelf> payloadtype;

    public static <T extends AbstractPacket<T>> Type<T> ConstructPacketType(String name)
    {
        ArgumentNullException.ThrowIfNull(name , "name");
        return new Type<>(MDEXBalmLayer.id(String.format("networking/%s" , name)));
    }

    public AbstractPacket()
    {
        payloadtype = ModNetworking.GetPayloadType(getClass());
    }

    @Override
    public final Type<TSelf> type() {
        return payloadtype;
    }
}

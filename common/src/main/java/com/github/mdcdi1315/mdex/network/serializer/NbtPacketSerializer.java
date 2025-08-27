package com.github.mdcdi1315.mdex.network.serializer;

import com.github.mdcdi1315.mdex.network.packet.AbstractPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;

public abstract class NbtPacketSerializer<TD extends AbstractPacket<TD>>
    extends PacketSerializer<TD>
{
    @Override
    protected final TD Read(RegistryFriendlyByteBuf bytebuffer) {
        return Read(bytebuffer.readNbt());
    }

    @Override
    protected final void Write(TD object, RegistryFriendlyByteBuf bytebuffer) {
        bytebuffer.writeNbt(Write(object));
    }

    protected abstract TD Read(CompoundTag ctg);

    protected abstract Tag Write(TD object);
}

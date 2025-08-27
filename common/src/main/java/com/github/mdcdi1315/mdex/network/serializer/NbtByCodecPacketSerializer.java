package com.github.mdcdi1315.mdex.network.serializer;

import com.github.mdcdi1315.DotNetLayer.System.AggregateException;
import com.github.mdcdi1315.mdex.network.packet.AbstractPacket;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;

public abstract class NbtByCodecPacketSerializer<TD extends AbstractPacket<TD>>
    extends NbtPacketSerializer<TD>
{
    private Codec<TD> codec;

    protected abstract Codec<TD> GetSerializationCodec();

    public NbtByCodecPacketSerializer() {
        codec = GetSerializationCodec();
    }

    @SuppressWarnings("all")
    protected final TD Read(CompoundTag ctg)
    {
        DataResult<Pair<TD , Tag>> dr = codec.decode(NbtOps.INSTANCE , ctg);
        if (dr instanceof DataResult.Error<Pair<TD, Tag>> err)
        {
            throw new AggregateException(String.format("NBT decode failed.\nDecode failure reason: %s" , err.message()));
        }
        // The .get() result will never throw, check the above if statement.
        return dr.result().get().getFirst();
    }

    @SuppressWarnings("all")
    protected final Tag Write(TD object)
    {
        NbtOps ops = NbtOps.INSTANCE;
        DataResult<Tag> dr = codec.encode(object , ops , ops.empty());
        if (dr instanceof DataResult.Error<Tag> err)
        {
            throw new AggregateException(String.format("NBT encode failed.\nDecode failure reason: %s" , err.message()));
        }
        // The .get() result will never throw, check the above if statement.
        return dr.result().get();
    }
}

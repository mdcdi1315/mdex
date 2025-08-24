package com.github.mdcdi1315.mdex.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.ListCodec;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicateType;

import java.util.List;

public abstract class AbstractModdedBlockPredicateType
    <T extends AbstractModdedBlockPredicate>
    implements BlockPredicateType<T>
{
    public static <T extends AbstractModdedBlockPredicate> com.mojang.serialization.codecs.RecordCodecBuilder<T, java.util.List<String>> GetBaseCodec()
    {
        return new ListCodec<>(Codec.STRING).optionalFieldOf("modids" , List.of()).forGetter((T inst) -> inst.ModIds);
    }

    private final Codec<T> codec;

    protected AbstractModdedBlockPredicateType() {
        codec = GetCodecInstance();
    }

    protected abstract Codec<T> GetCodecInstance();

    @Override
    public final Codec<T> codec() {
        return codec;
    }
}

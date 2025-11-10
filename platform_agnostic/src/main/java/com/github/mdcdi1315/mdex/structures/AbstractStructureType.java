package com.github.mdcdi1315.mdex.structures;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.world.level.levelgen.structure.StructureType;

import java.util.List;

public abstract class AbstractStructureType<TS extends AbstractStructure>
    implements StructureType<TS>
{
    public static <T extends AbstractStructure> RecordCodecBuilder<T, List<String>> GetBaseCodec()
    {
        return Codec.STRING.listOf().optionalFieldOf("modids" , List.of()).forGetter((T inst) -> inst.ModIds);
    }

    private final MapCodec<TS> codec;

    protected AbstractStructureType() {
        codec = GetCodecInstance();
    }

    protected abstract MapCodec<TS> GetCodecInstance();

    public final MapCodec<TS> codec() { return codec; }
}

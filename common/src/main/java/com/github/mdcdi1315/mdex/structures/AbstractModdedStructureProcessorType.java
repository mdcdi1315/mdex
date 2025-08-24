package com.github.mdcdi1315.mdex.structures;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.ListCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

import java.util.List;

public abstract class AbstractModdedStructureProcessorType<T extends AbstractModdedStructureProcessor>
    implements StructureProcessorType<T>
{
    public static <T extends AbstractModdedStructureProcessor> RecordCodecBuilder<T, java.util.List<String>> GetBaseCodec()
    {
        return new ListCodec<>(Codec.STRING).optionalFieldOf("modids" , List.of()).forGetter((T inst) -> inst.ModIds);
    }

    private final Codec<T> codec;

    protected AbstractModdedStructureProcessorType() {
        codec = GetCodecInstance();
    }

    protected abstract Codec<T> GetCodecInstance();

    public final Codec<T> codec() {
        return codec;
    }
}

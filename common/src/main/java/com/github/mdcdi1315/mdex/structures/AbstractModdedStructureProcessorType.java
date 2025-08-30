package com.github.mdcdi1315.mdex.structures;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

import java.util.List;

public abstract class AbstractModdedStructureProcessorType<T extends AbstractModdedStructureProcessor>
        implements StructureProcessorType<T>
{
    public static <T extends AbstractModdedStructureProcessor> RecordCodecBuilder<T, java.util.List<String>> GetBaseCodec()
    {
        return Codec.STRING.listOf().optionalFieldOf("modids" , List.of()).forGetter((T inst) -> inst.ModIds);
    }

    private final MapCodec<T> codec;

    protected AbstractModdedStructureProcessorType() {
        codec = GetCodecInstance();
    }

    protected abstract MapCodec<T> GetCodecInstance();

    public final MapCodec<T> codec() {
        return codec;
    }
}
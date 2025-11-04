package com.github.mdcdi1315.mdex.structures;

import com.github.mdcdi1315.basemodslib.codecs.CodecUtils;

import com.github.mdcdi1315.mdex.block.blockstateproviders.AbstractBlockStateProvider;

import com.mojang.serialization.Codec;

public final class BlockPhasesStructureProcessorType
    extends AbstractModdedStructureProcessorType<BlockPhasesStructureProcessor>
{
    public static BlockPhasesStructureProcessorType INSTANCE = new BlockPhasesStructureProcessorType();

    @Override
    protected Codec<BlockPhasesStructureProcessor> GetCodecInstance() {
        return CodecUtils.CreateCodecDirect(
                GetBaseCodec(),
                AbstractBlockStateProvider.CODEC.listOf().fieldOf("phases").forGetter((BlockPhasesStructureProcessor p) -> p.States),
                CodecUtils.FLOAT_PROBABILITY.fieldOf("probability").forGetter((BlockPhasesStructureProcessor p) -> p.probability),
                BlockPhasesStructureProcessor::new
        );
    }
}

package com.github.mdcdi1315.mdex.structures;

import com.github.mdcdi1315.mdex.block.blockstateproviders.AbstractBlockStateProvider;
import com.github.mdcdi1315.mdex.codecs.CodecUtils;
import com.mojang.serialization.MapCodec;

public final class BlockPhasesStructureProcessorType
    extends AbstractModdedStructureProcessorType<BlockPhasesStructureProcessor>
{
    public static BlockPhasesStructureProcessorType INSTANCE = new BlockPhasesStructureProcessorType();

    @Override
    protected MapCodec<BlockPhasesStructureProcessor> GetCodecInstance() {
        return CodecUtils.CreateMapCodecDirect(
                GetBaseCodec(),
                AbstractBlockStateProvider.CODEC.listOf().fieldOf("phases").forGetter((BlockPhasesStructureProcessor p) -> p.States),
                CodecUtils.FLOAT_PROBABILITY.fieldOf("probability").forGetter((BlockPhasesStructureProcessor p) -> p.probability),
                BlockPhasesStructureProcessor::new
        );
    }
}

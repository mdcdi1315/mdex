package com.github.mdcdi1315.mdex.structures;

import com.github.mdcdi1315.mdex.block.blockstateproviders.AbstractBlockStateProvider;
import com.github.mdcdi1315.mdex.codecs.CodecUtils;
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
                Codec.floatRange(0f , 1f).fieldOf("probability").forGetter((BlockPhasesStructureProcessor p) -> p.probability),
                BlockPhasesStructureProcessor::new
        );
    }
}

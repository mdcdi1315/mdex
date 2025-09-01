package com.github.mdcdi1315.mdex.structures;

import com.github.mdcdi1315.mdex.codecs.CodecUtils;
import com.github.mdcdi1315.mdex.util.BlockIdOrBlockTagEntry;

import com.mojang.serialization.Codec;

public final class ModdedProtectedBlocksProcessorType
    extends AbstractModdedStructureProcessorType<ModdedProtectedBlocksProcessor>
{
    public static ModdedProtectedBlocksProcessorType INSTANCE = new ModdedProtectedBlocksProcessorType();

    @Override
    protected Codec<ModdedProtectedBlocksProcessor> GetCodecInstance() {
        return CodecUtils.CreateCodecDirect(
                GetBaseCodec(),
                BlockIdOrBlockTagEntry.GetCodec().listOf().fieldOf("value").forGetter((ModdedProtectedBlocksProcessor p) -> p.entries),
                ModdedProtectedBlocksProcessor::new
        );
    }
}

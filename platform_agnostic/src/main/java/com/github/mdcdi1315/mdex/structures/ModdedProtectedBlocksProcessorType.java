package com.github.mdcdi1315.mdex.structures;

import com.github.mdcdi1315.basemodslib.codecs.CodecUtils;

import com.github.mdcdi1315.mdex.util.BlockIdOrBlockTagEntry;

import com.mojang.serialization.MapCodec;

public final class ModdedProtectedBlocksProcessorType
    extends AbstractModdedStructureProcessorType<ModdedProtectedBlocksProcessor>
{
    public static final ModdedProtectedBlocksProcessorType INSTANCE = new ModdedProtectedBlocksProcessorType();

    @Override
    protected MapCodec<ModdedProtectedBlocksProcessor> GetCodecInstance() {
        return CodecUtils.CreateMapCodecDirect(
                GetBaseCodec(),
                BlockIdOrBlockTagEntry.GetCodec().listOf().fieldOf("value").forGetter((ModdedProtectedBlocksProcessor p) -> p.entries),
                ModdedProtectedBlocksProcessor::new
        );
    }
}

package com.github.mdcdi1315.mdex.structures;

import com.mojang.serialization.MapCodec;
import com.github.mdcdi1315.mdex.codecs.CodecUtils;
import com.github.mdcdi1315.mdex.util.BlockIdOrBlockTagEntry;


public final class ModdedProtectedBlocksProcessorType
    extends AbstractModdedStructureProcessorType<ModdedProtectedBlocksProcessor>
{
    public static ModdedProtectedBlocksProcessorType INSTANCE = new ModdedProtectedBlocksProcessorType();

    @Override
    protected MapCodec<ModdedProtectedBlocksProcessor> GetCodecInstance() {
        return CodecUtils.CreateMapCodecDirect(
                GetBaseCodec(),
                BlockIdOrBlockTagEntry.GetCodec().listOf().fieldOf("value").forGetter((ModdedProtectedBlocksProcessor p) -> p.entries),
                ModdedProtectedBlocksProcessor::new
        );
    }
}

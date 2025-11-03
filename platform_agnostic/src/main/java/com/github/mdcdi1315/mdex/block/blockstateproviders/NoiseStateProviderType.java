package com.github.mdcdi1315.mdex.block.blockstateproviders;

import com.github.mdcdi1315.basemodslib.codecs.CodecUtils;
import com.github.mdcdi1315.mdex.util.CompilableBlockState;
import com.mojang.serialization.Codec;

public final class NoiseStateProviderType
    extends AbstractBlockStateProviderType<NoiseStateProvider>
{
    public static final NoiseStateProviderType INSTANCE = new NoiseStateProviderType();

    @Override
    protected Codec<NoiseStateProvider> GetCodecInstance() {
        return CodecUtils.CreateCodecDirect(
                NoiseStateProvider.GetBaseCodec(),
                CompilableBlockState.GetCodec().listOf().fieldOf("states").forGetter((s) -> s.States),
                NoiseStateProvider::new
        );
    }
}

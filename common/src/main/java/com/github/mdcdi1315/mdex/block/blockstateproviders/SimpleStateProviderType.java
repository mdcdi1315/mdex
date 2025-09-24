package com.github.mdcdi1315.mdex.block.blockstateproviders;

import com.github.mdcdi1315.mdex.codecs.CodecUtils;
import com.github.mdcdi1315.mdex.util.CompilableBlockState;

import com.mojang.serialization.MapCodec;

public final class SimpleStateProviderType
    extends AbstractBlockStateProviderType<SimpleStateProvider>
{
    public static final SimpleStateProviderType INSTANCE = new SimpleStateProviderType();

    @Override
    protected MapCodec<SimpleStateProvider> GetCodecInstance() {
        return CodecUtils.CreateMapCodecDirect(
                CompilableBlockState.GetCodec().fieldOf("state").forGetter((s) -> s.State),
                SimpleStateProvider::new
        );
    }
}

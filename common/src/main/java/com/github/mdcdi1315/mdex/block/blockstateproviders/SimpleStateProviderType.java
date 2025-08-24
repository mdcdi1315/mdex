package com.github.mdcdi1315.mdex.block.blockstateproviders;

import com.mojang.serialization.Codec;
import com.github.mdcdi1315.mdex.util.CompilableTargetBlockState;

public final class SimpleStateProviderType
    extends AbstractBlockStateProviderType<SimpleStateProvider>
{
    private final Codec<SimpleStateProvider> codec;

    public SimpleStateProviderType()
    {
        codec = CompilableTargetBlockState.GetCodec().fieldOf("state").xmap(SimpleStateProvider::new, (p_68804_) -> p_68804_.state).codec();
    }

    @Override
    public Codec<SimpleStateProvider> Codec() {
        return codec;
    }
}

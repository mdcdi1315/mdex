package com.github.mdcdi1315.mdex.block.blockstateproviders;

import com.mojang.serialization.MapCodec;
import com.github.mdcdi1315.mdex.util.CompilableBlockState;

public final class SimpleStateProviderType
    extends AbstractBlockStateProviderType<SimpleStateProvider>
{
    private final MapCodec<SimpleStateProvider> codec;

    public SimpleStateProviderType()
    {
        codec = CompilableBlockState.GetCodec().fieldOf("state").xmap(SimpleStateProvider::new, (p_68804_) -> p_68804_.state);
    }

    @Override
    public MapCodec<SimpleStateProvider> Codec() {
        return codec;
    }
}

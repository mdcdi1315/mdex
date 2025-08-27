package com.github.mdcdi1315.mdex.block.blockstateproviders;

import com.github.mdcdi1315.mdex.util.CompilableTargetBlockState;
import com.mojang.serialization.MapCodec;


public final class RotatedBlockProviderType
    extends AbstractBlockStateProviderType<RotatedBlockProvider>
{
    private final MapCodec<RotatedBlockProvider> codec;

    public RotatedBlockProviderType()
    {
        codec = CompilableTargetBlockState.GetCodec().fieldOf("state").xmap(RotatedBlockProvider::new, (p) -> p.block);
    }

    @Override
    public MapCodec<RotatedBlockProvider> Codec() {
        return codec;
    }
}

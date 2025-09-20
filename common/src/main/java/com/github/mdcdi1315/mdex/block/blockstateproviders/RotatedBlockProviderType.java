package com.github.mdcdi1315.mdex.block.blockstateproviders;

import com.github.mdcdi1315.mdex.util.CompilableBlockState;

import com.mojang.serialization.Codec;

public final class RotatedBlockProviderType
    extends AbstractBlockStateProviderType<RotatedBlockProvider>
{
    private final Codec<RotatedBlockProvider> codec;

    public RotatedBlockProviderType()
    {
        codec = CompilableBlockState.GetCodec().fieldOf("state").xmap(RotatedBlockProvider::new, (p) -> p.block).codec();
    }

    @Override
    public Codec<RotatedBlockProvider> Codec() {
        return codec;
    }
}

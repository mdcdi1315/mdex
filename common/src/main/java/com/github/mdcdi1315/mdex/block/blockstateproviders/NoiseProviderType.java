package com.github.mdcdi1315.mdex.block.blockstateproviders;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public final class NoiseProviderType
    extends AbstractBlockStateProviderType<NoiseProvider>
{
    private final MapCodec<NoiseProvider> codec;

    public NoiseProviderType()
    {
        codec = RecordCodecBuilder.mapCodec((o) -> NoiseProvider.noiseProviderCodec(o).apply(o , NoiseProvider::new));
    }

    @Override
    public MapCodec<NoiseProvider> Codec() {
        return codec;
    }
}

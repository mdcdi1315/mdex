package com.github.mdcdi1315.mdex.block.blockstateproviders;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public final class NoiseProviderType
    extends AbstractBlockStateProviderType<NoiseProvider>
{
    private final Codec<NoiseProvider> codec;

    public NoiseProviderType()
    {
        codec = RecordCodecBuilder.create((o) -> NoiseProvider.noiseProviderCodec(o).apply(o , NoiseProvider::new));
    }

    @Override
    public Codec<NoiseProvider> Codec() {
        return codec;
    }
}

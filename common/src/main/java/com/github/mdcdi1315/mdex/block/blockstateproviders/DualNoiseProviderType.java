package com.github.mdcdi1315.mdex.block.blockstateproviders;

import com.mojang.serialization.Codec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.InclusiveRange;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

public final class DualNoiseProviderType
    extends AbstractBlockStateProviderType<DualNoiseProvider>
{
    private final Codec<DualNoiseProvider> codec;

    public DualNoiseProviderType()
    {
        codec = RecordCodecBuilder.create(
                (p_191414_) -> p_191414_.group(
                        InclusiveRange.codec(Codec.INT, 1, 64).fieldOf("variety").forGetter((p_191416_) -> p_191416_.variety),
                        NormalNoise.NoiseParameters.CODEC.fieldOf("slow_noise").forGetter((p_191412_) -> p_191412_.slowNoiseParameters),
                        ExtraCodecs.POSITIVE_FLOAT.fieldOf("slow_scale").forGetter((p_191405_) -> p_191405_.slowScale)
                ).and(DualNoiseProvider.noiseProviderCodec(p_191414_)).apply(p_191414_, DualNoiseProvider::new));
    }

    @Override
    public Codec<DualNoiseProvider> Codec() {
        return codec;
    }
}

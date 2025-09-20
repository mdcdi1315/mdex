package com.github.mdcdi1315.mdex.block.blockstateproviders;

import com.github.mdcdi1315.mdex.codecs.CodecUtils;
import com.github.mdcdi1315.mdex.util.CompilableBlockState;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class NoiseThresholdProviderType
      extends AbstractBlockStateProviderType<NoiseThresholdProvider>
{
    private final Codec<NoiseThresholdProvider> codec;

    public NoiseThresholdProviderType()
    {
        codec = RecordCodecBuilder.create((p_191486_) -> {
            Codec<CompilableBlockState> codec = CompilableBlockState.GetCodec();
            return NoiseThresholdProvider.noiseCodec(p_191486_).and(p_191486_.group(
                            Codec.floatRange(-1.0F, 1.0F).fieldOf("threshold").forGetter((p) -> p.threshold),
                            CodecUtils.FLOAT_PROBABILITY.fieldOf("high_chance").forGetter((p) -> p.highChance),
                            codec.fieldOf("default_state").forGetter((p) -> p.defaultState),
                            Codec.list(codec).fieldOf("low_states").forGetter((p) -> p.lowStates),
                            Codec.list(codec).fieldOf("high_states").forGetter((p) -> p.highStates)
                    )
            ).apply(p_191486_, NoiseThresholdProvider::new);
        });
    }

    @Override
    public Codec<NoiseThresholdProvider> Codec() {
        return codec;
    }
}

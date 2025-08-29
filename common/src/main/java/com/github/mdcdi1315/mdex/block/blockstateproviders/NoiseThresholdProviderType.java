package com.github.mdcdi1315.mdex.block.blockstateproviders;

import com.github.mdcdi1315.mdex.util.CompilableTargetBlockState;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class NoiseThresholdProviderType
      extends AbstractBlockStateProviderType<NoiseThresholdProvider>
{
    private final MapCodec<NoiseThresholdProvider> codec;

    public NoiseThresholdProviderType()
    {
        codec = RecordCodecBuilder.mapCodec((n) -> {
            Codec<CompilableTargetBlockState> codec = CompilableTargetBlockState.GetCodec().codec();
            return NoiseThresholdProvider.noiseCodec(n).and(n.group(
                            Codec.floatRange(-1.0F, 1.0F).fieldOf("threshold").forGetter((p) -> p.threshold),
                            Codec.floatRange(0.0F, 1.0F).fieldOf("high_chance").forGetter((p) -> p.highChance),
                            codec.fieldOf("default_state").forGetter((p) -> p.defaultState),
                            Codec.list(codec).fieldOf("low_states").forGetter((p) -> p.lowStates),
                            Codec.list(codec).fieldOf("high_states").forGetter((p) -> p.highStates)
                    )
            ).apply(n, NoiseThresholdProvider::new);
        });
    }

    @Override
    public MapCodec<NoiseThresholdProvider> Codec() {
        return codec;
    }
}

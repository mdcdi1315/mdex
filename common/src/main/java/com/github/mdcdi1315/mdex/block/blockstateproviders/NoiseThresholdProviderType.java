package com.github.mdcdi1315.mdex.block.blockstateproviders;

import com.github.mdcdi1315.mdex.codecs.CodecUtils;
import com.github.mdcdi1315.mdex.util.CompilableBlockState;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;

public final class NoiseThresholdProviderType
    extends AbstractBlockStateProviderType<NoiseThresholdProvider>
{
    public static final NoiseThresholdProviderType INSTANCE = new NoiseThresholdProviderType();

    @Override
    protected MapCodec<NoiseThresholdProvider> GetCodecInstance() {
        Codec<CompilableBlockState> codec = CompilableBlockState.GetCodec();
        return CodecUtils.CreateMapCodecDirect(
                NoiseThresholdProvider.GetBaseCodec(),
                Codec.floatRange(-1.0F, 1.0F).fieldOf("threshold").forGetter((p) -> p.Threshold),
                CodecUtils.FLOAT_PROBABILITY.fieldOf("high_chance").forGetter((p) -> p.HighChance),
                codec.fieldOf("default_state").forGetter((p) -> p.DefaultState),
                Codec.list(codec).fieldOf("low_states").forGetter((p) -> p.LowStates),
                Codec.list(codec).fieldOf("high_states").forGetter((p) -> p.HighStates),
                NoiseThresholdProvider::new
        );
    }
}

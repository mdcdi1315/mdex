package com.github.mdcdi1315.mdex.features.orevein;

import com.github.mdcdi1315.basemodslib.codecs.CodecUtils;

import com.github.mdcdi1315.mdex.dco_logic.Compilable;
import com.github.mdcdi1315.mdex.util.CompilableBlockState;

import com.mojang.serialization.Codec;

public final class BaseStonePlacementSettings
    implements Compilable
{
    public CompilableBlockState StoneState;
    public final float NoiseDensityThreshold, PlacementProbability;

    public static Codec<BaseStonePlacementSettings> GetCodec()
    {
        return CodecUtils.CreateCodecDirect(
                CompilableBlockState.GetCodec().fieldOf("stone_state").forGetter((bs) -> bs.StoneState),
                Codec.floatRange(0f , 6f).fieldOf("noise_density_threshold").forGetter((bs) -> bs.NoiseDensityThreshold),
                CodecUtils.FLOAT_PROBABILITY.optionalFieldOf("probability" , 0.85f).forGetter((bs) -> bs.PlacementProbability),
                BaseStonePlacementSettings::new
        );
    }

    public BaseStonePlacementSettings(CompilableBlockState state, float density, float probability)
    {
        StoneState = state;
        NoiseDensityThreshold = density;
        PlacementProbability = probability;
    }

    @Override
    public void Compile() {
        StoneState.Compile();
    }

    @Override
    public boolean IsCompiled() {
        return StoneState.IsCompiled();
    }
}

package com.github.mdcdi1315.mdex.features.config;

import com.github.mdcdi1315.mdex.codecs.CodecUtils;
import com.github.mdcdi1315.mdex.util.SingleBlockState;

import com.mojang.serialization.Codec;

import java.util.List;

public final class ModdedOreFeatureConfiguration
    extends ModdedFeatureConfiguration
{
    public List<SingleBlockState> TargetStates;
    public final int Size;
    public final float DiscardChanceOnAirExposure;

    public ModdedOreFeatureConfiguration(List<String> modids, List<SingleBlockState> states , int size , float discardChanceOnAirExposure)
    {
        super(modids);
        TargetStates = states;
        Size = size;
        DiscardChanceOnAirExposure = discardChanceOnAirExposure;
        Compile();
    }

    public static Codec<ModdedOreFeatureConfiguration> GetCodec()
    {
        return CodecUtils.CreateCodecDirect(
                GetBaseCodec(),
                SingleBlockState.GetCodec().listOf().fieldOf("targets").forGetter((ModdedOreFeatureConfiguration f) -> f.TargetStates),
                Codec.intRange(0 , 64).optionalFieldOf("size" , 12).forGetter((ModdedOreFeatureConfiguration f) -> f.Size),
                CodecUtils.FLOAT_PROBABILITY.optionalFieldOf("discard_chance_on_air_exposure" , 0.48f).forGetter((ModdedOreFeatureConfiguration f) -> f.DiscardChanceOnAirExposure),
                ModdedOreFeatureConfiguration::new
        );
    }


    @Override
    protected void compileConfigData() {
        for (var s : TargetStates)
        {
            s.Compile();
        }
    }

    @Override
    protected void invalidateUntransformedFields() {

    }
}

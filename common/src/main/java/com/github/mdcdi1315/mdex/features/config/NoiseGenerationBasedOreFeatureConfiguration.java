package com.github.mdcdi1315.mdex.features.config;


import net.minecraft.core.Holder;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.ListCodec;
import com.github.mdcdi1315.mdex.codecs.CodecUtils;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import com.github.mdcdi1315.mdex.util.SingleBlockState;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

import java.util.List;

public final class NoiseGenerationBasedOreFeatureConfiguration
    extends ModdedFeatureConfiguration
{
    public List<SingleBlockState> TargetStates;
    public IntProvider Size;
    public IntProvider Y_Scale;
    public final float DiscardChanceOnAirExposure;
    public Holder<NormalNoise.NoiseParameters> NoiseParameters;

    public static Codec<NoiseGenerationBasedOreFeatureConfiguration> GetCodec()
    {
        return CodecUtils.CreateCodecDirect(
                GetBaseCodec(),
                new ListCodec<>(SingleBlockState.GetCodec()).fieldOf("targets").forGetter((f) -> f.TargetStates),
                IntProvider.codec(1 , 58).optionalFieldOf("size" , ConstantInt.of(12)).forGetter((f) -> f.Size),
                IntProvider.codec(1 , 18).optionalFieldOf("y_scale" , ConstantInt.of(2)).forGetter((f) -> f.Y_Scale),
                NormalNoise.NoiseParameters.CODEC.fieldOf("noise").forGetter((f) -> f.NoiseParameters),
                Codec.floatRange(0f , 1f).optionalFieldOf("discard_chance_on_air_exposure" , 0.48f).forGetter((f) -> f.DiscardChanceOnAirExposure),
                NoiseGenerationBasedOreFeatureConfiguration::new
        );
    }

    public NoiseGenerationBasedOreFeatureConfiguration(List<String> modids, List<SingleBlockState> states , IntProvider size , IntProvider y_scale , Holder<NormalNoise.NoiseParameters> params , float discardChanceOnAirExposure)
    {
        super(modids);
        TargetStates = states;
        Size = size;
        Y_Scale = y_scale;
        NoiseParameters = params;
        DiscardChanceOnAirExposure = discardChanceOnAirExposure;
        Compile();
    }

    @Override
    protected void compileConfigData()
    {
        for (var i : TargetStates)
        {
            i.Compile();
        }
    }

    @Override
    protected void invalidateUntransformedFields() {

    }
}

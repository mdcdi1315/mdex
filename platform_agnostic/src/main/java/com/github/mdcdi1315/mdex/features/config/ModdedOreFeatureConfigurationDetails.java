package com.github.mdcdi1315.mdex.features.config;

import com.github.mdcdi1315.basemodslib.codecs.CodecUtils;
import com.github.mdcdi1315.mdex.util.SingleTargetBlockState;

import com.mojang.serialization.MapCodec;

import java.util.List;

public class ModdedOreFeatureConfigurationDetails
    implements IModdedFeatureConfigurationDetails
{
    public List<SingleTargetBlockState> TargetStates;
    public final byte Size;
    public final float DiscardChanceOnAirExposure;

    public ModdedOreFeatureConfigurationDetails(List<SingleTargetBlockState> states , byte size , float discardChanceOnAirExposure)
    {
        TargetStates = states;
        Size = size;
        DiscardChanceOnAirExposure = discardChanceOnAirExposure;
    }

    public static MapCodec<ModdedOreFeatureConfigurationDetails> GetCodec()
    {
        return CodecUtils.CreateMapCodecDirect(
                SingleTargetBlockState.GetListCodec().fieldOf("targets").forGetter((ModdedOreFeatureConfigurationDetails f) -> f.TargetStates),
                CodecUtils.ByteRange(0 , 64).optionalFieldOf("size" , (byte)12).forGetter((ModdedOreFeatureConfigurationDetails f) -> f.Size),
                CodecUtils.FLOAT_PROBABILITY.optionalFieldOf("discard_chance_on_air_exposure" , 0.48f).forGetter((ModdedOreFeatureConfigurationDetails f) -> f.DiscardChanceOnAirExposure),
                ModdedOreFeatureConfigurationDetails::new
        );
    }


    @Override
    public void Compile() {
        for (var s : TargetStates)
        {
            s.Compile();
        }
    }

}

package com.github.mdcdi1315.mdex.features.config;

import com.github.mdcdi1315.basemodslib.codecs.CodecUtils;

import com.github.mdcdi1315.mdex.dco_logic.DCOUtils;
import com.github.mdcdi1315.mdex.util.SingleTargetBlockState;
import com.github.mdcdi1315.mdex.features.orevein.RareBlockPlacementSettings;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.Holder;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

import java.util.List;

public class ModdedOreVeinFeatureConfigurationDetails
    implements IModdedFeatureConfigurationDetails
{
    public RareBlockPlacementSettings RarePlacementSettings;
    public List<SingleTargetBlockState> TargetStates;
    public IntProvider Size;
    public IntProvider Y_Scale;
    public Holder<NormalNoise.NoiseParameters> Parameters;
    public final float DiscardChanceOnAirExposure;

    public static MapCodec<ModdedOreVeinFeatureConfigurationDetails> GetCodec()
    {
        return CodecUtils.CreateMapCodecDirect(
                RareBlockPlacementSettings.GetCodec().fieldOf("rare_placement").forGetter((f) -> f.RarePlacementSettings),
                SingleTargetBlockState.GetListCodec().fieldOf("targets").forGetter((ModdedOreVeinFeatureConfigurationDetails f) -> f.TargetStates),
                NormalNoise.NoiseParameters.CODEC.fieldOf("noise").forGetter((ModdedOreVeinFeatureConfigurationDetails f) -> f.Parameters),
                IntProvider.codec(5 , 48).optionalFieldOf("size" , ConstantInt.of(12)).forGetter((f) -> f.Size),
                IntProvider.codec(4 , 26).optionalFieldOf("y_scale" , ConstantInt.of(2)).forGetter((f) -> f.Y_Scale),
                CodecUtils.FLOAT_PROBABILITY.optionalFieldOf("discard_chance_on_air_exposure" , 0.48f).forGetter((ModdedOreVeinFeatureConfigurationDetails f) -> f.DiscardChanceOnAirExposure),
                ModdedOreVeinFeatureConfigurationDetails::new
        );
    }

    public ModdedOreVeinFeatureConfigurationDetails(RareBlockPlacementSettings s , List<SingleTargetBlockState> states , Holder<NormalNoise.NoiseParameters> p , IntProvider size , IntProvider yscale , float discardChanceOnAirExposure)
    {
        RarePlacementSettings = s;
        TargetStates = states;
        Parameters = p;
        Size = size;
        Y_Scale = yscale;
        DiscardChanceOnAirExposure = discardChanceOnAirExposure;
        Compile();
    }

    @Override
    public void Compile() {
        RarePlacementSettings.Compile();
        if (!RarePlacementSettings.IsCompiled())  {
            throw new FeatureCompilationFailureException();
        }
        if (!DCOUtils.CompileAllOrFail(TargetStates)) {
            throw new FeatureCompilationFailureException();
        }
    }

}

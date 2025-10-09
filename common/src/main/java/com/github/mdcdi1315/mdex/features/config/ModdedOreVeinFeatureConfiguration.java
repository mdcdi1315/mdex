package com.github.mdcdi1315.mdex.features.config;

import com.github.mdcdi1315.mdex.codecs.CodecUtils;
import com.github.mdcdi1315.mdex.util.Extensions;
import com.github.mdcdi1315.mdex.util.SingleTargetBlockState;
import com.github.mdcdi1315.mdex.features.orevein.RareBlockPlacementSettings;

import net.minecraft.core.Holder;
import com.mojang.serialization.Codec;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

import java.util.List;

public final class ModdedOreVeinFeatureConfiguration
    extends ModdedFeatureConfiguration
{
    public RareBlockPlacementSettings RarePlacementSettings;
    public List<SingleTargetBlockState> TargetStates;
    public IntProvider Size;
    public IntProvider Y_Scale;
    public Holder<NormalNoise.NoiseParameters> Parameters;
    public final float DiscardChanceOnAirExposure;

    public static Codec<ModdedOreVeinFeatureConfiguration> GetCodec()
    {
        return CodecUtils.CreateCodecDirect(
                GetBaseCodec(),
                RareBlockPlacementSettings.GetCodec().fieldOf("rare_placement").forGetter((f) -> f.RarePlacementSettings),
                SingleTargetBlockState.GetListCodec().fieldOf("targets").forGetter((ModdedOreVeinFeatureConfiguration f) -> f.TargetStates),
                NormalNoise.NoiseParameters.CODEC.fieldOf("noise").forGetter((ModdedOreVeinFeatureConfiguration f) -> f.Parameters),
                IntProvider.codec(5 , 48).optionalFieldOf("size" , ConstantInt.of(12)).forGetter((f) -> f.Size),
                IntProvider.codec(4 , 26).optionalFieldOf("y_scale" , ConstantInt.of(2)).forGetter((f) -> f.Y_Scale),
                CodecUtils.FLOAT_PROBABILITY.optionalFieldOf("discard_chance_on_air_exposure" , 0.48f).forGetter((ModdedOreVeinFeatureConfiguration f) -> f.DiscardChanceOnAirExposure),
                ModdedOreVeinFeatureConfiguration::new
        );
    }

    public ModdedOreVeinFeatureConfiguration(List<String> modids, RareBlockPlacementSettings s , List<SingleTargetBlockState> states , Holder<NormalNoise.NoiseParameters> p , IntProvider size , IntProvider yscale , float discardChanceOnAirExposure)
    {
        super(modids);
        RarePlacementSettings = s;
        TargetStates = states;
        Parameters = p;
        Size = size;
        Y_Scale = yscale;
        DiscardChanceOnAirExposure = discardChanceOnAirExposure;
        Compile();
    }

    @Override
    protected void compileConfigData() {
        RarePlacementSettings.Compile();
        if (!RarePlacementSettings.IsCompiled())
        {
            setConfigAsInvalid();
            return;
        }
        if (!Extensions.CompileAllOrFail(TargetStates)) {
            setConfigAsInvalid();
        }
    }

}

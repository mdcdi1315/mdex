package com.github.mdcdi1315.mdex.features.config;

import com.github.mdcdi1315.mdex.util.Extensions;
import com.github.mdcdi1315.mdex.codecs.CodecUtils;
import com.github.mdcdi1315.mdex.features.orevein.*;
import com.github.mdcdi1315.mdex.util.MDEXException;
import com.github.mdcdi1315.mdex.util.SingleTargetBlockState;

import com.mojang.serialization.Codec;

import net.minecraft.core.Holder;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

import java.util.List;

public final class ModdedClassicOreVeinFeatureConfiguration
    extends ModdedFeatureConfiguration
{
    public IntProvider Size, Y_Scale;
    public final float DiscardChanceOnAirExposure;
    public List<SingleTargetBlockState> TargetStates;
    public Holder<NormalNoise.NoiseParameters> Parameters;
    public RareBlockPlacementSettings RarePlacementSettings;
    public BaseStonePlacementSettings StonePlacementSettings;

    public static Codec<ModdedClassicOreVeinFeatureConfiguration> GetCodec()
    {
        return CodecUtils.CreateCodecDirect(
                GetBaseCodec(),
                IntProvider.codec(5 , 48).optionalFieldOf("size" , ConstantInt.of(12)).forGetter((f) -> f.Size),
                IntProvider.codec(4 , 26).optionalFieldOf("y_scale" , ConstantInt.of(2)).forGetter((f) -> f.Y_Scale),
                CodecUtils.FLOAT_PROBABILITY.optionalFieldOf("discard_chance_on_air_exposure" , 0.48f).forGetter((f) -> f.DiscardChanceOnAirExposure),
                BaseStonePlacementSettings.GetCodec().fieldOf("stone_placement").forGetter((f) -> f.StonePlacementSettings),
                RareBlockPlacementSettings.GetCodec().fieldOf("rare_placement").forGetter((f) -> f.RarePlacementSettings),
                SingleTargetBlockState.GetListCodec().fieldOf("targets").forGetter((f) -> f.TargetStates),
                NormalNoise.NoiseParameters.CODEC.fieldOf("noise").forGetter((f) -> f.Parameters),
                ModdedClassicOreVeinFeatureConfiguration::new
        );
    }

    public ModdedClassicOreVeinFeatureConfiguration(
            List<String> modids,
            IntProvider size,
            IntProvider yscale,
            float discardChanceOnAirExposure,
            BaseStonePlacementSettings bs,
            RareBlockPlacementSettings rs,
            List<SingleTargetBlockState> states ,
            Holder<NormalNoise.NoiseParameters> p
    )
    {
        super(modids);
        Size = size;
        Y_Scale = yscale;
        DiscardChanceOnAirExposure = discardChanceOnAirExposure;
        StonePlacementSettings = bs;
        RarePlacementSettings = rs;
        TargetStates = states;
        Parameters = p;
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
        StonePlacementSettings.Compile();
        if (!StonePlacementSettings.IsCompiled())
        {
            setConfigAsInvalid();
            return;
        }
        if (StonePlacementSettings.NoiseDensityThreshold > RarePlacementSettings.NoiseDensityThreshold) {
            throw new MDEXException("The 'stone_placement.noise_density_threshold' field value cannot be greater than the 'rare_placement.noise_density_threshold' field value.");
        }
        if (!Extensions.CompileAllOrFail(TargetStates)) {
            setConfigAsInvalid();
        }
    }

    @Override
    protected void invalidateUntransformedFields() {

    }
}

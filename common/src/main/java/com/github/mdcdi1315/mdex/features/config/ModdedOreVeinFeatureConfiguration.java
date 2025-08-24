package com.github.mdcdi1315.mdex.features.config;

import com.github.mdcdi1315.mdex.codecs.CodecUtils;
import com.github.mdcdi1315.mdex.util.SingleBlockState;
import com.github.mdcdi1315.mdex.features.orevein.RareBlockPlacementSettings;

import net.minecraft.core.Holder;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.ListCodec;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

import java.util.List;

public final class ModdedOreVeinFeatureConfiguration
    extends ModdedFeatureConfiguration
{
    public RareBlockPlacementSettings RarePlacementSettings;
    public List<SingleBlockState> TargetStates;
    public IntProvider Size;
    public IntProvider Y_Scale;
    public Holder<NormalNoise.NoiseParameters> Parameters;
    public final float DiscardChanceOnAirExposure;

    public static Codec<ModdedOreVeinFeatureConfiguration> GetCodec()
    {
        return CodecUtils.CreateCodecDirect(
                GetBaseCodec(),
                RareBlockPlacementSettings.GetCodec().fieldOf("rare_placement").forGetter((f) -> f.RarePlacementSettings),
                new ListCodec<>(SingleBlockState.GetCodec()).fieldOf("targets").forGetter((ModdedOreVeinFeatureConfiguration f) -> f.TargetStates),
                NormalNoise.NoiseParameters.CODEC.fieldOf("noise").forGetter((ModdedOreVeinFeatureConfiguration f) -> f.Parameters),
                IntProvider.codec(5 , 58).optionalFieldOf("size" , ConstantInt.of(12)).forGetter((f) -> f.Size),
                IntProvider.codec(4 , 18).optionalFieldOf("y_scale" , ConstantInt.of(2)).forGetter((f) -> f.Y_Scale),
                Codec.floatRange(0f , 1f).optionalFieldOf("discard_chance_on_air_exposure" , 0.48f).forGetter((ModdedOreVeinFeatureConfiguration f) -> f.DiscardChanceOnAirExposure),
                ModdedOreVeinFeatureConfiguration::new
        );
    }

    public ModdedOreVeinFeatureConfiguration(List<String> modids, RareBlockPlacementSettings s , List<SingleBlockState> states , Holder<NormalNoise.NoiseParameters> p , IntProvider size , IntProvider yscale , float discardChanceOnAirExposure)
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
        for (var i : TargetStates)
        {
            i.Compile();
            if (!i.IsCompiled()) { setConfigAsInvalid(); return; }
        }
    }

    @Override
    protected void invalidateUntransformedFields() {

    }
}

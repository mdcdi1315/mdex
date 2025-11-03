package com.github.mdcdi1315.mdex.features.config;

import com.github.mdcdi1315.basemodslib.codecs.CodecUtils;

import com.github.mdcdi1315.mdex.dco_logic.DCOUtils;
import com.github.mdcdi1315.mdex.features.orevein.*;
import com.github.mdcdi1315.mdex.util.MDEXException;

import com.mojang.serialization.MapCodec;

public final class ModdedClassicOreVeinFeatureConfigurationDetails
    extends ModdedOreVeinFeatureConfigurationDetails
{
    public BaseStonePlacementSettings StonePlacementSettings;

    public static MapCodec<ModdedClassicOreVeinFeatureConfigurationDetails> GetClassicOreVeinCodec()
    {
        return CodecUtils.CreateMapCodecDirect(
                GetCodec().forGetter((p) -> p),
                BaseStonePlacementSettings.GetCodec().fieldOf("stone_placement").forGetter((f) -> f.StonePlacementSettings),
                ModdedClassicOreVeinFeatureConfigurationDetails::new
        );
    }

    public ModdedClassicOreVeinFeatureConfigurationDetails(
            ModdedOreVeinFeatureConfigurationDetails d,
            BaseStonePlacementSettings bs
    )
    {
        super(
                d.RarePlacementSettings,
                d.TargetStates,
                d.Parameters,
                d.Size,
                d.Y_Scale,
                d.DiscardChanceOnAirExposure
        );
        StonePlacementSettings = bs;
    }

    @Override
    public void Compile() {
        RarePlacementSettings.Compile();
        if (!RarePlacementSettings.IsCompiled()) {
            throw new FeatureCompilationFailureException();
        }
        StonePlacementSettings.Compile();
        if (!StonePlacementSettings.IsCompiled()) {
            throw new FeatureCompilationFailureException();
        }
        if (StonePlacementSettings.NoiseDensityThreshold > RarePlacementSettings.NoiseDensityThreshold) {
            throw new MDEXException("The 'stone_placement.noise_density_threshold' field value cannot be greater than the 'rare_placement.noise_density_threshold' field value.");
        }
        if (!DCOUtils.CompileAllOrFail(TargetStates)) {
            throw new FeatureCompilationFailureException();
        }
    }

}

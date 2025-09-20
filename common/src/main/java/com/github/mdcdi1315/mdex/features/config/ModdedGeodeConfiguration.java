package com.github.mdcdi1315.mdex.features.config;

import com.github.mdcdi1315.mdex.codecs.CodecUtils;
import com.github.mdcdi1315.mdex.features.geode.GeodeBlockSettings;
import com.github.mdcdi1315.mdex.features.geode.GeodeCrackSettings;
import com.github.mdcdi1315.mdex.features.geode.GeodeLayerSettings;
import com.mojang.serialization.Codec;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;

import java.util.List;

public final class ModdedGeodeConfiguration
    extends ModdedFeatureConfiguration
{
    public GeodeBlockSettings geodeBlockSettings;
    public GeodeLayerSettings geodeLayerSettings;
    public GeodeCrackSettings geodeCrackSettings;
    public final double usePotentialPlacementsChance;
    public final double useAlternateLayer0Chance;
    public final boolean placementsRequireLayer0Alternate;
    public IntProvider outerWallDistance;
    public IntProvider distributionPoints;
    public IntProvider pointOffset;
    public final int minGenOffset;
    public final int maxGenOffset;
    public final double noiseMultiplier;
    public final int invalidBlocksThreshold;

    public static Codec<ModdedGeodeConfiguration> GetCodec()
    {
        Codec<IntProvider> ONE_TO_TWENTY = IntProvider.codec(1 , 20);
        return CodecUtils.CreateCodecDirect(
                GetBaseCodec(),
                GeodeBlockSettings.GetCodec().fieldOf("blocks").forGetter((p) -> p.geodeBlockSettings),
                GeodeLayerSettings.GetCodec().fieldOf("layers").forGetter((p) -> p.geodeLayerSettings),
                GeodeCrackSettings.GetCodec().fieldOf("crack").forGetter((p) -> p.geodeCrackSettings),
                CodecUtils.DOUBLE_PROBABILITY.optionalFieldOf("use_potential_placements_chance" , 0.35).forGetter((p) -> p.usePotentialPlacementsChance),
                CodecUtils.DOUBLE_PROBABILITY.optionalFieldOf("use_alternate_layer0_chance" , 0.0).forGetter((p) -> p.useAlternateLayer0Chance),
                Codec.BOOL.optionalFieldOf("placements_require_layer0_alternate" , true).forGetter((p) -> p.placementsRequireLayer0Alternate),
                ONE_TO_TWENTY.optionalFieldOf("outer_wall_distance" , UniformInt.of(4, 5)).forGetter((p) -> p.outerWallDistance),
                ONE_TO_TWENTY.optionalFieldOf("distribution_points" , UniformInt.of(3, 4)).forGetter((p) -> p.distributionPoints),
                IntProvider.codec(0, 10).optionalFieldOf("point_offset" , UniformInt.of(1, 2)).forGetter((p) -> p.pointOffset),
                Codec.INT.optionalFieldOf("min_gen_offset" , -16).forGetter((p) -> p.minGenOffset),
                Codec.INT.optionalFieldOf("max_gen_offset" , 16).forGetter((p) -> p.maxGenOffset),
                CodecUtils.DOUBLE_PROBABILITY.optionalFieldOf("noise_multiplier" , 0.05).forGetter((p) -> p.noiseMultiplier),
                CodecUtils.ZERO_OR_POSITIVE_INTEGER.fieldOf("invalid_blocks_threshold").forGetter((p) -> p.invalidBlocksThreshold),
                ModdedGeodeConfiguration::new
        );
    }

    public ModdedGeodeConfiguration(
            List<String> modids,
            GeodeBlockSettings geodeBlockSettings,
            GeodeLayerSettings geodeLayerSettings,
            GeodeCrackSettings geodeCrackSettings,
            double usePotentialPlacementsChance,
            double useAlternateLayer0Chance,
            boolean placementsRequireLayer0Alternate,
            IntProvider outerWallDistance,
            IntProvider distributionPoints,
            IntProvider pointOffset,
            int minGenOffset,
            int maxGenOffset,
            double noiseMultiplier,
            int invalidBlocksThreshold)
    {
        super(modids);
        this.geodeBlockSettings = geodeBlockSettings;
        this.geodeLayerSettings = geodeLayerSettings;
        this.geodeCrackSettings = geodeCrackSettings;
        this.usePotentialPlacementsChance = usePotentialPlacementsChance;
        this.useAlternateLayer0Chance = useAlternateLayer0Chance;
        this.placementsRequireLayer0Alternate = placementsRequireLayer0Alternate;
        this.outerWallDistance = outerWallDistance;
        this.distributionPoints = distributionPoints;
        this.pointOffset = pointOffset;
        this.minGenOffset = minGenOffset;
        this.maxGenOffset = maxGenOffset;
        this.noiseMultiplier = noiseMultiplier;
        this.invalidBlocksThreshold = invalidBlocksThreshold;
        Compile();
    }

    @Override
    protected void compileConfigData()
    {
        geodeBlockSettings.Compile();
        if (!geodeBlockSettings.IsCompiled())
        {
            setConfigAsInvalid();
        }
    }

    @Override
    protected void invalidateUntransformedFields() {

    }
}

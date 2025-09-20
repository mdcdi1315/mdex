package com.github.mdcdi1315.mdex.features.config;

import com.github.mdcdi1315.mdex.codecs.CodecUtils;
import com.github.mdcdi1315.mdex.util.CompilableBlockState;

import com.mojang.serialization.Codec;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.FloatProvider;

import java.util.List;

/**
 * Large Stone column feature.
 * Pretty much based on the large dripstone config, but it allows for any possible block state to be done the same.
 */
public final class LargeStoneColumnFeatureConfiguration
    extends ModdedFeatureConfiguration
{
    public final CompilableBlockState BlockState;
    public final short floorToCeilingSearchRange;
    public final IntProvider columnRadius;
    public final FloatProvider heightScale;
    public final float maxColumnRadiusToCaveHeightRatio;
    public final FloatProvider stalactiteBluntness;
    public final FloatProvider stalagmiteBluntness;
    public final FloatProvider windSpeed;
    public final byte minRadiusForWind;
    public final float minBluntnessForWind;

    public static Codec<LargeStoneColumnFeatureConfiguration> GetCodec()
    {
        Codec<FloatProvider> FP_FROMZEROPOINTONE_TOTEN = FloatProvider.codec(0.1F, 10.0F);
        return CodecUtils.CreateCodecDirect(
                GetBaseCodec(),
                CompilableBlockState.GetCodec().fieldOf("block_state").forGetter((LargeStoneColumnFeatureConfiguration p) -> p.BlockState),
                CodecUtils.ShortRange(1, 512).optionalFieldOf("floor_to_ceiling_search_range" , (short)30).forGetter((LargeStoneColumnFeatureConfiguration c) -> c.floorToCeilingSearchRange),
                IntProvider.codec(1, 60).fieldOf("column_radius").forGetter((LargeStoneColumnFeatureConfiguration c) -> c.columnRadius),
                FloatProvider.codec(0.0F, 20.0F).fieldOf("height_scale").forGetter((LargeStoneColumnFeatureConfiguration c) -> c.heightScale),
                Codec.floatRange(0.1F, 1.0F).fieldOf("max_column_radius_to_cave_height_ratio").forGetter((LargeStoneColumnFeatureConfiguration c) -> c.maxColumnRadiusToCaveHeightRatio),
                FP_FROMZEROPOINTONE_TOTEN.fieldOf("stalactite_bluntness").forGetter((LargeStoneColumnFeatureConfiguration c) -> c.stalactiteBluntness),
                FP_FROMZEROPOINTONE_TOTEN.fieldOf("stalagmite_bluntness").forGetter((LargeStoneColumnFeatureConfiguration c) -> c.stalagmiteBluntness),
                FloatProvider.codec(0.0F, 2.0F).fieldOf("wind_speed").forGetter((LargeStoneColumnFeatureConfiguration c) -> c.windSpeed),
                CodecUtils.ByteRange(0, 100).fieldOf("min_radius_for_wind").forGetter((LargeStoneColumnFeatureConfiguration c) -> c.minRadiusForWind),
                Codec.floatRange(0.0F, 5.0F).fieldOf("min_bluntness_for_wind").forGetter((LargeStoneColumnFeatureConfiguration c) -> c.minBluntnessForWind),
                LargeStoneColumnFeatureConfiguration::new
        );
    }

    public LargeStoneColumnFeatureConfiguration(List<String> modids ,
                                                CompilableBlockState bs ,
                                                short ftcsr ,
                                                IntProvider radius ,
                                                FloatProvider hs,
                                                float mcrtchr,
                                                FloatProvider stcbluntness,
                                                FloatProvider stagblutness,
                                                FloatProvider windspeed,
                                                byte minrdforwind,
                                                float minbfw)
    {
        super(modids);
        BlockState = bs;
        floorToCeilingSearchRange = ftcsr;
        columnRadius = radius;
        heightScale = hs;
        maxColumnRadiusToCaveHeightRatio = mcrtchr;
        stalactiteBluntness = stcbluntness;
        stalagmiteBluntness = stagblutness;
        windSpeed = windspeed;
        minRadiusForWind = minrdforwind;
        minBluntnessForWind = minbfw;
        Compile();
    }

    @Override
    protected void compileConfigData() {
        BlockState.Compile();
    }

    @Override
    protected void invalidateUntransformedFields() {
        // No untransformed fields exist for this feature config
        // Only the fields provided by BlockState are transformed.
    }
}

package com.github.mdcdi1315.mdex.features.config;

import com.github.mdcdi1315.basemodslib.codecs.CodecUtils;

import com.github.mdcdi1315.mdex.util.CompilableBlockState;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;

import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.FloatProvider;

/**
 * Large Stone column feature.
 * Pretty much based on the large dripstone config, but it allows for any possible block state to be done the same.
 */
public final class LargeStoneColumnFeatureConfigurationDetails
    implements IModdedFeatureConfigurationDetails
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

    public static MapCodec<LargeStoneColumnFeatureConfigurationDetails> GetCodec()
    {
        Codec<FloatProvider> FP_FROMZEROPOINTONE_TOTEN = FloatProvider.codec(0.1F, 10.0F);
        return CodecUtils.CreateMapCodecDirect(
                CompilableBlockState.GetCodec().fieldOf("block_state").forGetter((LargeStoneColumnFeatureConfigurationDetails p) -> p.BlockState),
                CodecUtils.ShortRange(1, 512).optionalFieldOf("floor_to_ceiling_search_range" , (short)30).forGetter((LargeStoneColumnFeatureConfigurationDetails c) -> c.floorToCeilingSearchRange),
                IntProvider.codec(1, 60).fieldOf("column_radius").forGetter((LargeStoneColumnFeatureConfigurationDetails c) -> c.columnRadius),
                FloatProvider.codec(0.0F, 20.0F).fieldOf("height_scale").forGetter((LargeStoneColumnFeatureConfigurationDetails c) -> c.heightScale),
                Codec.floatRange(0.1F, 1.0F).fieldOf("max_column_radius_to_cave_height_ratio").forGetter((LargeStoneColumnFeatureConfigurationDetails c) -> c.maxColumnRadiusToCaveHeightRatio),
                FP_FROMZEROPOINTONE_TOTEN.fieldOf("stalactite_bluntness").forGetter((LargeStoneColumnFeatureConfigurationDetails c) -> c.stalactiteBluntness),
                FP_FROMZEROPOINTONE_TOTEN.fieldOf("stalagmite_bluntness").forGetter((LargeStoneColumnFeatureConfigurationDetails c) -> c.stalagmiteBluntness),
                FloatProvider.codec(0.0F, 2.0F).fieldOf("wind_speed").forGetter((LargeStoneColumnFeatureConfigurationDetails c) -> c.windSpeed),
                CodecUtils.ByteRange(0, 100).fieldOf("min_radius_for_wind").forGetter((LargeStoneColumnFeatureConfigurationDetails c) -> c.minRadiusForWind),
                Codec.floatRange(0.0F, 5.0F).fieldOf("min_bluntness_for_wind").forGetter((LargeStoneColumnFeatureConfigurationDetails c) -> c.minBluntnessForWind),
                LargeStoneColumnFeatureConfigurationDetails::new
        );
    }

    public LargeStoneColumnFeatureConfigurationDetails(CompilableBlockState bs ,
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
    }

    @Override
    public void Compile() {
        BlockState.Compile();
    }
}

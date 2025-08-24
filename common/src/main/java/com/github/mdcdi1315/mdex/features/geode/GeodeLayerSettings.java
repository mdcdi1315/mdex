package com.github.mdcdi1315.mdex.features.geode;

import com.github.mdcdi1315.mdex.codecs.CodecUtils;
import com.mojang.serialization.Codec;

public class GeodeLayerSettings
{
    public static Codec<GeodeLayerSettings> GetCodec()
    {
        Codec<Double> LAYER_RANGE = Codec.doubleRange(0.01, 50.0);
        return CodecUtils.CreateCodecDirect(
                LAYER_RANGE.fieldOf("filling").orElse(1.7).forGetter((s) -> s.filling),
                LAYER_RANGE.fieldOf("inner_layer").orElse(2.2).forGetter((s) -> s.innerLayer),
                LAYER_RANGE.fieldOf("middle_layer").orElse(3.2).forGetter((s) -> s.middleLayer),
                LAYER_RANGE.fieldOf("outer_layer").orElse(4.2).forGetter((s) -> s.outerLayer),
                GeodeLayerSettings::new
        );
    }

    public final double filling;
    public final double innerLayer;
    public final double middleLayer;
    public final double outerLayer;

    public GeodeLayerSettings(double filling, double innerLayer, double middleLayer, double outerLayer) {
        this.filling = filling;
        this.innerLayer = innerLayer;
        this.middleLayer = middleLayer;
        this.outerLayer = outerLayer;
    }
}
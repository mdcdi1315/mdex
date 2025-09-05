package com.github.mdcdi1315.mdex.features.geode;

import com.mojang.serialization.Codec;
import com.github.mdcdi1315.mdex.codecs.CodecUtils;


public class GeodeCrackSettings
{
    public static Codec<GeodeCrackSettings> GetCodec()
    {
        return CodecUtils.CreateCodecDirect(
                CodecUtils.DOUBLE_PROBABILITY.optionalFieldOf("generate_crack_chance" , 1.0d).forGetter((s) -> s.generateCrackChance),
                Codec.doubleRange(0.0, 5.0).optionalFieldOf("base_crack_size" , 2.0d).forGetter((s) -> s.baseCrackSize),
                CodecUtils.ByteRange(0, 10).optionalFieldOf("crack_point_offset" , (byte)2).forGetter((s) -> s.crackPointOffset),
                GeodeCrackSettings::new
        );
    }

    public final double generateCrackChance;
    public final double baseCrackSize;
    public final byte crackPointOffset;

    public GeodeCrackSettings(double generateCrackChance, double baseCrackSize, byte crackPointOffset) {
        this.generateCrackChance = generateCrackChance;
        this.baseCrackSize = baseCrackSize;
        this.crackPointOffset = crackPointOffset;
    }
}
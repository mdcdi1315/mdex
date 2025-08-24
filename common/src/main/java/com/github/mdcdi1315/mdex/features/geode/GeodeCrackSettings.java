package com.github.mdcdi1315.mdex.features.geode;

import com.mojang.serialization.Codec;
import com.github.mdcdi1315.mdex.codecs.CodecUtils;
import com.github.mdcdi1315.mdex.features.config.ModdedGeodeConfiguration;


public class GeodeCrackSettings
{
    public static Codec<GeodeCrackSettings> GetCodec()
    {
        return CodecUtils.CreateCodecDirect(
                ModdedGeodeConfiguration.CHANCE_RANGE.fieldOf("generate_crack_chance").orElse(1.0).forGetter((s) -> s.generateCrackChance),
                Codec.doubleRange(0.0, 5.0).fieldOf("base_crack_size").orElse(2.0).forGetter((s) -> s.baseCrackSize),
                Codec.intRange(0, 10).fieldOf("crack_point_offset").orElse(2).forGetter((s) -> s.crackPointOffset),
                GeodeCrackSettings::new
        );
    }

    public final double generateCrackChance;
    public final double baseCrackSize;
    public final int crackPointOffset;

    public GeodeCrackSettings(double generateCrackChance, double baseCrackSize, int crackPointOffset) {
        this.generateCrackChance = generateCrackChance;
        this.baseCrackSize = baseCrackSize;
        this.crackPointOffset = crackPointOffset;
    }
}
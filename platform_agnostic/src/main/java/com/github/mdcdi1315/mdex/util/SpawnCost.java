package com.github.mdcdi1315.mdex.util;

import com.github.mdcdi1315.basemodslib.codecs.CodecUtils;

import com.mojang.serialization.Codec;

public record SpawnCost(double energy_budget, double charge)
{
    public static Codec<SpawnCost> GetCodec()
    {
        return CodecUtils.CreateCodecDirect(
                Codec.DOUBLE.fieldOf("energy_budget").forGetter(SpawnCost::energy_budget),
                Codec.DOUBLE.fieldOf("charge").forGetter(SpawnCost::charge),
                SpawnCost::new
        );
    }
}
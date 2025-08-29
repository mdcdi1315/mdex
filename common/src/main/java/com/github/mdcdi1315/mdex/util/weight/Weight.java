package com.github.mdcdi1315.mdex.util.weight;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;

import com.github.mdcdi1315.DotNetLayer.System.ArgumentOutOfRangeException;

public final class Weight
{
    public static final Codec<Weight> CODEC;
    public static final Weight SINGLE;
    private final int weight;

    static {
        CODEC = Codec.INT.flatXmap(Weight::FromInt , Weight::FromWeight);
        SINGLE = new Weight(0);
    }

    private static DataResult<Weight> FromInt(int wt)
    {
        if (wt < 0) {
            return DataResult.error(() -> "Weight must be more than or equal to zero.");
        } else if (wt == 0) {
            return DataResult.success(SINGLE);
        }
        return DataResult.success(new Weight(wt));
    }

    private static DataResult<Integer> FromWeight(Weight wt)
    {
        if (wt == null) {
            return DataResult.error(() -> "The specified Weight reference is null.");
        }
        return DataResult.success(wt.weight);
    }

    public static Weight Of(int wt)
    {
        if (wt < 0) {
            throw new ArgumentOutOfRangeException("wt" , "Weight must be more than or equal to zero.");
        } else if (wt == 0) {
            return SINGLE;
        }
        return new Weight(wt);
    }

    private Weight(int weight) {
        this.weight = weight;
    }

    public int getValue() {
        return weight;
    }
}

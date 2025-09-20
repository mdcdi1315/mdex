package com.github.mdcdi1315.mdex.util;

import com.github.mdcdi1315.DotNetLayer.System.ArgumentException;
import com.github.mdcdi1315.DotNetLayer.System.ArgumentNullException;

import com.github.mdcdi1315.mdex.codecs.CodecUtils;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;

import net.minecraft.util.RandomSource;

import java.util.function.Function;

public final class IntegerInclusiveRange
{
    private static Codec<IntegerInclusiveRange> codec;
    private final int min_inclusive;
    private final int max_inclusive;

    public IntegerInclusiveRange(int min_inclusive , int max_inclusive)
    {
        if (min_inclusive > max_inclusive) {
            throw new ArgumentException("min_inclusive must be less than or equal to max_inclusive");
        }
        this.min_inclusive = min_inclusive;
        this.max_inclusive = max_inclusive;
    }

    public static Codec<IntegerInclusiveRange> GetCodec()
    {
        if (codec == null)
        {
            codec = CodecUtils.CreateCodecDirect(
                    Codec.INT.fieldOf("min_inclusive").forGetter((IntegerInclusiveRange r) -> r.min_inclusive),
                    Codec.INT.fieldOf("max_inclusive").forGetter((IntegerInclusiveRange r) -> r.max_inclusive),
                    IntegerInclusiveRange::new
            );
        }
        return codec;
    }

    private static class InclusiveRangeChecker
        implements Function<IntegerInclusiveRange , DataResult<IntegerInclusiveRange>>
    {
        private final int min_inclusive_allowed;
        private final int max_inclusive_allowed;

        public InclusiveRangeChecker(int min , int max)
        {
            min_inclusive_allowed = min;
            max_inclusive_allowed = max;
        }

        @Override
        public DataResult<IntegerInclusiveRange> apply(IntegerInclusiveRange range) {
            if (range == null) {
                return DataResult.error(() -> "Cannot determine result because the object retrieved is null.");
            }
            if (range.min_inclusive < min_inclusive_allowed)
            {
                String s = String.format("Value outside of lower bound: %d. Actual value: %d" , min_inclusive_allowed , range.min_inclusive);
                return DataResult.error(() -> s);
            }
            if (range.max_inclusive > max_inclusive_allowed)
            {
                String s = String.format("Value outside of upper bound: %d. Actual value: %d" , max_inclusive_allowed , range.max_inclusive);
                return DataResult.error(() -> s);
            }
            return DataResult.success(range);
        }
    }

    public static Codec<IntegerInclusiveRange> EnforceRangeIntoRange(int min_inclusive_allowed , int max_inclusive_allowed)
    {
        var c = new InclusiveRangeChecker(min_inclusive_allowed , max_inclusive_allowed);
        return GetCodec().flatXmap(c , c);
    }

    public int GetSelectedMinInclusiveValue()
    {
        return min_inclusive;
    }

    public int GetSelectedMaxInclusiveValue()
    {
        return max_inclusive;
    }

    public int RollIntoRange(RandomSource source)
    {
        ArgumentNullException.ThrowIfNull(source , "source");
        return source.nextIntBetweenInclusive(min_inclusive , max_inclusive);
    }

    public boolean IsInRange(int value)
    {
        return !(value < min_inclusive || value > max_inclusive);
    }

    public int ClampIntoRange(int value)
    {
        if (value < min_inclusive) {
            return min_inclusive;
        } else {
            return Math.min(value, max_inclusive);
        }
    }
}

package com.github.mdcdi1315.mdex.codecs;

import com.mojang.serialization.DataResult;

import java.util.function.Function;

/**
 * Special class that defines the backend behind the {@link CodecUtils#ShortRange(int, int)} method. <br />
 * Created as a class to have better mem footprint than the lambda equivalent
 */
public final class ShortRangeChecker
    implements Function<Short , DataResult<Short>>
{
    private final short rangemininclusive;
    private final short rangemaxinclusive;

    public ShortRangeChecker(short mininclusive , short maxinclusive)
    {
        rangemininclusive = mininclusive;
        rangemaxinclusive = maxinclusive;
    }

    @Override
    public DataResult<Short> apply(Short aShort) {
        if (aShort >= rangemininclusive && aShort <= rangemaxinclusive) {
            return DataResult.success(aShort);
        }
        String s = String.format("Value %d outside of range [%d:%d]" , aShort , rangemininclusive , rangemaxinclusive);
        return DataResult.error(() -> s, aShort);
    }
}

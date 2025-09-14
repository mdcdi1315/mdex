package com.github.mdcdi1315.mdex.codecs;

import com.mojang.serialization.DataResult;

import java.util.function.Function;

/**
 * Special class that defines the backend behind the {@link CodecUtils#ByteRange(int , int)} method. <br />
 * Created as a class to have better mem footprint than the lambda equivalent
 */
public final class ByteRangeChecker
    implements Function<Byte , DataResult<Byte>>
{
    private final byte rangemininclusive;
    private final byte rangemaxinclusive;

    public ByteRangeChecker(byte mininclusive , byte maxinclusive)
    {
        rangemininclusive = mininclusive;
        rangemaxinclusive = maxinclusive;
    }

    @Override
    public DataResult<Byte> apply(Byte aByte) {
        if (aByte >= rangemininclusive && aByte <= rangemaxinclusive) {
            return DataResult.success(aByte);
        }
        String s = String.format("Value %d outside of range [%d:%d]" , aByte , rangemininclusive , rangemaxinclusive);
        return DataResult.error(() -> s, aByte);
    }
}

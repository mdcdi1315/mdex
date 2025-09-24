package com.github.mdcdi1315.mdex.codecs;

import com.mojang.serialization.DataResult;

public final class ShortRangeCodec
    extends PrimitiveCodecWithValidation<Short>
{
    private final short min_value, max_value;

    public ShortRangeCodec(int min_value , int max_value)
    {
        this.min_value = (short) min_value;
        this.max_value = (short) max_value;
    }

    @Override
    protected Short Mapper(Number number) {
        return number.shortValue();
    }

    @Override
    protected DataResult<Short> Validate(Short number) {
        if (number >= min_value && number <= max_value) {
            return DataResult.success(number);
        }
        String s = String.format("Value %d outside of range [%d..%d]" , number , min_value , max_value);
        return DataResult.error(() -> s, number);
    }
}

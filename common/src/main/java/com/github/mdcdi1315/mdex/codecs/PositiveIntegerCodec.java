package com.github.mdcdi1315.mdex.codecs;

import com.mojang.serialization.DataResult;

public final class PositiveIntegerCodec
    extends PrimitiveCodecWithValidation<Integer>
{
    @Override
    protected Integer Mapper(Number number) {
        return number.intValue();
    }

    @Override
    protected DataResult<Integer> Validate(Integer number) {
        if (number < 1) {
            String s = String.format("Integer not positive: %d" , number);
            return DataResult.error(() -> s);
        }
        return DataResult.success(number);
    }
}

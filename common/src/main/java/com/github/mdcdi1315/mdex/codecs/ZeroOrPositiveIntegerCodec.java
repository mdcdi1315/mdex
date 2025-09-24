package com.github.mdcdi1315.mdex.codecs;

import com.mojang.serialization.DataResult;

public final class ZeroOrPositiveIntegerCodec
    extends PrimitiveCodecWithValidation<Integer>
{

    @Override
    protected Integer Mapper(Number number) {
        return number.intValue();
    }

    @Override
    protected DataResult<Integer> Validate(Integer integer) {
        if (integer < 0) {
            String s = String.format("Integer not positive or zero: %d" , integer);
            return DataResult.error(() -> s , integer);
        }
        return DataResult.success(integer);
    }
}

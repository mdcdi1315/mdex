package com.github.mdcdi1315.mdex.codecs;

import com.github.mdcdi1315.mdex.util.StringSupplier;

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
        return (number < 1) ?
            DataResult.error(new StringSupplier(String.format("Integer not positive: %d" , number))) :
            DataResult.success(number);
    }
}

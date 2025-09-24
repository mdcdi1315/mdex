package com.github.mdcdi1315.mdex.codecs;

import com.mojang.serialization.DataResult;

public final class DoubleProbabilityCodec
    extends PrimitiveCodecWithValidation<Double>
{
    @Override
    protected Double Mapper(Number number) {
        return number.doubleValue();
    }

    @Override
    protected DataResult<Double> Validate(Double number)
    {
        if (number > 1d || number < 0d)
        {
            String s = String.format("Probability value out of range [0..1]: %f" , number);
            return DataResult.error(() -> s , number);
        }
        return DataResult.success(number);
    }
}

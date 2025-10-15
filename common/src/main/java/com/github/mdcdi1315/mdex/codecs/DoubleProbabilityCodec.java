package com.github.mdcdi1315.mdex.codecs;

import com.github.mdcdi1315.mdex.util.StringSupplier;
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
        return (number > 1d || number < 0d) ?
                DataResult.error(new StringSupplier(String.format("Probability value out of range [0..1]: %f" , number)), number) :
                DataResult.success(number);
    }
}

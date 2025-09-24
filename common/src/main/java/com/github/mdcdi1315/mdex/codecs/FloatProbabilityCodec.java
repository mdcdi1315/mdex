package com.github.mdcdi1315.mdex.codecs;

import com.mojang.serialization.DataResult;

public final class FloatProbabilityCodec
    extends PrimitiveCodecWithValidation<Float>
{
    @Override
    protected Float Mapper(Number number) {
        return number.floatValue();
    }

    @Override
    protected DataResult<Float> Validate(Float number)
    {
        if (number > 1f || number < 0f)
        {
            String s = String.format("Probability value out of range [0..1]: %f" , number);
            return DataResult.error(() -> s , number);
        }
        return DataResult.success(number);
    }
}

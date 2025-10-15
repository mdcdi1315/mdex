package com.github.mdcdi1315.mdex.codecs;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;

public abstract class PrimitiveCodecWithValidation<TPR extends Number> // Only valid for numeric types
    extends PrimitiveCodec<TPR>
{
    private static <T> DataResult<T> ErrorMapper(DataResult.Error<Number> pr) {
        return DataResult.error(pr.messageSupplier());
    }

    @Override
    protected <T> T Write(DynamicOps<T> ops, TPR value) {
        return ops.createNumeric(value);
    }

    @Override
    protected <T> DataResult<TPR> Read(DynamicOps<T> ops, T input) {
        DataResult<Number> n = ops.getNumberValue(input);
        var e = n.error();
        return e
                .<DataResult<TPR>>map(PrimitiveCodecWithValidation::ErrorMapper)
                .orElse(Validate(Mapper(n.result().get())));
    }

    protected abstract TPR Mapper(Number number);

    protected abstract DataResult<TPR> Validate(TPR number);
}

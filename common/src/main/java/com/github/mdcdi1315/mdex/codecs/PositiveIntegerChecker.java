package com.github.mdcdi1315.mdex.codecs;

import com.mojang.serialization.DataResult;

import java.util.function.Function;

public final class PositiveIntegerChecker
    implements Function<Integer , DataResult<Integer>>
{
    @Override
    public DataResult<Integer> apply(Integer integer) {
        if (integer < 1) {
            String s = String.format("Integer not positive: %d" , integer);
            return DataResult.error(() -> s);
        }
        return DataResult.success(integer);
    }
}

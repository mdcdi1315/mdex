package com.github.mdcdi1315.mdex.codecs;

import com.mojang.serialization.DataResult;

import java.util.List;
import java.util.function.Function;

public final class NonEmptyListChecker<T>
    implements Function<List<T> , DataResult<List<T>>>
{
    @Override
    public DataResult<List<T>> apply(List<T> ts) {
        if (ts == null) {
            return DataResult.error(() -> "Unexpected path: List was null.");
        }
        if (ts.isEmpty())  {
            return DataResult.error(() -> "List is empty, while it is required at least one element to exist within the list.");
        }
        return DataResult.success(ts);
    }
}

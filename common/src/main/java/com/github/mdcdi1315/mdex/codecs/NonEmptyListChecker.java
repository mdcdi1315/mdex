package com.github.mdcdi1315.mdex.codecs;

import com.github.mdcdi1315.mdex.util.StringSupplier;

import com.mojang.serialization.DataResult;

import java.util.List;
import java.util.function.Function;

public final class NonEmptyListChecker<T>
    implements Function<List<T> , DataResult<List<T>>>
{
    @Override
    public DataResult<List<T>> apply(List<T> ts) {
        return (ts == null) ?
                DataResult.error(new StringSupplier("Unexpected path: List was null.")) : (
                (ts.isEmpty()) ?
                        DataResult.error(new StringSupplier("List is empty, while it is required at least one element to exist within the list.")) :
                        DataResult.success(ts)
        );
    }
}

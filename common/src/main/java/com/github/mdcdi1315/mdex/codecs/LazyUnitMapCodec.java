package com.github.mdcdi1315.mdex.codecs;

import com.github.mdcdi1315.DotNetLayer.System.ArgumentNullException;
import com.github.mdcdi1315.DotNetLayer.System.Func1;

import com.mojang.serialization.*;

import java.util.stream.Stream;

public final class LazyUnitMapCodec<T>
    extends MapCodec<T>
{
    private T value;
    private Func1<T> valuegetter;

    public LazyUnitMapCodec(Func1<T> getter)
            throws ArgumentNullException
    {
        ArgumentNullException.ThrowIfNull(getter , "getter");
        valuegetter = getter;
    }

    private void EnsureLoaded()
    {
        if (value == null)
        {
            value = valuegetter.function();
            valuegetter = null;
        }
    }

    @Override
    public <T1> Stream<T1> keys(DynamicOps<T1> ops) {
        return Stream.empty();
    }

    @Override
    public <T1> DataResult<T> decode(DynamicOps<T1> ops, MapLike<T1> input) {
        EnsureLoaded();
        return DataResult.success(value);
    }

    @Override
    public <T1> RecordBuilder<T1> encode(T input, DynamicOps<T1> ops, RecordBuilder<T1> prefix) {
        return ops.mapBuilder(); // Create and return an empty map builder
    }
}

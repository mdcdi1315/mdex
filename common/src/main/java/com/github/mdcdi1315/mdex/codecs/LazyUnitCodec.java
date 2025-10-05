package com.github.mdcdi1315.mdex.codecs;

import com.github.mdcdi1315.DotNetLayer.System.Func1;
import com.github.mdcdi1315.DotNetLayer.System.ArgumentNullException;

import com.mojang.datafixers.util.Pair;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;

public final class LazyUnitCodec<T>
    implements Codec<T>
{
    private T value;
    private Func1<T> valuegetter;

    public LazyUnitCodec(Func1<T> getter)
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
    public <T1> DataResult<Pair<T, T1>> decode(DynamicOps<T1> ops, T1 input) {
        EnsureLoaded();
        return DataResult.success(Pair.of(value , input));
    }

    @Override
    public <T1> DataResult<T1> encode(T input, DynamicOps<T1> ops, T1 prefix) {
        return ops.mapBuilder().build(prefix); // Create an empty map instead.
    }
}

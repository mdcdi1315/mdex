package com.github.mdcdi1315.mdex.block.blockstateproviders;

import com.github.mdcdi1315.DotNetLayer.System.InvalidOperationException;
import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.NotNull;

import com.mojang.serialization.MapCodec;

public abstract class AbstractBlockStateProviderType<T extends AbstractBlockStateProvider>
{
    private final MapCodec<T> codec;

    public AbstractBlockStateProviderType()
    {
        if ((codec = GetCodecInstance()) == null)
        {
            throw new InvalidOperationException("Cannot create the block state provider type because it's serialization codec returned null.");
        }
    }

    protected abstract MapCodec<T> GetCodecInstance();

    @NotNull
    public final MapCodec<T> Codec() {
        return codec;
    }
}

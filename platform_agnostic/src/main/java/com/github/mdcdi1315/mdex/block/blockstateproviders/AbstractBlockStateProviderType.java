package com.github.mdcdi1315.mdex.block.blockstateproviders;

import com.github.mdcdi1315.DotNetLayer.System.InvalidOperationException;
import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.NotNull;

import com.mojang.serialization.Codec;

public abstract class AbstractBlockStateProviderType<T extends AbstractBlockStateProvider>
{
    private final Codec<T> codec;

    public AbstractBlockStateProviderType()
    {
        if ((codec = GetCodecInstance()) == null)
        {
            throw new InvalidOperationException("Cannot create the block state provider type because it's serialization codec returned null.");
        }
    }

    protected abstract Codec<T> GetCodecInstance();

    @NotNull
    public final Codec<T> Codec() {
        return codec;
    }
}

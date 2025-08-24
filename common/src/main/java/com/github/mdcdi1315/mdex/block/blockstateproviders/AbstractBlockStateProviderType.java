package com.github.mdcdi1315.mdex.block.blockstateproviders;

import com.mojang.serialization.Codec;

public abstract class AbstractBlockStateProviderType<T extends AbstractBlockStateProvider>
{
    public abstract Codec<T> Codec();
}

package com.github.mdcdi1315.mdex.block.blockstateproviders;

import com.mojang.serialization.MapCodec;

public abstract class AbstractBlockStateProviderType<T extends AbstractBlockStateProvider>
{
    public abstract MapCodec<T> Codec();
}

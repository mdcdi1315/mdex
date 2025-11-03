package com.github.mdcdi1315.mdex.util.weight;

import com.github.mdcdi1315.DotNetLayer.System.ArgumentNullException;

import com.mojang.serialization.MapCodec;

import java.util.List;

public final class SimpleWeightedEntryListCodec<T>
    extends AbstractWeightedEntryListCodec<SimpleWeightedEntryList_Entry<T> , SimpleWeightedEntryList<T>>
{
    public SimpleWeightedEntryListCodec(MapCodec<T> elementcodec)
            throws ArgumentNullException
    {
        super(SimpleWeightedEntryList_Entry.CreateCodec(elementcodec));
    }

    @Override
    protected SimpleWeightedEntryList<T> Transform(List<SimpleWeightedEntryList_Entry<T>> list) {
        return new SimpleWeightedEntryList<>(list);
    }
}

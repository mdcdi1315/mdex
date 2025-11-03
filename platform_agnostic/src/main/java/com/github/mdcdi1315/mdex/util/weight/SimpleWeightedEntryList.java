package com.github.mdcdi1315.mdex.util.weight;

import com.github.mdcdi1315.DotNetLayer.System.ArgumentNullException;
import com.github.mdcdi1315.DotNetLayer.System.InvalidOperationException;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;

import java.util.List;

public final class SimpleWeightedEntryList<TE>
    extends WeightedEntryList<SimpleWeightedEntryList_Entry<TE>>
{
    public static <TE> Codec<SimpleWeightedEntryList<TE>> CreateSimpleWeightedEntryList(MapCodec<TE> elementcodec)
    {
        return new SimpleWeightedEntryListCodec<>(elementcodec);
    }

    public SimpleWeightedEntryList(int capacity) {
        super(capacity);
    }

    public SimpleWeightedEntryList(List<SimpleWeightedEntryList_Entry<TE>> elements) {
        super(elements);
    }

    public void Add(TE element , Weight weight)
            throws ArgumentNullException , InvalidOperationException
    {
        super.Add(SimpleWeightedEntryList_Entry.Create(element , weight));
    }
}

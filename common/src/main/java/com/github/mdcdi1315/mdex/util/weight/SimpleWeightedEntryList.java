package com.github.mdcdi1315.mdex.util.weight;

import com.github.mdcdi1315.DotNetLayer.System.ArgumentNullException;
import com.github.mdcdi1315.DotNetLayer.System.InvalidOperationException;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.DataResult;

import java.util.List;

public final class SimpleWeightedEntryList<TE>
    extends WeightedEntryList<SimpleWeightedEntryList_Entry<TE>>
{
    public static <TE> Codec<SimpleWeightedEntryList<TE>> CreateSimpleWeightedEntryList(MapCodec<TE> elementcodec)
    {
        return SimpleWeightedEntryList_Entry.CreateCodec(elementcodec).listOf().flatXmap(SimpleWeightedEntryList::CreateWeightedEntryList , SimpleWeightedEntryList::DecomposeWeightedEntryList);
    }

    private static <TE> DataResult<SimpleWeightedEntryList<TE>> CreateWeightedEntryList(List<SimpleWeightedEntryList_Entry<TE>> elements)
    {
        if (elements == null) {
            return DataResult.error(() -> "The specified list was a null reference.");
        }
        return DataResult.success(new SimpleWeightedEntryList<>(elements));
    }

    private static <TE> DataResult<List<SimpleWeightedEntryList_Entry<TE>>> DecomposeWeightedEntryList(SimpleWeightedEntryList<TE> entlist)
    {
        if (entlist == null) {
            return DataResult.error(() -> "The specified weighted entry list was null.");
        }
        return DataResult.success(entlist.GetUnderlying());
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

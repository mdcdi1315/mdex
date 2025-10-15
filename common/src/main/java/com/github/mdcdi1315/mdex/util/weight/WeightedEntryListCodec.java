package com.github.mdcdi1315.mdex.util.weight;

import com.github.mdcdi1315.DotNetLayer.System.ArgumentNullException;
import com.mojang.serialization.Codec;

import java.util.List;

public final class WeightedEntryListCodec<T extends IWeightedEntry>
    extends AbstractWeightedEntryListCodec<T , WeightedEntryList<T>>
{
    public WeightedEntryListCodec(Codec<T> elementcodec)
            throws ArgumentNullException
    {
        super(elementcodec);
    }

    @Override
    protected WeightedEntryList<T> Transform(List<T> list) {
        return new WeightedEntryList<>(list);
    }
}

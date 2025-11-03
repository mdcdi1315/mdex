package com.github.mdcdi1315.mdex.util.weight;

import com.github.mdcdi1315.DotNetLayer.System.ArgumentNullException;

import com.github.mdcdi1315.basemodslib.codecs.AbstractStrictListCodec;

import com.mojang.serialization.Codec;

/**
 * Defines the base codec for {@link WeightedEntryList} classes and their derivants. <br />
 * In fact, it is a {@link AbstractStrictListCodec} properly wrapped to {@link WeightedEntryList} derivants.
 * @param <T> The weighted entry type to de/encode.
 * @param <TList> The weighted entry list to de/encode.
 * @since 1.7.0
 */
public abstract class AbstractWeightedEntryListCodec<T extends IWeightedEntry , TList extends WeightedEntryList<T>>
    extends AbstractStrictListCodec<T , TList>
{
    public AbstractWeightedEntryListCodec(Codec<T> elementcodec)
            throws ArgumentNullException
    {
        super(elementcodec);
    }
}

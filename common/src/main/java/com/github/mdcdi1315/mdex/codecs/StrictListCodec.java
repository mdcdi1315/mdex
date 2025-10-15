package com.github.mdcdi1315.mdex.codecs;

import com.github.mdcdi1315.DotNetLayer.System.ArgumentNullException;

import com.google.common.collect.ImmutableList;

import com.mojang.serialization.Codec;

import java.util.List;

/**
 * Provides a default implementation of the {@link AbstractStrictListCodec} class. <br />
 * The list is de/encoded into {@link ImmutableList} instances.
 * @param <TElement>
 * @since 1.7.0
 */
public class StrictListCodec<TElement>
    extends AbstractStrictListCodec<TElement , List<TElement>>
{
    public StrictListCodec(Codec<TElement> elementcodec)
            throws ArgumentNullException
    {
        super(elementcodec);
    }

    @Override
    protected List<TElement> Transform(List<TElement> list) {
        return ImmutableList.copyOf(list);
    }
}

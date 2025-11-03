package com.github.mdcdi1315.mdex.util.weight;

import com.github.mdcdi1315.DotNetLayer.System.ArgumentNullException;

import com.github.mdcdi1315.basemodslib.codecs.CodecUtils;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;

public final class SimpleWeightedEntryList_Entry<T>
    implements IWeightedEntry
{
    private final T reference;
    private final Weight weight;

    public static <T> SimpleWeightedEntryList_Entry<T> Create(T reference , Weight weight)
            throws ArgumentNullException
    {
        ArgumentNullException.ThrowIfNull(reference , "reference");
        ArgumentNullException.ThrowIfNull(weight , "weight");
        return new SimpleWeightedEntryList_Entry<>(reference , weight);
    }

    public static <T> Codec<SimpleWeightedEntryList_Entry<T>> CreateCodec(MapCodec<T> underlyingelementcodec)
            throws ArgumentNullException
    {
        ArgumentNullException.ThrowIfNull(underlyingelementcodec , "underlyingelementcodec");
        return CodecUtils.CreateCodecDirect(
                underlyingelementcodec.forGetter((SimpleWeightedEntryList_Entry<T> d) -> d.reference),
                Weight.CODEC.fieldOf("weight").forGetter((SimpleWeightedEntryList_Entry<T> d) -> d.weight),
                SimpleWeightedEntryList_Entry::new
        );
    }

    private SimpleWeightedEntryList_Entry(T reference , Weight weight)
    {
        this.reference = reference;
        this.weight = weight;
    }

    public T getValue() {
        return reference;
    }

    @Override
    public Weight getWeight() {
        return weight;
    }

    public int getWeightValue() { return weight.getValue(); }
}

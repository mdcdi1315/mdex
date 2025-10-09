package com.github.mdcdi1315.mdex.codecs;

import com.github.mdcdi1315.DotNetLayer.System.ArgumentNullException;

import com.mojang.datafixers.util.Pair;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;

import java.util.List;

/**
 * Defines a codec that does decode either using the element codec,
 * or into a list codec if the data to decode denote to be such. <br />
 * If a single element is described, the result is returned as a list containing the decoded element. <br />
 * Encoding is always done directly into a list to ensure further type safety.
 * @param <TElement> The type of the element to decode.
 * @since 1.6.0
 */
public final class SingleElementOrListCodec<TElement>
    implements Codec<List<TElement>>
{
    private final Codec<TElement> elementcodec;
    private Codec<List<TElement>> listcodec;

    /**
     * Creates a new instance of this codec class, by using the specified codec to decode the list's entries.
     * @param codec The codec to use for decoding the list's entries.
     * @throws ArgumentNullException {@code codec} is {@code null}.
     */
    public SingleElementOrListCodec(Codec<TElement> codec)
            throws ArgumentNullException
    {
        ArgumentNullException.ThrowIfNull(codec , "codec");
        elementcodec = codec;
        listcodec = null;
    }

    // The list codec is created lazily the first time is accessed.
    private Codec<List<TElement>> GetListCodec()
    {
        if (listcodec == null) {
            listcodec = elementcodec.listOf();
        }
        return listcodec;
    }

    @Override
    public <T> DataResult<Pair<List<TElement>, T>> decode(DynamicOps<T> ops, T input)
    {
        DataResult<Pair<TElement , T>> single = elementcodec.decode(ops , input);

        var e = single.error();

        if (e.isPresent()) {
            // Try with the list codec instead.
            DataResult<Pair<List<TElement> , T>> list = GetListCodec().decode(ops , input);

            var e2 = list.error();

            if (e2.isPresent()) {
                String s = String.format("Deserialization failed. \nSingle element codec failed with: %s \nList codec failed with: %s \n" , e.get().message() , e2.get().message());
                return DataResult.error(() -> s);
            } else {
                // Decode successful, return the list.
                return list;
            }
        } else {
            // Single element decode successful, return the element wrapped in an immutable list.
            // It is OK to call get() on the data result, since either an error or a valid result will only exist, not both.

            var pair = single.result().get();

            return DataResult.success(Pair.of(List.of(pair.getFirst()) , pair.getSecond()));
        }
    }

    @Override
    public <T> DataResult<T> encode(List<TElement> input, DynamicOps<T> ops, T prefix) {
        // Since we are reading from a list, just wrap around the result around the list codec.
        return GetListCodec().encode(input, ops , prefix);
    }
}

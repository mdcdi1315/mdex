package com.github.mdcdi1315.mdex.util.weight;

import com.github.mdcdi1315.DotNetLayer.System.ArgumentNullException;
import com.github.mdcdi1315.DotNetLayer.System.InvalidOperationException;
import com.github.mdcdi1315.DotNetLayer.System.ArgumentOutOfRangeException;
import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.NotNull;
import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.MaybeNull;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.util.RandomSource;

import java.util.*;
import java.util.function.Consumer;

public class WeightedEntryList<T extends IWeightedEntry>
    implements Iterable<T>
{
    private final List<T> list;
    private int totalweight;
    private final boolean notarraylist;

    public static <T extends IWeightedEntry> Codec<WeightedEntryList<T>> CreateWeightedEntryListCodec(Codec<T> elementcodec)
    {
        ArgumentNullException.ThrowIfNull(elementcodec , "elementcodec");
        return elementcodec.listOf().flatXmap(WeightedEntryList::CreateWeightedEntryList , WeightedEntryList::DecomposeWeightedEntryList);
    }

    private static <T extends IWeightedEntry> DataResult<WeightedEntryList<T>> CreateWeightedEntryList(List<T> elements)
    {
        if (elements == null) {
            return DataResult.error(() -> "The specified list was a null reference.");
        }
        return DataResult.success(new WeightedEntryList<>(elements));
    }

    private static <T extends IWeightedEntry> DataResult<List<T>> DecomposeWeightedEntryList(WeightedEntryList<T> entlist)
    {
        if (entlist == null) {
            return DataResult.error(() -> "The specified weighted entry list was null.");
        }
        return DataResult.success(entlist.list);
    }

    public WeightedEntryList(int capacity)
    {
        if (capacity < 0) {
            throw new ArgumentOutOfRangeException("capacity" , "Initial internal list capacity size must be more than -1.");
        }
        notarraylist = false;
        list = new ArrayList<>(capacity);
    }

    public WeightedEntryList(List<T> list)
    {
        ArgumentNullException.ThrowIfNull(list);
        totalweight = WeightUtils.GetTotalWeight(this.list = list);
        notarraylist = !(this.list instanceof ArrayList<T>);
    }

    private void RecomputeTotalWeight() {
        totalweight = WeightUtils.GetTotalWeight(this.list);
    }

    @NotNull
    public List<T> GetUnderlying() {
        return list;
    }

    public void Add(T element)
            throws ArgumentNullException , InvalidOperationException
    {
        ArgumentNullException.ThrowIfNull(element , "element");
        if (notarraylist) {
            throw new InvalidOperationException("This operation is not allowed on immutable list objects");
        }
        list.add(element);
        RecomputeTotalWeight();
    }

    public void AddAll(Iterable<T> elements)
            throws ArgumentNullException , InvalidOperationException
    {
        ArgumentNullException.ThrowIfNull(elements , "elements");
        if (notarraylist) {
            throw new InvalidOperationException("This operation is not allowed on immutable list objects");
        }
        for (T e : elements) {
            list.add(e);
        }
        RecomputeTotalWeight();
    }

    public void AddAll(Collection<T> elements)
            throws ArgumentNullException , InvalidOperationException
    {
        ArgumentNullException.ThrowIfNull(elements , "elements");
        if (notarraylist) {
            throw new InvalidOperationException("This operation is not allowed on immutable list objects");
        }
        list.addAll(elements);
        RecomputeTotalWeight();
    }

    public boolean IsEmpty() {
        return list.isEmpty();
    }

    @MaybeNull
    public Optional<T> GetRandom(RandomSource rs)
    {
        if (totalweight == 0) {
            return Optional.empty();
        }
        return WeightUtils.GetWeightedItem(list , rs.nextInt(totalweight));
    }

    public RandomWeightedEntriesIterable<T> GetRandomWeightedEntriesIterable(RandomSource rs, int rolls) {
        return new RandomWeightedEntriesIterable<>(list , rs , totalweight , rolls);
    }

    @NotNull
    @Override
    @SuppressWarnings("all")
    public Iterator<T> iterator() {
        return list.iterator();
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        list.forEach(action);
    }

    @Override
    public Spliterator<T> spliterator() {
        return list.spliterator();
    }
}

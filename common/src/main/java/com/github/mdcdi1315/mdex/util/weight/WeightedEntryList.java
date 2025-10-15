package com.github.mdcdi1315.mdex.util.weight;

import com.github.mdcdi1315.DotNetLayer.System.ArgumentNullException;
import com.github.mdcdi1315.DotNetLayer.System.ArgumentOutOfRangeException;
import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.NotNull;
import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.MaybeNull;
import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.DisallowNull;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;

import net.minecraft.util.RandomSource;

import java.util.*;
import java.util.function.Consumer;

public class WeightedEntryList<T extends IWeightedEntry>
    implements List<T>
{
    private final List<T> list;
    private int totalweight;

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
        list = new ArrayList<>(capacity);
    }

    public WeightedEntryList(List<T> list)
    {
        ArgumentNullException.ThrowIfNull(list);
        totalweight = WeightUtils.GetTotalWeight(this.list = list);
    }

    private void RecomputeTotalWeight() {
        totalweight = WeightUtils.GetTotalWeight(this.list);
    }

    public void Add(T element)
            throws ArgumentNullException
    {
        ArgumentNullException.ThrowIfNull(element , "element");
        list.add(element);
        totalweight += element.getWeight().getValue();
    }

    public void AddAll(Iterable<T> elements)
            throws ArgumentNullException
    {
        ArgumentNullException.ThrowIfNull(elements , "elements");
        for (T e : elements) {
            list.add(e);
            totalweight += e.getWeight().getValue();
        }
    }

    public void AddAll(Collection<T> elements)
            throws ArgumentNullException
    {
        ArgumentNullException.ThrowIfNull(elements , "elements");
        for (T i : elements) {
            list.add(i);
            totalweight += i.getWeight().getValue();
        }
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

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return list.contains(o);
    }

    @NotNull
    @Override
    @SuppressWarnings("all")
    public Iterator<T> iterator() {
        return list.iterator();
    }

    @NotNull
    @Override
    public Object[] toArray() {
        return list.toArray();
    }

    @NotNull
    @Override
    public <T1> T1[] toArray(@DisallowNull T1[] a) {
        return list.toArray(a);
    }

    @Override
    public boolean add(T t) {
        list.add(t);
        totalweight += t.getWeight().getValue();
        return true; // By Collection.add reference, this is always true.
    }

    @Override
    public boolean remove(Object o) {
        return list.remove(o);
    }

    @Override
    public boolean containsAll(@DisallowNull Collection<?> c) {
        return list.containsAll(c);
    }

    @Override
    public boolean addAll(@DisallowNull Collection<? extends T> c) {
        for (var i : c) {
            list.add(i);
            totalweight += i.getWeight().getValue();
        }
        return true;
    }

    @Override
    public boolean addAll(int index, @DisallowNull Collection<? extends T> c) {
        boolean b = list.addAll(index, c);
        if (b) {
            for (var i : c) {
                totalweight += i.getWeight().getValue();
            }
        }
        return b;
    }

    @Override
    public boolean removeAll(@DisallowNull Collection<?> c) {
        boolean b = list.removeAll(c);
        if (b) {
            RecomputeTotalWeight();
        }
        return b;
    }

    @Override
    public boolean retainAll(@DisallowNull Collection<?> c) {
        boolean b = list.retainAll(c);
        if (b) {
            RecomputeTotalWeight();
        }
        return b;
    }

    @Override
    public void clear() {
        list.clear();
        totalweight = 0;
    }

    @Override
    public T get(int index) {
        return list.get(index);
    }

    @Override
    public T set(int index, T element) {
        T e = list.set(index , element);
        if (e != null) {
            totalweight -= e.getWeight().getValue();
        }
        totalweight += element.getWeight().getValue();
        return e;
    }

    @Override
    public void add(int index, T element) {
        list.add(index , element);
        RecomputeTotalWeight();
    }

    @Override
    public T remove(int index) {
        T r = list.remove(index);
        // Will always succeed.
        totalweight -= r.getWeight().getValue();
        return r;
    }

    @Override
    public int indexOf(Object o) {
        return list.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return list.lastIndexOf(o);
    }

    @NotNull
    @Override
    public ListIterator<T> listIterator() {
        return list.listIterator();
    }

    @NotNull
    @Override
    public ListIterator<T> listIterator(int index) {
        return list.listIterator(index);
    }

    @NotNull
    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return list.subList(fromIndex , toIndex);
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

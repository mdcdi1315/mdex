package com.github.mdcdi1315.mdex.util.weight;

import com.github.mdcdi1315.DotNetLayer.System.ArgumentException;
import com.github.mdcdi1315.DotNetLayer.System.ArgumentNullException;
import com.github.mdcdi1315.DotNetLayer.System.ArgumentOutOfRangeException;

import net.minecraft.util.RandomSource;
import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.Iterator;

public final class RandomWeightedEntriesIterable<T extends IWeightedEntry>
    implements Iterable<T>
{
    private final ImmutableList<T> list;
    private final int totalweight , rolls;
    private final RandomSource rs;

    public RandomWeightedEntriesIterable(List<T> elements , RandomSource source , int totalweight , int nrolls)
            throws ArgumentException
    {
        ArgumentNullException.ThrowIfNull(elements , "elements");
        ArgumentNullException.ThrowIfNull(source , "source");
        if (elements.isEmpty()) {
            throw new ArgumentException("List cannot be empty." , "elements");
        }
        if (totalweight < 0) {
            throw new ArgumentOutOfRangeException("totalweight" , "Total weight cannot be less than zero.");
        }
        if (nrolls < 0) {
            throw new ArgumentOutOfRangeException("nrolls" , "The number of rolls to perform cannot be less than zero.");
        }
        list = ImmutableList.copyOf(elements);
        this.totalweight = totalweight;
        this.rolls = nrolls;
        rs = source;
    }

    @Override
    public Iterator<T> iterator() {
        return new RandomWeightedEntriesIterator<>(list, rs , totalweight , rolls);
    }

    public Iterator<T> IteratorWithRolls(int newrolls)
            throws ArgumentOutOfRangeException
    {
        if (newrolls < 0) {
            throw new ArgumentOutOfRangeException("newrolls" , "The number of rolls to perform cannot be less than zero.");
        }
        return new RandomWeightedEntriesIterator<>(list, rs , totalweight , newrolls);
    }

    public int GetCount() {
        return list.size();
    }

    public int GetRolls() {
        return rolls;
    }
}

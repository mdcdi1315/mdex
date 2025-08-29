package com.github.mdcdi1315.mdex.util.weight;

import com.google.common.collect.ImmutableList;
import net.minecraft.util.RandomSource;

import java.util.Iterator;
import java.util.Optional;

public final class RandomWeightedEntriesIterator<T extends IWeightedEntry>
    implements Iterator<T>
{
    private final ImmutableList<T> list;
    private final int totalweight , rolls;
    private final RandomSource rs;
    private T computed;
    private int rollsdone;

    RandomWeightedEntriesIterator(ImmutableList<T> elements , RandomSource source , int totalweight , int nrolls)
    {
        list = elements;
        this.totalweight = totalweight;
        this.rolls = nrolls;
        rs = source;
        rollsdone = -1;
    }

    @Override
    public boolean hasNext()
    {
        Optional<T> ret = Optional.empty();
        while (ret.isEmpty() && ++rollsdone < rolls)
        {
            ret = WeightUtils.GetWeightedItem(list , rs.nextInt(totalweight));
        }
        if (ret.isPresent()) {
            computed = ret.get();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public T next() {
        return computed;
    }
}

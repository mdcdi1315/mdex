package com.github.mdcdi1315.mdex.util;

import com.github.mdcdi1315.DotNetLayer.System.ArgumentNullException;

import net.minecraft.tags.TagKey;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import com.mojang.datafixers.util.Either;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

public final class ConcatenatingHolderSet<T>
    extends HolderSet.ListBacked<T>
{
    private ArrayList<Holder<T>> listinternal;

    public ConcatenatingHolderSet()
    {
        listinternal = new ArrayList<>();
    }

    public ConcatenatingHolderSet(int capacity)
    {
        listinternal = new ArrayList<>(capacity);
    }

    public ConcatenatingHolderSet(List<Holder<T>> elements)
    {
        if (elements == null) {
            listinternal = new ArrayList<>();
        } else {
            listinternal = new ArrayList<>(elements);
        }
    }

    public ConcatenatingHolderSet(Iterable<Holder<T>> elements)
    {
        if (elements == null) {
            listinternal = new ArrayList<>();
        } else if (elements instanceof HolderSet<T> hs) {
            listinternal = new ArrayList<>(hs.size());
            for (var i : hs)
            {
                listinternal.add(i);
            }
        } else {
            listinternal = new ArrayList<>();
            for (var i : elements)
            {
                listinternal.add(i);
            }
        }
    }

    public ConcatenatingHolderSet(HolderSet<T> other)
    {
        if (other == null || other.size() == 0) {
            listinternal = new ArrayList<>();
        } else {
            listinternal = new ArrayList<>(other.size());
            for (var i : other)
            {
                listinternal.add(i);
            }
        }
    }

    public void Add(List<Holder<T>> list)
    {
        ArgumentNullException.ThrowIfNull(list , "list");
        listinternal.ensureCapacity(listinternal.size() + list.size());
        listinternal.addAll(list);
    }

    public void Add(HolderSet<T> setother)
    {
        ArgumentNullException.ThrowIfNull(setother , "setother");
        listinternal.ensureCapacity(listinternal.size() + setother.size());
        for (var i : setother)
        {
            listinternal.add(i);
        }
    }

    public void Optimize()
    {
        // Remove all the unbounded values from the list
        // Search will run from the beginning once such a value is found
        for (int I = 0; I < listinternal.size(); I++)
        {
            if (listinternal.get(I).isBound() == false)
            {
                listinternal.remove(I);
                I = -1;
            }
        }
        // Finally, optimize the list to only fit the elements that only do actually matter.
        listinternal.trimToSize();
    }

    @Override
    protected List<Holder<T>> contents() {
        return listinternal;
    }

    @Override
    public boolean isBound() {
        return true;
    }

    @Override
    public Either<TagKey<T>, List<Holder<T>>> unwrap() {
        return Either.right(listinternal);
    }

    @Override
    public boolean contains(Holder<T> holder) {
        return listinternal.contains(holder);
    }

    @Override
    public Optional<TagKey<T>> unwrapKey() {
        return Optional.empty();
    }
}

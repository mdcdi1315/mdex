package com.github.mdcdi1315.mdex.util.weight;

import com.github.mdcdi1315.DotNetLayer.System.ArgumentException;

import net.minecraft.Util;
import net.minecraft.util.RandomSource;

import java.util.List;
import java.util.Optional;

public final class WeightUtils
{
    private WeightUtils() {}

    public static <T extends IWeightedEntry> int GetTotalWeight(List<T> elements)
            throws ArgumentException
    {
        long i = 0L;

        for (T t : elements) {
            i += t.getWeight().getValue();
        }

        if (i > 2147483647L) {
            throw new ArgumentException("Sum of weights must be <= 2147483647");
        } else {
            return (int)i;
        }
    }

    public static <T extends IWeightedEntry> Optional<T> GetRandomItem(RandomSource random, List<T> elements, int totalWeight)
            throws ArgumentException
    {
        if (totalWeight < 0) {
            throw Util.pauseInIde(new ArgumentException("Negative total weight in GetRandomItem"));
        } else if (totalWeight == 0) {
            return Optional.empty();
        } else {
            int i = random.nextInt(totalWeight);
            return GetWeightedItem(elements, i);
        }
    }

    public static <T extends IWeightedEntry> Optional<T> GetRandomItem(RandomSource random, List<T> elements)
    {
        return GetRandomItem(random , elements , GetTotalWeight(elements));
    }

    public static <T extends IWeightedEntry> Optional<T> GetWeightedItem(List<T> elements, int index)
    {
        for(T t : elements) {
            index -= t.getWeight().getValue();
            if (index < 0) {
                return Optional.of(t);
            }
        }

        return Optional.empty();
    }

}

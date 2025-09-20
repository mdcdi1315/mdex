package com.github.mdcdi1315.mdex.util;

import com.github.mdcdi1315.DotNetLayer.System.ArgumentNullException;
import com.github.mdcdi1315.DotNetLayer.System.Collections.Generic.KeyValuePair;
import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.MaybeNull;
import com.github.mdcdi1315.DotNetLayer.System.Runtime.CompilerServices.Extension;
import com.github.mdcdi1315.DotNetLayer.System.Collections.Generic.IEqualityComparer;

import net.minecraft.util.Mth;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;

import java.util.List;

/**
 * I like better this term here than 'Utilities'.
 */
@Extension
@SuppressWarnings("unused")
public final class Extensions
{
    private Extensions() {}

    public static float PI = (float) Math.PI;
    public static float TWO_PI = PI * 2;

    @Extension
    public static Direction GetRandomDirectionExcludingUpDown(RandomSource rs)
    {
        Direction ret;
        var values = Direction.values();
        do {
            ret = values[rs.nextIntBetweenInclusive(0 , values.length-1)];
        } while (ret == Direction.UP || ret == Direction.DOWN);
        return ret;
    }

    @Extension
    public static KeyValuePair<Direction , Direction> GetRandomDirectionPairNonUpDown(RandomSource rs)
    {
        return SelectRandomFromListUnsafe(List.of(
                new KeyValuePair<>(Direction.EAST , Direction.NORTH),
                new KeyValuePair<>(Direction.WEST , Direction.NORTH),
                new KeyValuePair<>(Direction.EAST , Direction.SOUTH),
                new KeyValuePair<>(Direction.WEST , Direction.SOUTH)
        ) , rs);
    }

    public static float Sin(float v)
    {
        return Mth.sin(v); // Currently forwards to Minecraft's math class, we need to find a better alternative for this
    }

    public static double Sin(double v)
    {
        return Math.sin(v);
    }

    public static float Cos(float v)
    {
        return Mth.cos(v); // Currently forwards to Minecraft's math class, we need to find a better alternative for this
    }

    public static double Cos(double v)
    {
        return Math.cos(v);
    }

    public static double InvertedSquareRoot(double d)
    {
        return 1.0d / Math.sqrt(d);
    }

    public static float InvertedSquareRoot(float d)
    {
        return 1.0f / (float) Math.sqrt(d);
    }

    public static int Ceiling(float value) {
        // Borrowed from Minecraft's code, but this is roughly in all cases.
        int i = (int)value;
        return value > (float)i ? i + 1 : i;
    }

    public static int Ceiling(double value) {
        // Borrowed from Minecraft's code, but this is roughly in all cases.
        int i = (int)value;
        return value > (double)i ? i + 1 : i;
    }

    public static float Lerp(float delta, float start, float end)
    {
        // Borrowed from Minecraft's code, but this is roughly in all cases.
        return start + delta * (end - start);
    }

    public static double Lerp(double delta, double start, double end)
    {
        // Borrowed from Minecraft's code, but this is roughly in all cases.
        return start + delta * (end - start);
    }

    public static int Floor(float value) {
        // Borrowed from Minecraft's code, but this is roughly in all cases.
        int i = (int)value;
        return value < (float)i ? i - 1 : i;
    }

    public static int Floor(double value) {
        // Borrowed from Minecraft's code, but this is roughly in all cases.
        int i = (int)value;
        return value < (double)i ? i - 1 : i;
    }

    public static float Square(float input) {
        return input * input;
    }

    public static double Square(double input) {
        return input * input;
    }

    public static int Square(int input) {
        return input * input;
    }

    public static float SquareRoot(float value) {
        return (float)Math.sqrt(value);
    }

    /**
     * Gets a random item from the specified list, and returns that item.
     * @param elements The list of items to get a random item from.
     * @param rs The random source to use for getting the random item.
     * @return The random item.
     * @param <T> The type of the items to select from. An item of such type is returned.
     * @throws ArgumentNullException {@code elements} or {@code rs} were {@code null}.
     */
    public static <T> T SelectRandomFromList(List<T> elements, RandomSource rs)
            throws ArgumentNullException
    {
        ArgumentNullException.ThrowIfNull(elements , "elements");
        ArgumentNullException.ThrowIfNull(rs , "rs");
        return SelectRandomFromListUnsafe(elements , rs);
    }

    /**
     * Gets a random item from the specified list, and returns that item.
     * Additionally, it ensures that the specified item is not selected in any way.
     * @param list The list of items to get a random item from.
     * @param item_to_exclude The item instance to exclude from the possible outcomes.
     * @param source The random source to use for getting the random item.
     * @return The random item, ensuring that is not the instance specified in {@code item_to_exclude}.
     * @param <T> The type of the items to select from. An item of such type is returned.
     */
    public static <T> T SelectRandomFromListWithExclusion(List<T> list , @MaybeNull T item_to_exclude , RandomSource source)
        throws ArgumentNullException
    {
        ArgumentNullException.ThrowIfNull(list , "list");
        ArgumentNullException.ThrowIfNull(source , "random");
        return SelectRandomFromListWithExclusionUnsafe(list , item_to_exclude , source);
    }

    /**
     * Gets a random item from the specified list, and returns that item.
     * @param elements The list of items to get a random item from.
     * @param rs The random source to use for getting the random item.
     * @return The random item.
     * @param <T> The type of the items to select from. An item of such type is returned.
     */
    public static <T> T SelectRandomFromListUnsafe(List<T> elements, RandomSource rs)
    {
        return elements.get(rs.nextIntBetweenInclusive(0 , elements.size()-1));
    }

    /**
     * Gets a random item from the specified list, and returns that item.
     * Additionally, it ensures that the specified item is not selected in any way.
     * @param list The list of items to get a random item from.
     * @param item_to_exclude The item instance to exclude from the possible outcomes.
     * @param source The random source to use for getting the random item.
     * @param comparer The equality comparer to use for testing the objects for equality.
     * @return The random item, ensuring that is not the instance specified in {@code item_to_exclude}.
     * @param <T> The type of the items to select from. An item of such type is returned.
     */
    public static <T> T SelectRandomFromListWithExclusionUnsafe(List<T> list , @MaybeNull T item_to_exclude , IEqualityComparer<T> comparer, RandomSource source)
    {
        T item;
        int size = list.size() - 1;
        if (size == 0) {
            return list.get(0);
        }
        do {
            item = list.get(source.nextIntBetweenInclusive(0, size));
        } while (comparer.Equals(item_to_exclude , item));
        return item;
    }

    /**
     * Gets a random item from the specified list, and returns that item.
     * Additionally, it ensures that the specified item is not selected in any way.
     * @param list The list of items to get a random item from.
     * @param item_to_exclude The item instance to exclude from the possible outcomes.
     * @param source The random source to use for getting the random item.
     * @return The random item, ensuring that is not the instance specified in {@code item_to_exclude}.
     * @param <T> The type of the items to select from. An item of such type is returned.
     * @apiNote This method uses the {@link Object#equals(Object)} pattern to compare the objects.
     * Use {@link #SelectRandomFromListWithExclusionUnsafe(List, Object, IEqualityComparer, RandomSource)} if you want to control how comparison is done.
     */
    public static <T> T SelectRandomFromListWithExclusionUnsafe(List<T> list , @MaybeNull T item_to_exclude , RandomSource source)
    {
        return SelectRandomFromListWithExclusionUnsafe(list , item_to_exclude , new JavaObjectEqualsEqualityComparer<>() , source);
    }

    public static int RandomBetweenInclusive(RandomSource rs , int min_inclusive , int max_inclusive)
            throws ArgumentNullException
    {
        ArgumentNullException.ThrowIfNull(rs , "rs");
        return RandomBetweenInclusiveUnsafe(rs , min_inclusive , max_inclusive);
    }

    public static int RandomBetweenInclusiveUnsafe(RandomSource rs , int min_inclusive , int max_inclusive)
    {
        return rs.nextInt(max_inclusive - min_inclusive + 1) + min_inclusive;
    }

    public static float RandomBetween(RandomSource rs, float min_inclusive, float max_exclusive)
        throws ArgumentNullException
    {
        ArgumentNullException.ThrowIfNull(rs , "rs");
        return RandomBetweenUnsafe(rs , min_inclusive , max_exclusive);
    }

    public static double RandomBetween(RandomSource rs, double min_inclusive, double max_exclusive)
            throws ArgumentNullException
    {
        ArgumentNullException.ThrowIfNull(rs , "rs");
        return RandomBetweenUnsafe(rs , min_inclusive , max_exclusive);
    }

    public static float RandomBetweenUnsafe(RandomSource random, float min_inclusive, float max_exclusive) {
        return random.nextFloat() * (max_exclusive - min_inclusive) + min_inclusive;
    }

    public static double RandomBetweenUnsafe(RandomSource random, double min_inclusive, double max_exclusive) {
        return random.nextDouble() * (max_exclusive - min_inclusive) + min_inclusive;
    }

    public static double NumberMap(double input , double inputbase , double outputbase)
    {
        return ((input / inputbase) * outputbase);
    }

    public static double Clamp(double value , double minimum , double maximum)
    {
        return value < minimum ? minimum : Math.min(value, maximum);
    }

    public static float Clamp(float value , float minimum , float maximum)
    {
        return value < minimum ? minimum : Math.min(value, maximum);
    }

    public static int Clamp(int value , int minimum , int maximum)
    {
        return value < minimum ? minimum : Math.min(value, maximum);
    }

    public static float ToNormalRange(float v, float min, float max) { return Math.abs(v - min) / Math.abs(min - max); }

    public static double ToNormalRange(double v, double min, double max) { return Math.abs(v - min) / Math.abs(min - max); }

    public static double ClampedMapToRange(double input, double inputlowerbound, double inputupperbound, double outputlowerbound, double outputupperbound)
    {
        return (ToNormalRange(input > inputupperbound ? inputupperbound : Math.max(input, inputlowerbound), inputlowerbound, inputupperbound) * (outputupperbound - outputlowerbound)) + outputlowerbound;
    }

    public static float ClampedMapToRange(float input, float inputlowerbound, float inputupperbound, float outputlowerbound, float outputupperbound)
    {
        return (ToNormalRange(input > inputupperbound ? inputupperbound : Math.max(input, inputlowerbound), inputlowerbound, inputupperbound) * (outputupperbound - outputlowerbound)) + outputlowerbound;
    }

}

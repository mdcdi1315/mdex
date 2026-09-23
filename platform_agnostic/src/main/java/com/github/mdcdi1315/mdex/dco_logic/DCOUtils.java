package com.github.mdcdi1315.mdex.dco_logic;

import com.github.mdcdi1315.DotNetLayer.System.ArgumentNullException;
import com.github.mdcdi1315.DotNetLayer.System.Collections.Generic.IEnumerable;
import com.github.mdcdi1315.mdex.block.blockstateproviders.WeightedStateProvider;

/**
 * Provides utilities for working with the DCO logic.
 */
public final class DCOUtils
{
    /**
     * Compiles all the compilable objects in the iterable object, or fails. <br />
     * The method will additionally throw the exception directly, if any exception occurs from {@link Compilable#Compile()} calls.
     * @param iterable The iterable to compile its objects.
     * @return A value whether all the objects were compiled in the list. If false, at least one element in the list failed compilation.
     * @param <T> The compilable object type to be compiled.
     * @exception ArgumentNullException {@code iterable} was null.
     */
    public static <T extends Compilable> boolean CompileAllOrFail(Iterable<T> iterable)
            throws ArgumentNullException
    {
        ArgumentNullException.ThrowIfNull(iterable , "iterable");
        for (T i : iterable)
        {
            i.Compile();
            if (!i.IsCompiled()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Compiles all the compilable objects in the enumerable object, or fails. <br />
     * The method will additionally throw the exception directly, if any exception occurs from {@link Compilable#Compile()} calls.
     * @param enumerable The iterable to compile its objects.
     * @return A value whether all the objects were compiled in the list. If false, at least one element in the list failed compilation.
     * @param <T> The compilable object type to be compiled.
     * @exception ArgumentNullException {@code iterable} was null.
     * @since 2.2.8
     */
    public static <T extends Compilable> boolean CompileAllOrFail(IEnumerable<T> enumerable)
            throws ArgumentNullException
    {
        ArgumentNullException.ThrowIfNull(enumerable, "enumerable");
        try (var enumerator = enumerable.GetEnumerator())
        {
            Compilable e;
            while (enumerator.MoveNext())
            {
                e = enumerator.getCurrent();
                e.Compile();
                if (!e.IsCompiled())
                {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Compiles all the specified compilable objects, or fails.<br />
     * The method will additionally throw the exception directly, if any exception occurs from {@link Compilable#Compile()} calls.
     * @param elements The elements to compile. This is a variable argument array.
     * @return A value whether all the objects were compiled in the array. If false, at least one element in the list failed compilation.
     * @param <T> The compilable object type to be compiled.
     */
    @SafeVarargs
    public static <T extends Compilable> boolean CompileAllOrFail(T... elements)
    {
        for (T i : elements)
        {
            i.Compile();
            if (!i.IsCompiled()) {
                return false;
            }
        }
        return true;
    }
}

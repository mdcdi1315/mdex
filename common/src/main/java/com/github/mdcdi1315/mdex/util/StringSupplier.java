package com.github.mdcdi1315.mdex.util;

import java.util.function.Supplier;

/**
 * Provides a supplier for string data.
 * @param stringdata The string data that the {@link StringSupplier#get} method will return.
 * @since 1.7.0
 */
public record StringSupplier(String stringdata)
        implements Supplier<String>
{
    /**
     * Returns the value of the {@link StringSupplier#stringdata} field. <br />
     * Provided to support the {@link Supplier#get} method.
     * @return The value of the field.
     */
    @Override
    public String get() {
        return stringdata;
    }
}

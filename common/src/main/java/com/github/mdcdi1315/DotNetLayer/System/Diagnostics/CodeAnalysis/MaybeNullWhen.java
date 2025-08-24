package com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis;

import com.github.mdcdi1315.DotNetLayer.System.Attribute;
import com.github.mdcdi1315.DotNetLayer.System.AttributeTargets;
import com.github.mdcdi1315.DotNetLayer.System.AttributeUsage;

import java.lang.annotation.Documented;

/**
 * Specifies that when a method returns {@link MaybeNullWhen#ReturnValue},
 * the parameter may be null even if the corresponding type disallows it.
 */
@Attribute
@Documented
@AttributeUsage(value = AttributeTargets.Parameter , Inherited = false)
public @interface MaybeNullWhen
{
    /**
     * Gets the return value condition.
     * @return The return value condition. If the method returns this value, the associated parameter may be null.
     */
    boolean ReturnValue();
}
package com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis;

import com.github.mdcdi1315.DotNetLayer.System.*;

import java.lang.annotation.Documented;

/**
 * Annotation specified to a parameter or method return value to indicate that a value may be null even if it's type disallows it.
 */
@Attribute
@Documented
@AttributeUsage(value = { AttributeTargets.Parameter , AttributeTargets.ReturnValue }, AllowMultiple = false)
public @interface MaybeNull {
    
}

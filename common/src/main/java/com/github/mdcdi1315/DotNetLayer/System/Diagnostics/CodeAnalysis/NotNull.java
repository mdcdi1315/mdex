package com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis;

import com.github.mdcdi1315.DotNetLayer.System.Attribute;
import com.github.mdcdi1315.DotNetLayer.System.AttributeTargets;
import com.github.mdcdi1315.DotNetLayer.System.AttributeUsage;

import java.lang.annotation.Documented;

/**
 * Specifies that an output is not null even if the corresponding type allows it.
 * <p>Specifies that an input argument was not null when the call returns.</p>
 */
@Attribute
@Documented
@AttributeUsage(value = { AttributeTargets.Parameter , AttributeTargets.Field , AttributeTargets.ReturnValue } , AllowMultiple = false)
public @interface NotNull {

}

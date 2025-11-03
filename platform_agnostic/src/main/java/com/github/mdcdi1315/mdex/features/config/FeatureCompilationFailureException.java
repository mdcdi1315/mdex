package com.github.mdcdi1315.mdex.features.config;

import com.github.mdcdi1315.DotNetLayer.System.Exception;
import com.github.mdcdi1315.mdex.util.MDEXException;

/**
 * Generic exception class to be used when another compilable object has failed compilation.
 */
public final class FeatureCompilationFailureException
    extends MDEXException
{
    public FeatureCompilationFailureException() { super(); }

    public FeatureCompilationFailureException(String msg) { super(msg); }

    public FeatureCompilationFailureException(String msg, Exception inner) { super(msg , inner); }
}

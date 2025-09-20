package com.github.mdcdi1315.mdex.util;

import com.github.mdcdi1315.mdex.MDEXBalmLayer;
import com.github.mdcdi1315.DotNetLayer.System.IDisposable;

/**
 * Provides a base implementation framework for classes that want to support both {@link Compilable} and {@link IDisposable} interfaces.
 */
public abstract class AbstractDisposableCompilable
    implements Compilable , IDisposable
{
    private byte state;
    private static final byte STATE_IS_COMPILED = 1 << 0 , STATE_IS_DISPOSED = 1 << 1;

    public final void Compile()
    {
        try {
            CompileImplementation();
            state |= STATE_IS_COMPILED;
        } catch (Exception e) {
            MDEXBalmLayer.LOGGER.error("Cannot compile the abstract compilable disposable object." , e);
        }
    }

    public final boolean IsCompiled()
    {
        return ((state & STATE_IS_COMPILED) != 0) && ((state & STATE_IS_DISPOSED) == 0);
    }

    protected abstract void CompileImplementation();

    protected void Dispose(boolean disposing) {

    }

    public final void Dispose()
    {
        if ((state & STATE_IS_DISPOSED) == 0)
        {
            state |= STATE_IS_DISPOSED;
            Dispose(true);
        }
    }
}

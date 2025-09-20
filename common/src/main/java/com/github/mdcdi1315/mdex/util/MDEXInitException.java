package com.github.mdcdi1315.mdex.util;

import com.github.mdcdi1315.DotNetLayer.System.Exception;
import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.MaybeNull;

/**
 * The exception that is thrown when something on the mod's initializer has not gone very well...
 */
public final class MDEXInitException
        extends MDEXException
{
    /**
     * Creates a new instance of the {@link MDEXInitException} class, with a message that describes the cause of this exception.
     * @param message The message that describes the cause of this exception instance.
     */
    public MDEXInitException(String message) {
        super(message);
    }

    /**
     * Creates a new instance of the {@link MDEXInitException} class, with a message that describes the cause of this exception,
     * as well as the original exception that is the cause of this exception instance.
     * @param message The message that describes the cause of this exception instance.
     * @param inner Optional. Provides the original exception that is the cause of creating this exception instance.
     */
    public MDEXInitException(String message , @MaybeNull Exception inner) {
        super(message , inner);
    }
}

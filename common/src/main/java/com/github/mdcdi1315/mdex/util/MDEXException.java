package com.github.mdcdi1315.mdex.util;

import com.github.mdcdi1315.DotNetLayer.System.Exception;
import com.github.mdcdi1315.DotNetLayer.System.ApplicationException;
import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.MaybeNull;

/**
 * Defines the base exception class for all mod-sourcing exceptions. <br />
 * Should be sub-classed.
 */
public class MDEXException
    extends ApplicationException
{
    /**
     * Creates a new and empty instance of the {@link MDEXException} class.
     */
    public MDEXException() { super(); }

    /**
     * Creates a new instance of the {@link MDEXException} class, with a message that describes the cause of this exception.
     * @param msg The message that describes the cause of this exception.
     */
    public MDEXException(String msg) { super(msg); }

    /**
     * Creates a new instance of the {@link MDEXException} class, with a message that describes the cause of this exception,
     * as well as the original exception that is the cause of this exception instance.
     * @param msg The message that describes the cause of this exception.
     * @param except Optional. Provides the original exception that is the cause of creating this exception instance.
     */
    public MDEXException(String msg , @MaybeNull Exception except) { super(msg , except); }
}

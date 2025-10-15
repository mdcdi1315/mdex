package com.github.mdcdi1315.mdex.api.saveddata;

import com.github.mdcdi1315.DotNetLayer.System.Exception;

import com.github.mdcdi1315.mdex.util.MDEXException;

/**
 * The exception that is thrown when the read saved data format details are not correct, or the saved data could not be read.
 */
public final class IncorrectSavedDataFormatException
    extends MDEXException
{
    public IncorrectSavedDataFormatException() {}

    public IncorrectSavedDataFormatException(String message) { super(message); }

    public IncorrectSavedDataFormatException(String message, Exception ex) { super(message , ex); }
}

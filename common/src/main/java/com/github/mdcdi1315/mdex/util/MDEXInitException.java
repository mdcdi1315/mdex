package com.github.mdcdi1315.mdex.util;

import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.MaybeNull;
import com.github.mdcdi1315.DotNetLayer.System.Exception;

public final class MDEXInitException extends MDEXException
{
    public MDEXInitException(String message) {
        super(message);
    }

    public MDEXInitException(String message , @MaybeNull Exception inner) {
        super(message , inner);
    }
}

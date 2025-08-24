package com.github.mdcdi1315.mdex.util;

import com.github.mdcdi1315.DotNetLayer.System.Exception;
import com.github.mdcdi1315.DotNetLayer.System.ApplicationException;
import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.MaybeNull;

public class MDEXException
    extends ApplicationException
{
    public MDEXException() { super(); }

    public MDEXException(String msg) { super(msg); }

    public MDEXException(String msg , @MaybeNull Exception except) { super(msg , except); }
}

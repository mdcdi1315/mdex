package com.github.mdcdi1315.mdex.util;

public class BlockPropertyNotFoundException
    extends MDEXException
{
    private final String propname;

    public BlockPropertyNotFoundException(String propertyname)
    {
        super(String.format("Cannot find the requested block property in the given block definition. \nProperty Name: %s" , propertyname));
        propname = propertyname;
    }

    public String getPropertyName()
    {
        return propname;
    }
}

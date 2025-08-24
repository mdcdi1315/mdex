package com.github.mdcdi1315.mdex.util;

import com.github.mdcdi1315.DotNetLayer.System.Exception;

public class InvalidFeatureConfigurationException
    extends MDEXException
{
    public InvalidFeatureConfigurationException(String message)
    {
        super(message);
    }

    public InvalidFeatureConfigurationException(String message , Exception inner)
    {
        super(message , inner);
    }
}

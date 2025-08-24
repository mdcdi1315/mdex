package com.github.mdcdi1315.mdex.util;

import net.minecraft.resources.ResourceLocation;

/**
 * Base exception class for cases where registries are not found.
 */
public class RegistryNotFoundException
        extends MDEXException
{
    private final ResourceLocation location;

    public RegistryNotFoundException(ResourceLocation location)
    {
        super(String.format("The registry with resource location %s was not found." , location));
        this.location= location;
    }

    public ResourceLocation GetExpectedLocation()
    {
        return location;
    }
}

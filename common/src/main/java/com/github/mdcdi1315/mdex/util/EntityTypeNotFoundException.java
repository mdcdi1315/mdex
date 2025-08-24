package com.github.mdcdi1315.mdex.util;

import com.github.mdcdi1315.DotNetLayer.System.ApplicationException;
import net.minecraft.resources.ResourceLocation;

public class EntityTypeNotFoundException
        extends ApplicationException
{
    private final ResourceLocation location;

    public EntityTypeNotFoundException(ResourceLocation l)
    {
        super(String.format("The specified entity type was not found. \nEntity ID: %s" , l));
        location = l;
    }

    public ResourceLocation GetExpectedLocation()
    {
        return location;
    }
}

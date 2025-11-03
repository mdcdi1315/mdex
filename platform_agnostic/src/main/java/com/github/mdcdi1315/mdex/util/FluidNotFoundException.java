package com.github.mdcdi1315.mdex.util;

import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.NotNull;

import net.minecraft.resources.ResourceLocation;

public class FluidNotFoundException
    extends MDEXException
{
    private final ResourceLocation id;

    public FluidNotFoundException(@NotNull ResourceLocation id)
    {
        this.id = id;
    }

    public ResourceLocation getID()
    {
        return id;
    }

    @Override
    public String getMessage() {
        return String.format(
                "Cannot perform the operation because the specified fluid was not found.\nFluid ID: %s", id
        );
    }
}

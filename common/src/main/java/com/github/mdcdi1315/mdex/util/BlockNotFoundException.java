package com.github.mdcdi1315.mdex.util;

import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.NotNull;
import net.minecraft.resources.ResourceLocation;

public class BlockNotFoundException
    extends MDEXException
{
    private final ResourceLocation id;

    public BlockNotFoundException(@NotNull ResourceLocation id)
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
                "Cannot perform the operation because the specified block was not found.\nBlock ID: %s", id
        );
    }
}

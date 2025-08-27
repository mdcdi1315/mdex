package com.github.mdcdi1315.mdex.api;

import net.blay09.mods.balm.api.DeferredObject;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class RegistryCreationInformation<T>
{
    public ResourceKey<Registry<T>> Key;
    public DeferredObject<IModLoaderRegistry<T>> Registry;

    protected RegistryCreationInformation() {}

    public RegistryCreationInformation(ResourceKey<Registry<T>> key) {
        Key = key;
    }
}

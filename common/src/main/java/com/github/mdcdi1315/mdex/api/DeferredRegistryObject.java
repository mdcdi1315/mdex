package com.github.mdcdi1315.mdex.api;

import com.github.mdcdi1315.DotNetLayer.System.InvalidOperationException;
import net.blay09.mods.balm.api.DeferredObject;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class DeferredRegistryObject<T>
        extends DeferredObject<IModLoaderRegistry<T>>
{
    private boolean resolvable;
    private Supplier<IModLoaderRegistry<T>> s;

    public DeferredRegistryObject(ResourceLocation id) {
        super(id);
        s = null;
        resolvable = false;
    }

    @Override
    public boolean canResolve() {
        return resolvable;
    }

    @Override
    public IModLoaderRegistry<T> resolve() {
        if (object == null && resolvable)
        {
            object = s.get();
        }

        return object;
    }

    public void SetAsCanResolveWith(Supplier<IModLoaderRegistry<T>> regsupplier)
    {
        if (resolvable) {
            throw new InvalidOperationException("This deferred object is already bound to a mod loader registry and cannot be modified.");
        }
        resolvable = true;
        s = regsupplier;
    }

    public void SetAsCanResolveWithObject(Object obj)
    {
        if (resolvable) {
            throw new InvalidOperationException("This deferred object is already bound to a mod loader registry and cannot be modified.");
        }
        resolvable = true;
        s = () -> (IModLoaderRegistry<T>) obj;
    }
}
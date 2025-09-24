package com.github.mdcdi1315.mdex.codecs;

import com.github.mdcdi1315.mdex.api.IModLoaderRegistry;

import com.github.mdcdi1315.DotNetLayer.System.ArgumentNullException;

import com.mojang.serialization.Codec;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;

import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

public final class DelayLoadedRegistryByNameCodec<TElement>
    implements Codec<TElement>
{
    private IModLoaderRegistry<TElement> registry;

    public DelayLoadedRegistryByNameCodec() {
        registry = null;
    }

    public void ProvideRegistry(IModLoaderRegistry<TElement> reg)
            throws ArgumentNullException
    {
        ArgumentNullException.ThrowIfNull(reg , "reg");
        registry = reg;
    }

    @Override
    @SuppressWarnings("all")
    public <T> DataResult<Pair<TElement, T>> decode(DynamicOps<T> ops, T input) {
        if (registry == null) {
            return DataResult.error(() -> "The registry has not been initialized yet.");
        }
        var dr = ResourceLocation.CODEC.decode(ops , input);
        var e = dr.error();
        if (e.isPresent()) {
            var m = e.get().message();
            return DataResult.error(() -> m);
        }
        var location = dr.result().get().getFirst();
        Optional<TElement> opt = registry.GetElementValue(location);
        if (opt.isPresent()) {
            return DataResult.success(new Pair<>(opt.get() , input));
        } else {
            String s = String.format("Cannot find registry element with registry key %s in registry %s.", location, registry.GetRegistryKey().location());
            return DataResult.error(() -> s);
        }
    }

    @Override
    public <T> DataResult<T> encode(TElement input, DynamicOps<T> ops, T prefix) {
        if (registry == null) {
            return DataResult.error(() -> "The registry has not been initialized yet.");
        }
        var o = registry.GetResourceKey(input);
        if (o.isPresent()) {
            return ResourceLocation.CODEC.encode(o.get().location() , ops , prefix);
        } else {
            String s = String.format("Cannot verify that the specified registry object exists in registry %s." , registry.GetRegistryKey().location());
            return DataResult.error(() -> s);
        }
    }
}

package com.github.mdcdi1315.mdex.forge.api;

import com.github.mdcdi1315.DotNetLayer.System.ArgumentException;
import com.github.mdcdi1315.DotNetLayer.System.ArgumentNullException;
import com.github.mdcdi1315.DotNetLayer.System.ExecutionEngineException;
import com.github.mdcdi1315.DotNetLayer.System.NotSupportedException;
import com.github.mdcdi1315.mdex.api.*;
import com.mojang.serialization.Codec;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.event.server.ServerStartedEvent;
import net.blay09.mods.balm.api.event.server.ServerStoppedEvent;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@SuppressWarnings("unused") // This class is invoked by common code, just it cannot be seen that is actually in use
public class ModLoaderMethodsImplementation
        implements ModLoaderMethods
{
    private static class RegistryCreationInformationForge<T>
            extends RegistryCreationInformation<T>
    {
        public ResourceKey<Registry<T>> Key;
        public DeferredRegistryObject<T> Registry;

        protected RegistryCreationInformationForge() {}

        public RegistryCreationInformationForge(ResourceKey<Registry<T>> key)
        {
            Key = key;
        }

        public RegistryCreationInformationForge(ResourceKey<Registry<T>> key , DeferredRegistryObject<T> deferred)
        {
            Key = key;
            Registry = deferred;
        }

        public void SetAsResolvableWithRegObject(Object obj)
        {
            Registry.SetAsCanResolveWith(() -> new ForgeRegistryWrappedInRegistry<>(((Supplier<IForgeRegistry<T>>)obj).get()));
        }

        public RegistryBuilder<T> GetRegistryBuilder() {
            return RegistryBuilder.<T>of(Key.location()).disableSync().disableSaving().allowModification();
        }
    }

    private static class DatapackRegistryCreationInformation<T>
            extends RegistryCreationInformationForge<T>
    {
        public DatapackRegistryCreationInformation(ResourceKey<Registry<T>> key , Codec<T> element , Codec<T> network)
        {
            super(key);
            ElementCodec = element;
            NetworkCodec = network;
        }

        public Codec<T> ElementCodec;
        public Codec<T> NetworkCodec;
    }

    private ForgeTeleportingManager manager;
    private List<RegistryCreationInformationForge<?>> infos;
    public List<Runnable> registryokmethods;
    public List<Runnable> executormethods;

    public ModLoaderMethodsImplementation()
    {
        infos = new ArrayList<>(4);
        executormethods = new ArrayList<>(4);
        registryokmethods = new ArrayList<>(4);
        // This constructor is called from Balm, so doing this is perfectly valid.
        Balm.getEvents().onEvent(ServerStartedEvent.class , (ServerStartedEvent sse) -> manager = new ForgeTeleportingManager(sse.getServer()));
        Balm.getEvents().onEvent(ServerStoppedEvent.class, (ServerStoppedEvent sse) -> {
            if (manager != null)
            {
                manager.Dispose();
                manager =null;
            }
        });
    }

    @Override
    public TeleportingManager GetTeleportingManager() {
        return manager;
    }

    @Override
    public <T> void CreateSimpleRegistry(RegistryCreationInformation<T> rci) throws ArgumentNullException
    {
        var d = new DeferredRegistryObject<T>(rci.Key.location());
        rci.Registry = d;
        infos.add(new RegistryCreationInformationForge<>(rci.Key , d));
    }

    @Override
    public <T> void CreateDatapackRegistry(ResourceKey<Registry<T>> key, Codec<T> elementcodec, Codec<T> networkcodec) throws ArgumentNullException, NotSupportedException
    {
        infos.add(new DatapackRegistryCreationInformation<>(key , elementcodec , networkcodec));
    }

    public void RunMethodOnWhenRegistriesAreReady(Runnable runnable) throws ArgumentNullException
    {
        ArgumentNullException.ThrowIfNull(runnable , "runnable");
        executormethods.add(runnable);
    }

    @Override
    public void RunMethodOnWhenAllRegistriesAreRegistered(Runnable runnable) throws ArgumentNullException
    {
        ArgumentNullException.ThrowIfNull(runnable , "runnable");
        registryokmethods.add(runnable);
    }

    @Override
    public <T> IModLoaderRegistry<T> GetRegistry(ResourceLocation location)
            throws ArgumentNullException
    {
        ArgumentNullException.ThrowIfNull(location , "location");
        IForgeRegistry<T> r = RegistryManager.ACTIVE.getRegistry(location);
        if (r == null)
        {
            // Maybe it is a vanilla registry?
            r = RegistryManager.VANILLA.getRegistry(location);
        }
        if (r == null)
        {
            // Maybe it is a frozen registry?
            // If this fails, we must throw exception back to the caller.
            r = RegistryManager.FROZEN.getRegistry(location);
        }
        if (r == null)
        {
            throw new ArgumentException(String.format("Cannot locate the registry with ID %s." , location));
        }
        return new ForgeRegistryWrappedInRegistry<>(r);
    }

    // Internal mod support methods <--
    public void ForEachRegistryCreationInstance(DataPackRegistryEvent.NewRegistry evt)
    {
        Method methodREF = null;
        for (var m : evt.getClass().getMethods())
        {
            if (m.getName().equals("dataPackRegistry") && m.getParameters().length == 3) {
                methodREF = m;
            }
        }
        if (methodREF == null) {
            throw new ExecutionEngineException("Cannot find the 3-argument method dataPackRegistry. Has the method been removed from Forge?");
        }
        for (RegistryCreationInformationForge<?> info : infos)
        {
            if (info instanceof DatapackRegistryCreationInformation<?> dpk)
            {
                try {
                    methodREF.invoke(evt, dpk.Key, dpk.ElementCodec , dpk.NetworkCodec);
                } catch (IllegalAccessException e) {
                    //throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new ExecutionEngineException("Cannot execute the specified method.\nException data:" + e.getTargetException());
                }
            }
        }
    }

    public void ForEachSimpleRegistryInstance(NewRegistryEvent evt)
    {
        Method methodREF = null;
        for (var m : evt.getClass().getMethods())
        {
            if (m.getName().equals("create") && m.getParameters().length == 1) {
                methodREF = m;
            }
        }
        if (methodREF == null) {
            throw new ExecutionEngineException("Cannot find the 1-argument method create. Has the method been removed from Forge?");
        }
        for (RegistryCreationInformationForge<?> entry : infos)
        {
            if (!(entry instanceof DatapackRegistryCreationInformation<?>))
            {
                try {
                    entry.SetAsResolvableWithRegObject(methodREF.invoke(evt, entry.GetRegistryBuilder()));
                } catch (IllegalAccessException e) {
                    //throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new ExecutionEngineException("Cannot execute the specified method.\nException data:" + e.getTargetException());
                }
            }
        }
    }

    public void DestroyUnusedReferences()
    {
        infos.clear();
        infos = null;
        executormethods.clear();
        executormethods = null;
        registryokmethods.clear();
        registryokmethods = null;
    }

    // Internal mod support methods -->

    @Override
    public void Dispose()
    {
        if (manager != null)
        {
            manager.Dispose();
            manager =null;
        }
    }
}
package com.github.mdcdi1315.mdex.neoforge.api;

import com.github.mdcdi1315.DotNetLayer.System.*;
import com.github.mdcdi1315.mdex.api.*;
import com.mojang.serialization.Codec;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.event.server.ServerStartedEvent;
import net.blay09.mods.balm.api.event.server.ServerStoppedEvent;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused") // This class is invoked by common code, just it cannot be seen that is actually in use
public class ModLoaderMethodsImplementation
    implements ModLoaderMethods
{
    private static class RegistryCreationInformationNeoForge<T>
        extends RegistryCreationInformation<T>
    {
        public ResourceKey<Registry<T>> Key;
        public DeferredRegistryObject<T> Registry;

        protected RegistryCreationInformationNeoForge() {}

        public RegistryCreationInformationNeoForge(ResourceKey<Registry<T>> key)
        {
            Key = key;
            Registry = null;
        }

        public RegistryCreationInformationNeoForge(ResourceKey<Registry<T>> key , DeferredRegistryObject<T> reg)
        {
            Key = key;
            Registry = reg;
        }

        public void SetAsResolvableWithRegObject(Object obj)
        {
            Registry.SetAsCanResolveWith(() -> new MinecraftWrappedModLoaderRegistry<>((Registry<T>) obj));
        }

        public RegistryBuilder<T> GetRegistryBuilder() {
            return new RegistryBuilder<>(Key);
        }
    }

    private static class DatapackRegistryCreationInformation<T>
        extends RegistryCreationInformationNeoForge<T>
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

    private NeoForgeTeleportingManager manager;
    private List<RegistryCreationInformationNeoForge<?>> infos;
    public Map<ResourceLocation , Action1<IModLoaderRegistry<?>>> registryinvokemap;

    // This is implicitly called by common code at startup.
    public ModLoaderMethodsImplementation()
    {
        infos = new ArrayList<>(4);
        registryinvokemap = new HashMap<>(10);
        // This constructor is called from Balm, so doing this is perfectly valid.
        Balm.getEvents().onEvent(ServerStartedEvent.class , (ServerStartedEvent sse) -> manager = new NeoForgeTeleportingManager(sse.getServer()));
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
    public <T> void CreateSimpleRegistry(RegistryCreationInformation<T> info) throws ArgumentNullException
    {
        var rg = new DeferredRegistryObject<T>(info.Key.location());
        info.Registry = rg;
        infos.add(new RegistryCreationInformationNeoForge<>(info.Key , rg));
    }

    @Override
    public <T> void CreateDatapackRegistry(ResourceKey<Registry<T>> key, Codec<T> elementcodec, Codec<T> networkcodec) throws ArgumentNullException, NotSupportedException
    {
        infos.add(new DatapackRegistryCreationInformation<>(key , elementcodec , networkcodec));
    }

    @Override
    public <T> void RunMethodOnWhenRegistryIsRegistering(ResourceKey<Registry<T>> key, Action1<IModLoaderRegistry<T>> action)
            throws ArgumentNullException
    {
        ArgumentNullException.ThrowIfNull(key, "key");
        ArgumentNullException.ThrowIfNull(action , "action");
        registryinvokemap.put(key.location() , (IModLoaderRegistry<?> md) -> action.action((IModLoaderRegistry<T>) md));
    }

    @Override
    public <T> IModLoaderRegistry<T> GetRegistry(ResourceLocation location)
            throws ArgumentNullException
    {
        ArgumentNullException.ThrowIfNull(location , "location");
        Registry<T> reg = (Registry<T>) BuiltInRegistries.REGISTRY.get(location);
        if (reg == null)
        {
            throw new ArgumentException(String.format("Cannot locate the registry with ID %s." , location));
        }
        return new MinecraftWrappedModLoaderRegistry<>(reg);
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
        for (RegistryCreationInformation<?> info : infos)
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
        for (RegistryCreationInformationNeoForge<?> entry : infos)
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
        registryinvokemap.clear();
        registryinvokemap = null;
    }

    // Internal mod support methods -->

    @Override
    public void Dispose()
    {
        if (manager != null)
        {
            manager.Dispose();
            manager = null;
        }
    }
}
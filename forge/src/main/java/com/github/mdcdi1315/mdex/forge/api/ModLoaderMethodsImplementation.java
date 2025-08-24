package com.github.mdcdi1315.mdex.forge.api;

import com.github.mdcdi1315.DotNetLayer.System.ArgumentException;
import com.github.mdcdi1315.DotNetLayer.System.NotSupportedException;
import com.github.mdcdi1315.DotNetLayer.System.ArgumentNullException;
import com.github.mdcdi1315.DotNetLayer.System.ExecutionEngineException;

import com.github.mdcdi1315.mdex.api.*;

import com.mojang.serialization.Codec;

import net.minecraft.core.Registry;
import net.minecraft.world.entity.Entity;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceLocation;

import net.minecraftforge.registries.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused") // This class is invoked by common code, just it cannot be seen that is actually in use
public class ModLoaderMethodsImplementation
    implements ModLoaderMethods
{
    private static class RegistryCreationInformation<T>
    {
        public ResourceKey<Registry<T>> Key;

        protected RegistryCreationInformation() {}

        public RegistryCreationInformation(ResourceKey<Registry<T>> key)
        {
            Key = key;
        }

        public RegistryBuilder<T> GetRegistryBuilder() {
            return RegistryBuilder.<T>of(Key.location()).disableSync().disableSaving().allowModification();
        }
    }

    private static class DatapackRegistryCreationInformation<T>
        extends RegistryCreationInformation<T>
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

    private List<RegistryCreationInformation<?>> infos;
    public List<Runnable> executormethods;

    public ModLoaderMethodsImplementation()
    {
        infos = new ArrayList<>(4);
        executormethods = new ArrayList<>(4);
    }

    @Override
    public Entity ChangeDimension(ServerPlayer sp, ServerLevel server, ITeleporter teleporter)
    {
        if (teleporter == null)
        {
            return null;
        }
        return sp.changeDimension(server , new TeleporterForgeTranslator(teleporter));
    }

    @Override
    public <T> void CreateSimpleRegistry(ResourceKey<Registry<T>> key) throws ArgumentNullException
    {
        infos.add(new RegistryCreationInformation<>(key));
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
        for (RegistryCreationInformation<?> entry : infos)
        {
            if (!(entry instanceof DatapackRegistryCreationInformation<?>))
            {
                try {
                    methodREF.invoke(evt, entry.GetRegistryBuilder());
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
    }

    // Internal mod support methods -->

    @Override
    public void Dispose()
    {

    }
}
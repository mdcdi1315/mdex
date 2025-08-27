package com.github.mdcdi1315.mdex.api;

import com.github.mdcdi1315.DotNetLayer.System.ArgumentNullException;
import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.MaybeNull;
import com.github.mdcdi1315.DotNetLayer.System.IDisposable;
import com.github.mdcdi1315.DotNetLayer.System.NotSupportedException;
import com.github.mdcdi1315.mdex.util.RegistryNotFoundException;
import com.mojang.serialization.Codec;
import net.blay09.mods.balm.api.DeferredObject;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

/**
 * Internal methods declaration.
 * Abstracts some mod loader views.
 * Note that the {@link MDEXModAPI} class will search for a class named as 'com.github.mdcdi1315.mdex.%s.api.ModLoaderMethodsImplementation'.
 * Where %s the modloader used.
 */
public interface ModLoaderMethods
    extends IDisposable
{
    /**
     * Gets the teleporting manager for the current mod loader.
     * @return The teleporting manager.
     */
    @MaybeNull
    TeleportingManager GetTeleportingManager();

    /**
     * Creates a simple registry, and makes it known to the mod loader. <br />
     * The registry can be accessed with {@link ModLoaderMethods#GetRegistry(ResourceLocation)} when
     * a runnable provided by {@link ModLoaderMethods#RunMethodOnWhenRegistriesAreReady(Runnable)} is run, <br />
     * or by using the field reference {@link RegistryCreationInformation#Registry}.
     * @param info The registry creation information.
     * @param <T> The type of the elements that the new registry will hold.
     * @throws ArgumentNullException <em>info</em> is null.
     */
    <T> void CreateSimpleRegistry(RegistryCreationInformation<T> info)
            throws ArgumentNullException;

    /**
     * Creates a datapack registry, and makes it known to the mod loader. <br />
     * It's contents are known when the {@link net.blay09.mods.balm.api.event.server.ServerStartingEvent} fires. <br />
     * The registry is returned through the server's registry access.
     * @param key The resource key where this new registry will be located to.
     * @param elementcodec The codec to use for a single element serialization.
     * @param networkcodec Optional. A network codec of the registry, if required some of it's elements to be transmitted over network.
     * @param <T> The type of the elements that the new registry will hold.
     * @throws ArgumentNullException <em>key</em> or <em>elementcodec</em> are null.
     * @throws NotSupportedException A network codec was provided, but it is not supported by the current mod loader.
     */
    <T> void CreateDatapackRegistry(ResourceKey<Registry<T>> key , Codec<T> elementcodec , @MaybeNull Codec<T> networkcodec)
            throws ArgumentNullException , NotSupportedException;

    /**
     * Creates a datapack registry, and makes it known to the mod loader. <br />
     * It's contents are known when the {@link net.blay09.mods.balm.api.event.server.ServerStartingEvent} fires. <br />
     * The registry is returned through the server's registry access.
     * @param key The resource key where this new registry will be located to.
     * @param elementcodec The codec to use for a single element serialization.
     * @param <T> The type of the elements that the new registry will hold.
     * @throws ArgumentNullException <em>key</em> or <em>elementcodec</em> are null.
     */
    default <T> void CreateDatapackRegistry(ResourceKey<Registry<T>> key , Codec<T> elementcodec)
            throws ArgumentNullException
    {
        CreateDatapackRegistry(key , elementcodec , null);
    }

    default <T> DeferredObject<T> Register(ResourceKey<Registry<T>> key , Function<ResourceLocation, T> fu, ResourceLocation objectid)
            throws ArgumentNullException , RegistryNotFoundException
    {
        ArgumentNullException.ThrowIfNull(key , "key");
        ArgumentNullException.ThrowIfNull(fu , "fu");
        ArgumentNullException.ThrowIfNull(objectid , "objectid");
        return new DeferredObject<>(objectid , () -> {
            T object = fu.apply(objectid);
            GetRegistry(key).Register(objectid , object);
            return object;
        });
    }

    /**
     * Provides a method to be executed, when the mod loader asserts that is ready to register registry objects to its registries. <br />
     * Multiple methods may be also provided to be executed, to ensure that class encapsulation is preserved.
     * @param runnable The {@link Runnable} to run.
     * @throws ArgumentNullException <em>runnable</em> was null.
     */
    void RunMethodOnWhenRegistriesAreReady(Runnable runnable) throws ArgumentNullException;

    /**
     * Provides a method to be executed after all the simple registries are registered. <br />
     * You may use this in cases that you need only fast read-only access to it to register codecs and other things. <br />
     * Unlike {@link ModLoaderMethods#RunMethodOnWhenRegistriesAreReady} which does dispatch all the methods async, this does synchronize with the new registry event instead.
     * @param runnable The {@link Runnable} to run.
     * @throws ArgumentNullException <em>runnable</em> was null.
     */
    void RunMethodOnWhenAllRegistriesAreRegistered(Runnable runnable) throws ArgumentNullException;

    /**
     * Gets a registry at the specified location.
     * @param location The location of the registry.
     * @return The registry, wrapped into a {@link IModLoaderRegistry} interface instance.
     * @param <T> The type of the elements that this registry holds.
     * @throws ArgumentNullException <em>location</em> was null.
     * @throws RegistryNotFoundException The registry with the specified location was not found.
     */
    <T> IModLoaderRegistry<T> GetRegistry(ResourceLocation location) throws ArgumentNullException , RegistryNotFoundException;

    /**
     * Gets a registry at the specified location.
     * @param key The key that specifies the registry location.
     * @return The registry, wrapped into a {@link IModLoaderRegistry} interface instance.
     * @param <T> The type of the elements that this registry holds.
     * @throws ArgumentNullException <em>key</em> was null.
     * @throws RegistryNotFoundException The registry with the specified location was not found.
     */
    default <T> IModLoaderRegistry<T> GetRegistry(ResourceKey<Registry<T>> key)
            throws ArgumentNullException , RegistryNotFoundException
    {
        ArgumentNullException.ThrowIfNull(key ,"key");
        return GetRegistry(key.location());
    }
}

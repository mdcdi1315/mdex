package com.github.mdcdi1315.mdex.neoforge;

import com.github.mdcdi1315.mdex.MDEXBalmLayer;
import com.github.mdcdi1315.mdex.api.MDEXModAPI;
import com.github.mdcdi1315.mdex.api.client.MDEXClientModule;
import com.github.mdcdi1315.mdex.neoforge.api.ModLoaderMethodsImplementation;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.client.BalmClient;
import net.blay09.mods.balm.neoforge.NeoForgeLoadContext;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;

@Mod(MDEXBalmLayer.MODID)
public final class MDEXNeoForge
{
    public MDEXNeoForge(IEventBus context)
    {
        NeoForgeLoadContext FLC = new NeoForgeLoadContext(context);
        Balm.initializeMod(MDEXBalmLayer.MODID, FLC, () -> {
            MDEXBalmLayer.Initialize();
            FLC.modBus().addListener(MDEXNeoForge::NewRegistryEventLoader);
            FLC.modBus().addListener(MDEXNeoForge::NewDataPackRegistryEventLoader);
            FLC.modBus().addListener(MDEXNeoForge::CompletedEventHandler);
            FLC.modBus().addListener(MDEXNeoForge::RegisterCapabilitiesEventAfterThisSafeToLoadRegistries);
        });
        if (FMLEnvironment.dist.isClient())
        {
            BalmClient.initializeMod(MDEXBalmLayer.MODID, FLC, new MDEXClientModule());
        }
    }

    private static void RegisterCapabilitiesEventAfterThisSafeToLoadRegistries(RegisterCapabilitiesEvent rce)
    {
        var m = ((ModLoaderMethodsImplementation)MDEXModAPI.getMethodImplementation());
        for (var r : m.executormethods)
        {
            MDEXBalmLayer.RunTaskAsync(r);
        }
    }

    private static void NewDataPackRegistryEventLoader(DataPackRegistryEvent.NewRegistry evt)
    {
        MDEXBalmLayer.LOGGER.info("Registering custom datapack registries to NeoForge.");
        ((ModLoaderMethodsImplementation)MDEXModAPI.getMethodImplementation()).ForEachRegistryCreationInstance(evt);
    }

    private static void NewRegistryEventLoader(NewRegistryEvent evt)
    {
        MDEXBalmLayer.LOGGER.info("Registering custom registries to NeoForge.");
        var mi = ((ModLoaderMethodsImplementation)MDEXModAPI.getMethodImplementation());
        mi.ForEachSimpleRegistryInstance(evt);
        for (var g : mi.registryokmethods)
        {
            try {
                g.run();
            } catch (Exception e) {
                MDEXBalmLayer.LOGGER.info("Error executing a scheduled task" , e);
            }
        }
    }

    private static void CompletedEventHandler(FMLLoadCompleteEvent evt)
    {
        MDEXBalmLayer.LOGGER.info("Mining Dimension: EX mod has been successfully initialized. Cleaning initialization state.");
        ((ModLoaderMethodsImplementation)MDEXModAPI.getMethodImplementation()).DestroyUnusedReferences();
    }
}

package com.github.mdcdi1315.mdex.forge;

import com.github.mdcdi1315.mdex.MDEXBalmLayer;
import com.github.mdcdi1315.mdex.api.MDEXModAPI;
import com.github.mdcdi1315.mdex.api.client.MDEXClientModule;
import com.github.mdcdi1315.mdex.forge.api.ModLoaderMethodsImplementation;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.EmptyLoadContext;
import net.blay09.mods.balm.api.client.BalmClient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DataPackRegistryEvent;
import net.minecraftforge.registries.NewRegistryEvent;

@Mod(MDEXBalmLayer.MODID)
public final class MDEXForge
{
    public MDEXForge(FMLJavaModLoadingContext cxt)
    {
        var evb = cxt.getModEventBus();
        Balm.initializeMod(MDEXBalmLayer.MODID, EmptyLoadContext.INSTANCE, () -> {
            MDEXBalmLayer.Initialize();
            evb.addListener(MDEXForge::NewRegistryEventLoader);
            evb.addListener(MDEXForge::NewDataPackRegistryEventLoader);
            evb.addListener(MDEXForge::CompletedEventHandler);
            evb.addListener(MDEXForge::RegisterCapabilitiesEventAfterThisSafeToLoadRegistries);
        });
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> MDEXForge::RunClient);
    }

    private static void RunClient()
    {
        BalmClient.initializeMod(MDEXBalmLayer.MODID, EmptyLoadContext.INSTANCE, new MDEXClientModule());
    }

    // TODO: Find the next event after the registries have been registered
    private static void RegisterCapabilitiesEventAfterThisSafeToLoadRegistries(RegisterCapabilitiesEvent rce)
    {
        var m = ((ModLoaderMethodsImplementation)MDEXModAPI.getMethodImplementation());
        for (var g : m.registryokmethods)
        {
            try {
                g.run();
            } catch (Exception e) {
                MDEXBalmLayer.LOGGER.info("Error executing a scheduled task" , e);
            }
        }
        // Ensure that the registries will have been correctly initialized
        for (var r : m.executormethods)
        {
            MDEXBalmLayer.RunTaskAsync(r);
        }
    }

    private static void NewDataPackRegistryEventLoader(DataPackRegistryEvent.NewRegistry evt)
    {
        MDEXBalmLayer.LOGGER.info("Registering custom datapack registries to Forge.");
        ((ModLoaderMethodsImplementation)MDEXModAPI.getMethodImplementation()).ForEachRegistryCreationInstance(evt);
    }

    private static void NewRegistryEventLoader(NewRegistryEvent evt)
    {
        MDEXBalmLayer.LOGGER.info("Registering custom registries to Forge.");
        var mi = ((ModLoaderMethodsImplementation)MDEXModAPI.getMethodImplementation());
        mi.ForEachSimpleRegistryInstance(evt);
    }

    private static void CompletedEventHandler(FMLLoadCompleteEvent evt)
    {
        MDEXBalmLayer.LOGGER.info("Mining Dimension: EX mod has been successfully initialized. Cleaning initialization state.");
        ((ModLoaderMethodsImplementation)MDEXModAPI.getMethodImplementation()).DestroyUnusedReferences();
    }
}
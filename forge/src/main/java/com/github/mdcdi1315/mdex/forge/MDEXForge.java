package com.github.mdcdi1315.mdex.forge;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.client.BalmClient;
import net.blay09.mods.balm.forge.ForgeLoadContext;

import net.minecraftforge.registries.*;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;

import com.github.mdcdi1315.mdex.MDEXBalmLayer;
import com.github.mdcdi1315.mdex.api.MDEXModAPI;
import com.github.mdcdi1315.mdex.api.client.MDEXClientModule;
import com.github.mdcdi1315.mdex.forge.api.ModLoaderMethodsImplementation;

@Mod(MDEXBalmLayer.MODID)
public final class MDEXForge
{
    public MDEXForge(FMLJavaModLoadingContext context)
    {
        ForgeLoadContext FLC = new ForgeLoadContext(context.getModEventBus());
        Balm.initializeMod(MDEXBalmLayer.MODID, FLC, () -> {
            MDEXBalmLayer.Initialize();
            FLC.modEventBus().addListener(MDEXForge::NewRegistryEventLoader);
            FLC.modEventBus().addListener(MDEXForge::NewDataPackRegistryEventLoader);
            FLC.modEventBus().addListener(MDEXForge::CompletedEventHandler);
            FLC.modEventBus().addListener(MDEXForge::RegisterCapabilitiesEventAfterThisSafeToLoadRegistries);
        });
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> () -> BalmClient.initializeMod(MDEXBalmLayer.MODID, FLC, new MDEXClientModule()));
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
        MDEXBalmLayer.LOGGER.info("Registering custom datapack registries to Forge.");
        ((ModLoaderMethodsImplementation)MDEXModAPI.getMethodImplementation()).ForEachRegistryCreationInstance(evt);
    }

    private static void NewRegistryEventLoader(NewRegistryEvent evt)
    {
        MDEXBalmLayer.LOGGER.info("Registering custom registries to Forge.");
        ((ModLoaderMethodsImplementation)MDEXModAPI.getMethodImplementation()).ForEachSimpleRegistryInstance(evt);
    }

    private static void CompletedEventHandler(FMLLoadCompleteEvent evt)
    {
        MDEXBalmLayer.LOGGER.info("Mining Dimension: EX mod has been successfully initialized. Cleaning initialization state.");
        ((ModLoaderMethodsImplementation)MDEXModAPI.getMethodImplementation()).DestroyUnusedReferences();
    }
}

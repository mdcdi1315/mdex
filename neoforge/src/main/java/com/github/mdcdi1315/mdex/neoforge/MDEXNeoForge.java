package com.github.mdcdi1315.mdex.neoforge;

import com.github.mdcdi1315.DotNetLayer.System.Action1;
import com.github.mdcdi1315.mdex.MDEXBalmLayer;
import com.github.mdcdi1315.mdex.api.IModLoaderRegistry;
import com.github.mdcdi1315.mdex.api.MDEXModAPI;
import com.github.mdcdi1315.mdex.api.MinecraftWrappedModLoaderRegistry;
import com.github.mdcdi1315.mdex.api.client.MDEXClientModule;
import com.github.mdcdi1315.mdex.neoforge.api.ModLoaderMethodsImplementation;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.client.BalmClient;
import net.blay09.mods.balm.neoforge.NeoForgeLoadContext;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

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
            FLC.modBus().addListener(MDEXNeoForge::RegisterEventListener);
        });
        if (FMLEnvironment.dist.isClient())
        {
            BalmClient.initializeMod(MDEXBalmLayer.MODID, FLC, new MDEXClientModule());
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
    }

    private static void RegisterEventListener(RegisterEvent rev)
    {
        Action1<IModLoaderRegistry<?>> m = ((ModLoaderMethodsImplementation)MDEXModAPI.getMethodImplementation()).registryinvokemap.get(rev.getRegistryKey().location());
        if (m != null) {
            m.action(new MinecraftWrappedModLoaderRegistry<>(rev.getRegistry()));
        }
    }

    private static void CompletedEventHandler(FMLLoadCompleteEvent evt)
    {
        MDEXBalmLayer.LOGGER.info("Mining Dimension: EX mod has been successfully initialized. Cleaning initialization state.");
        ((ModLoaderMethodsImplementation)MDEXModAPI.getMethodImplementation()).DestroyUnusedReferences();
    }
}

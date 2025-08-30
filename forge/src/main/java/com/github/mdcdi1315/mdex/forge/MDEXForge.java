package com.github.mdcdi1315.mdex.forge;

import com.github.mdcdi1315.DotNetLayer.System.Action1;

import com.github.mdcdi1315.mdex.MDEXBalmLayer;
import com.github.mdcdi1315.mdex.api.IModLoaderRegistry;
import com.github.mdcdi1315.mdex.api.MDEXModAPI;
import com.github.mdcdi1315.mdex.api.MinecraftWrappedModLoaderRegistry;
import com.github.mdcdi1315.mdex.api.client.MDEXClientModule;
import com.github.mdcdi1315.mdex.forge.api.ForgeRegistryWrappedInRegistry;
import com.github.mdcdi1315.mdex.forge.api.ModLoaderMethodsImplementation;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.EmptyLoadContext;
import net.blay09.mods.balm.api.client.BalmClient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DataPackRegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.NewRegistryEvent;
import net.minecraftforge.registries.RegisterEvent;

@Mod(MDEXBalmLayer.MODID)
public final class MDEXForge
{
    public MDEXForge()
    {
        var evb = FMLJavaModLoadingContext.get().getModEventBus();
        Balm.initializeMod(MDEXBalmLayer.MODID, EmptyLoadContext.INSTANCE, () -> {
            MDEXBalmLayer.Initialize();
            evb.addListener(MDEXForge::NewRegistryEventLoader);
            evb.addListener(MDEXForge::NewDataPackRegistryEventLoader);
            evb.addListener(MDEXForge::CompletedEventHandler);
            evb.addListener(MDEXForge::RegisterEventListener);
        });
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> MDEXForge::RunClient);
    }

    private static void RunClient()
    {
        BalmClient.initializeMod(MDEXBalmLayer.MODID, EmptyLoadContext.INSTANCE, new MDEXClientModule());
    }

    private static void RegisterEventListener(RegisterEvent rev)
    {
        Action1<IModLoaderRegistry<?>> m = ((ModLoaderMethodsImplementation)MDEXModAPI.getMethodImplementation()).registryinvokemap.get(rev.getRegistryKey().location());
        if (m != null)
        {
            IForgeRegistry<?> fg = rev.getForgeRegistry();
            if (fg == null) {
                m.action(new MinecraftWrappedModLoaderRegistry<>(rev.getVanillaRegistry()));
            } else {
                m.action(new ForgeRegistryWrappedInRegistry<>(fg));
            }
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

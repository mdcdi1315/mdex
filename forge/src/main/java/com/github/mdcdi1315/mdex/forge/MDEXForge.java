package com.github.mdcdi1315.mdex.forge;

import com.github.mdcdi1315.basemodslib.BaseModsLib;
import com.github.mdcdi1315.basemodslib.BaseModsLibClient;
import com.github.mdcdi1315.basemodslib.eventapi.server.ServerStartedEvent;

import com.github.mdcdi1315.mdex.MDEXModInstance;
import com.github.mdcdi1315.mdex.MDEXClientModInstance;
import com.github.mdcdi1315.mdex.forge.api.ForgeTeleportingManager;
import com.github.mdcdi1315.mdex.api.TeleportingManagerConfiguration;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(MDEXModInstance.MOD_ID)
public final class MDEXForge
{
    public MDEXForge(FMLJavaModLoadingContext context) {
        var eb = context.getModEventBus();
        BaseModsLib.InitializeServerSideMod(new MDEXModInstance() , eb);
        BaseModsLib.GetEventsManager().AddEventListener(ServerStartedEvent.class , MDEXForge::CreateTeleportingManager);
        if (FMLEnvironment.dist == Dist.CLIENT) {
            InitializeClientSideMod(eb);
        }
    }

    private static void InitializeClientSideMod(IEventBus bus)
    {
        BaseModsLibClient.InitializeClientSideMod(new MDEXClientModInstance() , bus);
    }

    private static void CreateTeleportingManager(ServerStartedEvent sse)
    {
        MDEXModInstance.MANAGER = new ForgeTeleportingManager(sse.server() , new TeleportingManagerConfiguration(MDEXModInstance.CONFIG));
    }
}

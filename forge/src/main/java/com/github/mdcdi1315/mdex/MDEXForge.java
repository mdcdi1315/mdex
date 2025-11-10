package com.github.mdcdi1315.mdex;

import com.github.mdcdi1315.basemodslib.BaseModsLib;
import com.github.mdcdi1315.basemodslib.BaseModsLibClient;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(MDEXModInstance.MOD_ID)
public final class MDEXForge
{
    public MDEXForge(FMLJavaModLoadingContext context) {
        IEventBus bus = context.getModEventBus();
        BaseModsLib.InitializeServerSideMod(new MDEXModInstance() , bus);
        if (FMLEnvironment.dist == Dist.CLIENT) {
            InitializeClient(bus);
        }
    }

    private static void InitializeClient(IEventBus event_bus) {
        BaseModsLibClient.InitializeClientSideMod(new MDEXClientModInstance() , event_bus);
    }
}

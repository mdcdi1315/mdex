package com.github.mdcdi1315.mdex.forge;

import com.github.mdcdi1315.mdex.MDEXModInstance;
import com.github.mdcdi1315.basemodslib.BaseModsLib;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(MDEXModInstance.MOD_ID)
public final class MDEXForge
{
    public MDEXForge(FMLJavaModLoadingContext context) {
        BaseModsLib.InitializeServerSideMod(new MDEXModInstance() , context.getModEventBus());
    }
}

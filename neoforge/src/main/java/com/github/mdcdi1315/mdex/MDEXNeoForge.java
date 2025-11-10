package com.github.mdcdi1315.mdex;

import com.github.mdcdi1315.basemodslib.BaseModsLib;

import net.neoforged.fml.common.Mod;
import net.neoforged.bus.api.IEventBus;

@Mod(MDEXModInstance.MOD_ID)
public final class MDEXNeoForge
{
    public MDEXNeoForge(IEventBus mod_event_bus) {
        BaseModsLib.InitializeServerSideMod(new MDEXModInstance() , mod_event_bus);
    }
}

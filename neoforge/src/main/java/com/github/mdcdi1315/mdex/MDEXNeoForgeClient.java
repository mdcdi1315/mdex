package com.github.mdcdi1315.mdex;

import com.github.mdcdi1315.basemodslib.BaseModsLibClient;

import net.neoforged.fml.common.Mod;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.api.distmarker.Dist;

@Mod(value = MDEXModInstance.MOD_ID, dist = Dist.CLIENT)
public final class MDEXNeoForgeClient
{
    public MDEXNeoForgeClient(IEventBus mod_event_bus) {
        BaseModsLibClient.InitializeClientSideMod(new MDEXClientModInstance() , mod_event_bus);
    }
}

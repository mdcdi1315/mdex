package com.github.mdcdi1315.mdex.fabric;

import com.github.mdcdi1315.basemodslib.EmptyModObject;

import com.github.mdcdi1315.mdex.MDEXModInstance;
import com.github.mdcdi1315.basemodslib.BaseModsLib;

import net.fabricmc.api.ModInitializer;

public final class MDEXFabricEntryPoint
    implements ModInitializer
{
    @Override
    public void onInitialize() {
        BaseModsLib.InitializeServerSideMod(new MDEXModInstance() , EmptyModObject.INSTANCE);
    }
}

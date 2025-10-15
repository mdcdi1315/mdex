package com.github.mdcdi1315.mdex.api.client;

import com.github.mdcdi1315.mdex.MDEXBalmLayer;

import net.blay09.mods.balm.api.event.BalmEvents;
import net.blay09.mods.balm.api.client.module.BalmClientModule;
import net.blay09.mods.balm.api.client.keymappings.BalmKeyMappings;

import net.minecraft.resources.ResourceLocation;

public class MDEXClientModule
    implements BalmClientModule
{
    public MDEXClientModule()
    {
        MDEXBalmLayer.LOGGER.info("Mining Dimension: EX Client module now initializing.");
    }

    @Override
    public void registerEvents(BalmEvents events) {
        MDEXBalmLayer.LOGGER.trace("Setting up client-side events.");
        MDEXBalmLayer.SetupClientSideEvents(events);
    }

    @Override
    public void registerKeyMappings(BalmKeyMappings mappings) {
        ModKeyMappings.Initialize(mappings);
    }

    @Override
    public ResourceLocation getId() {
       return ResourceLocation.tryParse(MDEXBalmLayer.MODID);
    }
}

package com.github.mdcdi1315.mdex;

import com.github.mdcdi1315.basemodslib.mods.IClientModInstance;
import com.github.mdcdi1315.basemodslib.config.gui.ConfigurationScreenFactory;
import com.github.mdcdi1315.basemodslib.config.gui.DefaultConfigurationScreenFactory;

import net.minecraft.client.gui.screens.Screen;

public final class MDEXClientModInstance
    implements IClientModInstance
{
    public ConfigurationScreenFactory<Screen> RegisterConfigurationScreenFactory() {
        return new DefaultConfigurationScreenFactory<>(MDEXModInstance.CONFIG, MDEXModInstance.MOD_ID);
    }

    @Override
    public String GetModId() {
        return MDEXModInstance.MOD_ID;
    }

    @Override
    public void Dispose() {

    }
}

package com.github.mdcdi1315.mdex;

import com.github.mdcdi1315.basemodslib.mods.IClientModInstance;

import com.github.mdcdi1315.basemodslib.config.gui.ConfigurationScreenFactory;
import com.github.mdcdi1315.basemodslib.config.gui.DefaultConfigurationScreen;
import com.github.mdcdi1315.basemodslib.config.gui.DefaultConfigurationScreenFactory;

public final class MDEXClientModInstance
    implements IClientModInstance
{

    @Override
    public ConfigurationScreenFactory<DefaultConfigurationScreen<MDEXModConfig>> RegisterConfigurationScreenFactory() {
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

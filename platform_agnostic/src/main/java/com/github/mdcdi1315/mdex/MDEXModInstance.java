package com.github.mdcdi1315.mdex;

import com.github.mdcdi1315.basemodslib.block.IBlockRegistrar;
import com.github.mdcdi1315.basemodslib.mods.IServerModInstance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class MDEXModInstance
    implements IServerModInstance
{
    public static final String MOD_ID = "mdex";
    public static Logger LOGGER;

    @Override
    public void Initialize() {
        LOGGER = LoggerFactory.getLogger("Mining Dimension: EX mod logger");
        LOGGER.info("Now initializing the Mining Dimension: EX mod!");
    }

    @Override
    public void RegisterBlocks(IBlockRegistrar registrar) {

    }

    @Override
    public String GetModId() {
        return MOD_ID;
    }

    @Override
    public void Dispose() {
        LOGGER = null;
    }
}
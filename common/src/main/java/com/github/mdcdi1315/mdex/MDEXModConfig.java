package com.github.mdcdi1315.mdex;

import net.blay09.mods.balm.api.Balm;
import net.minecraft.resources.ResourceLocation;
import net.blay09.mods.balm.api.config.reflection.*;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;

@Config(value = MDEXBalmLayer.MODID)
public class MDEXModConfig
{
    @Comment("Enables additional debug messages to resolve common errors involving invalid feature configs. For this change to take effect, you must restart the game.")
    public boolean DebugFeatureConfigurations = false;

    @Comment("The dimension where the player should return when he clicks on any Teleporter block in the Mining Dimension. By default it is set to the overworld.")
    public ResourceLocation HomeDimension = BuiltinDimensionTypes.OVERWORLD.location();

    @Comment("Whether the portal when placed on the Mining Dimension should be placed in lower Y levels.")
    public boolean ShouldSpawnPortalInDeep = false;

    public static void Initialize()
    {
        var cfg = Balm.getConfig();
        var schema = cfg.registerConfig(MDEXModConfig.class);
        cfg.onConfigAvailable(MDEXModConfig.class , (MDEXModConfig mlc) -> {
            if (!cfg.getConfigFile(schema).exists())
            {
                MDEXBalmLayer.LOGGER.info("Creating empty config file since the file does not exist.");
                cfg.saveLocalConfig(schema);
            }
            MDEXBalmLayer.DebugFeatureConfigurations = mlc.DebugFeatureConfigurations;
            Balm.getConfig().updateLocalConfig(MDEXModConfig.class , MDEXModConfig::EmptyUpdater);
        });
    }

    public static MDEXModConfig getActive() {
        return Balm.getConfig().getActiveConfig(MDEXModConfig.class);
    }

    private static void EmptyUpdater(MDEXModConfig cfg) {}
}

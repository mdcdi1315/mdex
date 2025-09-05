package com.github.mdcdi1315.mdex;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.config.BalmConfig;
import net.blay09.mods.balm.api.config.reflection.*;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;

@Config(value = MDEXBalmLayer.MODID)
public class MDEXModConfig
{
    @Comment(
            "Enables additional debug messages to resolve common errors involving invalid feature configs.\n" +
            "For this change to take effect, you must restart the game."
    )
    public boolean DebugFeatureConfigurations = false;

    @Comment(
            "The dimension where the player should return when he clicks on any Teleporter block in the Mining Dimension.\n" +
            "By default it is set to Minecraft's overworld dimension."
    )
    public ResourceLocation HomeDimension = BuiltinDimensionTypes.OVERWORLD.location();

    @Comment(
            "Enabling this value restricts the portal placement in lower height levels.\n" +
            "It is highly recommended enabling this value when you want the players to be spawned in lower height levels."
    )
    public boolean ShouldSpawnPortalInDeep = false;

    @Comment(
            "Whether the starter's chest should be placed after all when going to the Mining Dimension for the first time.\n" +
            "Useful for cases where the datapack has defined a starter chest to be placed, but server admins do not need it."
    )
    public boolean ShouldPlaceStarterChestAtFirstTime = true;

    public static void Initialize(BalmConfig cfg)
    {
        var schema = cfg.registerConfig(MDEXModConfig.class);
        cfg.onConfigAvailable(MDEXModConfig.class , (MDEXModConfig mlc) -> {
            if (!cfg.getConfigFile(schema).exists())
            {
                MDEXBalmLayer.LOGGER.info("Creating empty config file since the file does not exist.");
                cfg.saveLocalConfig(schema);
            }
            MDEXBalmLayer.DebugFeatureConfigurations = mlc.DebugFeatureConfigurations;
            cfg.updateLocalConfig(MDEXModConfig.class , MDEXModConfig::EmptyUpdater);
        });
    }

    public static MDEXModConfig getActive() {
        return Balm.getConfig().getActiveConfig(MDEXModConfig.class);
    }

    private static void EmptyUpdater(MDEXModConfig cfg) {}
}

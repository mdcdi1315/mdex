package com.github.mdcdi1315.mdex;

import com.github.mdcdi1315.basemodslib.config.IModConfig;
import com.github.mdcdi1315.basemodslib.config.ConfigField;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;

public final class MDEXModConfig
    implements IModConfig
{
    @ConfigField(
            comment = "Enables additional debug messages to resolve common errors involving invalid feature configurations.\n" +
            "For this change to take effect, you must restart the game."
    )
    public boolean DebugFeatureConfigurations = false;

    @ConfigField(
            comment = "Enables additional debug messages to resolve common errors involving invalid structure configurations.\n" +
            "For this change to take effect, you must restart the game."
    )
    public boolean DebugStructureConfigurations = false;

    @ConfigField(
            comment = "Enables additional debug messages to resolve common errors involving invalid structure processor configurations.\n" +
            "For this change to take effect, you must restart the game."
    )
    public boolean DebugStructureProcessorConfigurations = false;

    @ConfigField(
            comment = "Enables additional debug messages to resolve common errors involving invalid feature placement modifier configurations.\n" +
            "For this change to take effect, you must restart the game."
    )
    public boolean DebugFeaturePlacementModifierConfigurations = false;

    @ConfigField(
            comment = "The dimension where the player should return when he clicks on any Teleporter block in the Mining Dimension.\n" +
            "By default it is set to Minecraft's overworld dimension."
    )
    public ResourceLocation HomeDimension = BuiltinDimensionTypes.OVERWORLD.location();

    @ConfigField(
            comment = "Enabling this value restricts the portal placement in lower height levels.\n" +
            "It is highly recommended enabling this value when you want the players to be spawned in lower height levels."
    )
    public boolean ShouldSpawnPortalInDeep = false;

    @ConfigField(
            comment = "Defines a value whether the starter's chest should be placed after all when going to the Mining Dimension for the first time.\n" +
            "Useful for cases where the datapack has defined a starter chest to be placed, but server administrators do not need it."
    )
    public boolean ShouldPlaceStarterChestAtFirstTime = true;

    @ConfigField(
            comment = "Defines a value whether teleportation to the Mining Dimension is disabled.\n" +
            "Useful for those server administrators that want to temporarily disable teleportation to the Mining Dimension.\n" +
            "Players that are still into the Mining Dimension after this is enabled will remain there, however, and will be able to return to their home dimension."
    )
    public boolean DisableTeleportations = false;
    
    @Override
    public String GetName() {
        return MDEXModInstance.MOD_ID + "-common";
    }
}

package com.github.mdcdi1315.mdex.api;

import com.github.mdcdi1315.DotNetLayer.System.ArgumentNullException;

import com.github.mdcdi1315.basemodslib.world.saveddata.PerDimensionWorldDataManager;

import com.github.mdcdi1315.mdex.MDEXModConfig;
import net.minecraft.resources.ResourceLocation;
import com.github.mdcdi1315.mdex.MDEXModInstance;

public final class TeleportingManagerConfiguration
{
    /**
     * Provides the default dimension file name that is used by the Mining Dimension: EX mod.
     */
    public static final String DEFAULT_TELEPORTER_DATA_DIMFILE_NAME = PerDimensionWorldDataManager.EXPECTED_SAVED_DATA_PREFIX + "MDEX_TELEPORTERSPAWNDATA";
    /**
     * Provides the default resource location that is used by the Mining Dimension: EX mod.
     */
    public static final String DEFAULT_FEATURE_RESOURCE_LOCATION = MDEXModInstance.COMPATIBILITY_NAMESPACE + ":teleporter_placement_feature";

    public final boolean DisableTeleportations, ShouldSpawnPortalInDeep, ShouldPlaceStarterChestAtFirstTime;
    public final ResourceLocation HomeDimension, MiningDimension;
    public final String FeatureLocation, DimensionFileName;

    public TeleportingManagerConfiguration(MDEXModConfig config)
    {
        ArgumentNullException.ThrowIfNull(config , "config");

        HomeDimension = config.HomeDimension;
        DisableTeleportations = config.DisableTeleportations;
        ShouldSpawnPortalInDeep = config.ShouldSpawnPortalInDeep;
        ShouldPlaceStarterChestAtFirstTime = config.ShouldPlaceStarterChestAtFirstTime;

        FeatureLocation = DEFAULT_FEATURE_RESOURCE_LOCATION;
        MiningDimension = MDEXModInstance.MINING_DIM_IDENTIFIER;
        DimensionFileName = DEFAULT_TELEPORTER_DATA_DIMFILE_NAME;
    }

    /**
     * Creates a custom configuration instance for a given teleporting manager.
     * @param disable_teleportations
     * @param should_spawn_portal_in_deep
     * @param home_dimension
     * @param mining_dimension
     * @param feature_location A configured feature resource location, as a string, that provides the location for loading the teleporter feature.
     * @param dim_file_name The dimension-specific spawn data file name for saving teleporter data.
     * @throws ArgumentNullException
     */
    public TeleportingManagerConfiguration(
            boolean disable_teleportations,
            boolean should_spawn_portal_in_deep,
            boolean should_place_starter_chest_at_first_time,
            ResourceLocation home_dimension,
            ResourceLocation mining_dimension,
            String feature_location,
            String dim_file_name
    )   throws ArgumentNullException
    {
        ArgumentNullException.ThrowIfNull(home_dimension, "home_dimension");
        ArgumentNullException.ThrowIfNull(mining_dimension, "mining_dimension");
        ArgumentNullException.ThrowIfNullOrEmpty(feature_location, "feature_location");
        ArgumentNullException.ThrowIfNullOrEmpty(dim_file_name, "dim_file_name");
        DisableTeleportations = disable_teleportations;
        ShouldSpawnPortalInDeep = should_spawn_portal_in_deep;
        ShouldPlaceStarterChestAtFirstTime = should_place_starter_chest_at_first_time;
        HomeDimension = home_dimension;
        MiningDimension = mining_dimension;
        FeatureLocation = feature_location;
        DimensionFileName = dim_file_name;
    }
}

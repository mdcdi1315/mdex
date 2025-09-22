

## Miscellaneous features provided by the mod

This article covers miscellaneous features provided by the mod for the datapacks.



### Teleporter feature customization

You can customize how the teleporter start area will look like as well as whether a starter chest should be placed the first time 
a player enters the Mining Dimension.

You must override the `teleporter_placement_feature` feature file that is contained in the `configured_feature` subfolder of the `worldgen` subfolder of the `mdcdi1315_md` namespace.

After that you must use the `mdex:base_teleporter_placement` feature type. That type is defined as follows:

~~~
object(ConfiguredFeature), required
{
    "type": resourcelocation(Feature), must_have_value="mdex:base_teleporter_placement"
    "config": object(BaseTeleporterPlacementFeatureConfiguration), required
    {
        "base_plate_provider": object(AbstractBlockStateProvider), required
        "light_block_provider": object(AbstractBlockStateProvider), required
        "size": <<IntProvider>>, required, range=[1 , 6]
        "starter_chest_placement": object(StarterChestPlacement), optional, default_value={ "container_state_provider": null , "loot_table": null , "placement_probability": 0 }
        {
            "container_state_provider": object(AbstractBlockStateProvider), required
            "loot_table": resourcelocation(LootTable), required
            "placement_probability": float, required, range=[0 , 1]
        }
    }
}
~~~

Fields:

| Name                                                    | Purpose                                                                                                                                                                                                                                    |
|---------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| type                                                    | Defines the feature type. Must be set to `mdex:base_teleporter_placement`.                                                                                                                                                                 |
| config                                                  | Defines the feature's configuration object.                                                                                                                                                                                                |
| config.modids                                           | Defines a list of strings that do represent mod ID's. If the feature does need a specific mod but is not loaded, it is disabled. All the mods in this list must be valid at the specified run time in order for this feature to be placed. |
| config.base_plate_provider                              | Defines a block state provider that provides the plate (surface) of the teleporter area.                                                                                                                                                   |
| config.light_block_provider                             | Defines a block state provider that provides the torches placed on the edges of the teleporter area. The torches are placed only if the place where the teleporter is placed is dark enough.                                               |
| config.size                                             | Defines the size of the area that the teleporter feature will cover. Can be a random value from 1 to 6. Value of 1 makes a 3X3 area.                                                                                                       |
| config.starter_chest_placement                          | Optional. Defines starter chest placement settings.                                                                                                                                                                                        |
| config.starter_chest_placement.container_state_provider | Defines the block state provider providing the container to act as a starter chest. Can be any container extending the `RandomizableContainerBlockEntity` class.                                                                           |
| config.starter_chest_placement.loot_table               | Defines the loot table to associate with the starter chest. Players opening it will generate loot from the loot table provided here.                                                                                                       |
| config.starter_chest_placement.placement_probability    | Defines a floating-point value defining an additional probability that finally places or not the starter chest.                                                                                                                            |

Thus, you can define how the base of the feature will look like, plus the torches to use as well as it's size.

Note also that the starter chest placement is additionally controlled by the config option `ShouldPlaceStarterChestAtFirstTime`.

> [!CAUTION]
Using any other feature type than `mdex:base_teleporter_placement` will result in a crash.

### Overriding the teleporter recipe

You can also override the teleporter recipe and provide your own one if you want to integrate Mining Dimension earlier or later in the game play.

The teleporter recipe location is located at the `mdex_teleporter_recipe` file that is contained in the `recipes` subfolder (`recipe` for 1.21.1 and later) of the `mdcdi1315_md` namespace.

You should define there a crafting table recipe that has as output the `mdex:teleporter` block.




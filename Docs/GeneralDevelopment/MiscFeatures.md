

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

Thus, you can define how the base of the feature will look like, plus the torches to use as well as it's size.

Note also that the starter chest placement is additionally controlled by the config option `ShouldPlaceStarterChestAtFirstTime`.

> [!CAUTION]
Using any other feature type than `mdex:base_teleporter_placement` will result in a crash.

### Overriding the teleporter recipe

You can also override the teleporter recipe and provide your own one if you want to integrate Mining Dimension earlier or later in the game play.

The teleporter recipe location is located at the `mdex_teleporter_recipe` file that is contained in the `recipes` subfolder (`recipe` for 1.21.1 and later) of the `mdcdi1315_md` namespace.

You should define there a crafting table recipe that has as output the `mdex:teleporter` block.




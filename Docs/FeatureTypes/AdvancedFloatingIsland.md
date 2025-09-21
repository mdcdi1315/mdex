
### Advanced Floating Island Feature Type

This is yet another floating island feature type, but it gives more control to the creator, hence it's name.

You can specify both the widths of each layer of the island feature and a [state provider](https://minecraft.wiki/w/Template:Nbt_inherit/block_state_provider/template) to even spice up the island's blocks.
Note that you must replace the namespace name of `minecraft` to `mdex` of the state provider type in order for some internal stuff to work correctly.

Feature type syntax:

~~~
object(ConfiguredFeature), required
{
    "type": resourcelocation(Feature), must_have_value="mdex:advanced_floating_layered_island"
    "config": object(SimpleFloatingIslandConfiguration), required
    {
        "layers": list<<AdvancedCompilableIslandLayer>>, required
        "max_distance_from_ground": <IntProvider>, range=[1, 8], required
        "additional_features_on_top": list<object(PlacedFeature)>, optional, default_value=[], can_be_empty
    }
}
~~~

Where the `AdvancedCompilableIslandLayer` object data is:

~~~
object(AdvancedCompilableIslandLayer), required
{
    "state_provider": object(AbstractBlockStateProvider), required
    "width_x": <IntProvider>, range=[1 , 24], optional, default_value=5
    "width_z": <IntProvider>, range=[1 , 24], optional, default_value=5
}
~~~

An example JSON of the feature:

~~~JSON
{
    "type": "mdex:advanced_floating_layered_island",
    "config": {
        "max_distance_from_ground": {
            "type": "minecraft:uniform",
            "value": {
                "min_inclusive": 3,
                "max_inclusive": 5
            }
        },
        "layers": [
			{
				"width_x": {
					"type": "minecraft:constant",
					"value": 3
				},
				"width_z": {
					"type": "minecraft:constant",
					"value": 3
				},
				"state_provider": {
					"type": "mdex:simple_state_provider",
					"state": {
						"Name": "minecraft:grass_block"
					}
				}
			},
			{
				"width_x": {
					"type": "minecraft:constant",
					"value": 2
				},
				"width_z": {
					"type": "minecraft:constant",
					"value": 2
				},
				"state_provider": {
					"type": "mdex:simple_state_provider",
					"state": {
						"Name": "minecraft:dirt"
					}
				}
			},
			{
				"width_x": {
					"type": "minecraft:constant",
					"value": 1
				},
				"width_z": {
					"type": "minecraft:constant",
					"value": 1
				},
				"state_provider": {
					"type": "mdex:simple_state_provider",
					"state": {
						"Name": "minecraft:stone"
					}
				}
			}
        ]
    }
}
~~~

Note also that you can specify any valid integer provider type here, just the others can give jerky results , depending of the settings you have possibly done.


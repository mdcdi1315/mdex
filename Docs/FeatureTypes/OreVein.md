
## Ore Vein feature type

This performs a generation similar to an ore vein in ordinary Minecraft,
but it can be defined as a feature type.

This is a special case of the Noise Generation-Based Ore feature type,
but this defines also a `rare_placement` field defining the inner rare block
(possibly the rare placement will contain raw blocks).

Feature type syntax:
~~~
object(ConfiguredFeature), required
{
    "type": resourcelocation(FeatureType), must_have_value="mdex:noise_generation_based_ore"
    "config": object(NoiseGenerationBasedOreFeatureConfiguration), required 
    {
        "targets": list<<SingleBlockState>>, required, can_be_empty
        "size": <IntProvider>, range=[3, 58], default_value={ "type": "minecraft:constant" , "value": 12 }
        "y_scale": <IntProvider>, range=[1, 18], default_value={ "type": "minecraft:constant" , "value": 2 }
        "noise": object(NormalNoise.NoiseParameters), required
        "discard_chance_on_air_exposure": float , range=[0 , 1] , required
        "rare_placement": object(RareBlockPlacementSettings), required
        {
            "targets": list<<SingleBlockState>>, required, can_be_empty
            "noise_density_threshold": float, range=[0, 6], required
        }
    }
}
~~~

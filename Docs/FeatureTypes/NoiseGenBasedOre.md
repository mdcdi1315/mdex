

### Noise-based generation ore feature type

This feature type defines a way to generate the ore through perlin noise , 
thus being much more practical and performant in a handful of applications dealing with large ore deposits.

The perlin noise data are being defined by you, thus controlling with which perlin noise the ore will be generated with.

You may also define your noise data to a dedicated file inside the 
`worldgen/noise` folder and reference it, 
for the sake of completeness and reusability with other ore configurations.

Any ore block is generated if the noise at the position that the ore feature currently attempts to place is larger than 0.

Success is considered if at least 9 blocks were placed on the specified area.

Thus, you can use on-line generators like [this](https://misode.github.io/worldgen/noise/).

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
    }
}
~~~

All the field values work as you expect and are defined as how modern Minecraft defines them,
except for `y_scale` which it does define how many blocks up the feature will be generated to.






## The modded classic ore vein feature type.

> [!WARNING]
This feature type was first publisized in 1.7.0 version of the mod.
Although existing in older versions, it was not considered one of 
the mod's shipped features. As such, using it in older versions
neither will have the same syntax defined here, 
nor it is garanteed that will work as expected.
`mdcdi1315` will not take any responsibility about issues 
aroused from such older versions, nor will perform a fix 
for those older versions.

This performs a generation like an ore vein in ordinary Minecraft, but it can be now defined as a feature type.

Unlike the ore vein feature also provided by the mod, this one is even closer to what does look in the game itself.

Feature type syntax:
~~~
object(ConfiguredFeature), required, since_mod_version="1.7.0"
{
    "type": resourcelocation(Feature), must_have_value="mdex:noise_generation_based_ore"
    "config": object(NoiseGenerationBasedOreFeatureConfiguration), required 
    {
        "targets": list<<SingleTargetBlockState>>, required, can_be_empty
        "size": <IntProvider>, range=[3, 58], default_value={ "type": "minecraft:constant" , "value": 12 }
        "y_scale": <IntProvider>, range=[1, 18], default_value={ "type": "minecraft:constant" , "value": 2 }
        "noise": object(NormalNoise.NoiseParameters), required
        "discard_chance_on_air_exposure": float , range=[0 , 1] , required
        "rare_placement": object(RareBlockPlacementSettings), required
        {
            "targets": list<<SingleTargetBlockState>>, required, can_be_empty
            "noise_density_threshold": float, range=[0, 6], required
        }
        "stone_placement": object(BaseStonePlacementSettings), required
        {
            "stone_state": object(CompilableBlockState), required
            "noise_density_threshold": float, range=[0, 6], required
            "probability": float, range=[0, 1], optional, default=0.85
        }
    }
}
~~~

Fields:

| Name                                          | Purpose                                                                                                                                                                                                                                                                                                                            |
|-----------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| type                                          | Defines the feature type. Must always be the value `mdex:noise_generation_based_ore`.                                                                                                                                                                                                                                              |
| config                                        | Defines the feature's configuration object.                                                                                                                                                                                                                                                                                        |
| config.modids                                 | Defines a list of strings that do represent mod ID's. If the feature does need a specific mod but is not loaded, it is disabled. All the mods in this list must be valid at the specified run time in order for this feature to be placed.                                                                                         |
| config.targets                                | Defines an array of rules for what ore blocks to place and when. For more information, see [this document](../SingleTargetBlockState.md).                                                                                                                                                                                          |
| config.size                                   | Defines the horizontal size of the feature , expressed as the span of x and z axis respectively. This is an [integer provider](https://minecraft.wiki/w/Template:Nbt_inherit/int_provider/template).                                                                                                                               |
| config.y_scale                                | Defines the vertical size of the feature , that is how much blocks will span on the y axis.  This is an [integer provider](https://minecraft.wiki/w/Template:Nbt_inherit/int_provider/template).                                                                                                                                   |
| config.noise                                  | Defines the noise settings to use. Can be a reference to a file, or can also be an noise parameters object.                                                                                                                                                                                                                        |
| config.discard_chance_on_air_exposure         | Defines the probability of discarding an ore placement attempt, if at least one of it's neighboring blocks is the air block. If this value is set to zero, no discarding of ore blocks will happen. If respectively set to 1 , ores will be always discarded when one of their neighboring blocks is air.                          |
| config.rare_placement                         | Defines rare ore block placement settings. These settings will be used during each generation pass.                                                                                                                                                                                                                                |
| config.rare_placement.targets                 | Defines a different array of rules of what ore blocks to place and when. This list comprises the rare ore placements. For more information, see [this document](../SingleTargetBlockState.md).                                                                                                                                     |
| config.rare_placement.noise_density_threshold | Defines the threshold that below it, it selects an ore block to place from the `config.targets` array; otherwise, it selects an ore block from the `config.rare_placement.targets` list. This value depends on what noise you have selected for ore generation and thus you must set it accordingly to reflect the rare placement. |
| config.stone_placement                      | Defines the stone block placement settings. These settings will be used during each generation pass.                                                                                                                                                                                                                                |
| config.stone_placement.stone_state                                 | Defines the block state that is base stone block to use for filling the ore vein, just like how Minecraft uses Tuff to fill iron ore veins.    |
| config.stone_placement.noise_density_threshold | Defines the threshold that is allowed to place stone blocks. Values below or equal to this values selects the `stone_state` field to fill the blocks there, along with the selected probability value selected in the `probability` field. |
| config.stone_placement.probability                             | Defines an additional probability value for eventually placing the stone block in that position. | 


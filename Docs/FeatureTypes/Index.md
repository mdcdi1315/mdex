
## Mining Dimension: EX feature types.

This page lists and documents all the configured feature types defined by the mod.

All the feature types described are 'modded' features; 
that is, all of them do support modding cases,
allowing them to only load when specific mods are loaded, if required.

| Feature Type              | Link                                                                                                                                                            |
|---------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Customizable Monster Room | [Link](CustomizableMonsterRoom.md)                                                                                                                              |
| Floating Island                     | [Simple](SimpleFloatingIsland.md) [Advanced](AdvancedFloatingIsland.md)                                                                                         |
| Modded Geode					   | [Link](ModdedGeode.md)                                                                                                                                          |
| Modded Ore                           | [Simple](ModdedOre.md) [Legacy](ModdedLegacyOre.md) [Scattered](ModdedScatteredOre.md) [Ore Vein](OreVein.md) [Noise-Based Generation Ore](NoiseGenBasedOre.md) [Create mod Layered Ore](CreateLayeredOre.md) [Classic Ore Vein](./ModdedClassicOreVein.md) |
| Modded Spring                      | [Link](ModdedSpring.md) |
| Modded Vegetation Patch     | [Link](ModdedVegetationPatch.md) |
| Modded Fallen Tree              | [Link](ModdedFallenTree.md) |

The below JSON data syntax is shared among all modded feature implementations.

Thus, any modded feature that you make use of has also these fields below:


~~~
object(ConfiguredFeature), required
{
	"type": resourcelocation(FeatureType), required
	"config": object(ModdedFeatureConfiguration), required
	{
		"modids": list<string>, optional, can_be_empty, default_value=[]
	}
}
~~~

Fields commonly provided by the modded feature types:

| Field Name                           |  Description                                                                                                                                                      |
|---------------------------------|---------------------------------------------------------------------------------------------------------------------------------| 
| type                                    | Defines the feature type to use. This must be one of the modded feature types provided by the mod.      |
| config                                 | Defines the settings for this configured feature.                                                                                         |
| config.modids                      | Defines a list of strings that do represent mod ID's. If the feature does need a specific mod but is not loaded, it is disabled. All the mods specified in this list must be valid at the specified run time in order for this feature to be used. |


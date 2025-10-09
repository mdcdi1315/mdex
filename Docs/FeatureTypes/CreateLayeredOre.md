

## The `Create` mod layered ore feature


> [!WARNING]
This feature type was first defined in 1.6.0 version of the mod.
If attempted to be used on older mod versions, Minecraft will 
justifiably complain that it cannot find the requested feature type.

This is an ore feature type originally defined in the [`Create`](https://www.curseforge.com/minecraft/mc-mods/create) mod for it's own use. 
However, it is useful when developing the Mining Dimension with that mod as well.

> [!NOTE]
At least at the time of this writing, the mod does provide this feature type and in 1.21.5 environments,
but because the Create mod has not been ported yet for that one, it is useless. Specifically, you cannot reference
any of the Create-defined blocks, items, etc. in 1.21.5, because simply that mod is not released yet for that version.

Note also that this feature provided by the Mining Dimension: EX mod is also a very optimized variant of that 
one and has less memory footprint than the original one.

Feature type syntax:

~~~
object(ConfiguredFeature), required, since_mod_version="1.6.0"
{
	"type": resourcelocation(FeatureType), must_have_value="mdex:create_layered_ore"
	"config": object(CreateLayeredOreFeatureConfiguration), required 
	{
		"discard_chance_on_air_exposure": float , range=[0 , 1] , required
		"size": int, range=[0, 48]
		"layer_patterns": list<LayerPattern>, required, can_be_empty
	}
}
~~~

| Field Name                                                               |  Description                                                                                                                                                                                                                                                           |
|------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------| 
| type                                                                        | Defines the type of this feature. This must always be the value `mdex:create_layered_ore`.                                                                                                                         |
| config                                                                     | Defines the configuration object for this feature object.                                                                                                                                                                                  |
| config.size                                                              | Defines the XYZ size of this feature. Larger values make this feature generally larger.                                                                                                                               |
| config.discard_chance_on_air_exposure           | Defines a probability value whether exposed-on-air ore blocks should be discarded. Setting this to 0 does always keep them and setting it to 1 always discard them.   |
| config.layer_patterns                                           | Defines a list of different layer patterns to randomly select from during generation stage.                                                                                                                         |


Where a layer pattern object is another list of layers. 
A single layer object is described below.

~~~
object(Layer), required, since_mod_version="1.6.0"
{
	"targets": list<list<object(SingleTargetBlockState)>>, required, can_be_empty
	"min_size": int, range=[0, 32767], required
	"max_size": int, range=[0, 32767], required
	"weight": <Weight>, required
}
~~~

| Field Name                                                               |  Description                                                                                                                                                                                                                                                                                |
|------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------| 
| targets                                                                   | Defines different lists of [ore target states](../SingleTargetBlockState.md) to select from. The list that will be used for generation is randomly selected during generation stage. |
| min_size                                                                  | Defines the minimum size of this layer. A random value between this and `max_size` field is selected.                                                                                                                                    |
| max_size                                                                 | Defines the maximum size of this layer. A random value between this and `min_size` field is selected.                                                                                                                                   |
| weight                                                                      | Defines a value that specifies it's selection proportionally to other entries in the list.                                                                                                                                                       |

> [!WARNING]
Although all list fields defined in this feature configuration can be empty, empty lists will cause it to fail compilation.
Additionally, compilation will fail for this feature type if all the entries that are defined to it are invalid.










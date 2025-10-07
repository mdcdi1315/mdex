

## Randomize input position placement modifier.

This placement modifier is able to fully randomize the input block position by all the X, Y and Z axes of the world.
It does that by offsetting the input position to the computed random values.

All these can be defined at developer's will, as well as how the random values will be calculated.

Additionally, you can specify how many block positions to return as well. 
You can not only return one random position, but also multiple ones.

This number of positions is also very configurable over how it's random value is generated.


Placement Modifier syntax:

~~~
object(RandomizeInputPositionPlacementModifier), required, since_mod_version="1.5.0"
{
	"type": resourcelocation(PlacementModifier), required, must_have_value="mdex:randomize_input_position"
	"x_offset": <IntProvider>, required, range=[-32, 32]
	"y_offset": <IntProvider>, required, range=[-32, 32]
	"z_offset": <IntProvider>, required, range=[-32, 32]
	"positions_count": <IntProvider>, required, range=[0, 26]
}
~~~


| Field Name             | Description                                                                                                                                                                         |
|-----------------------|----------------------------------------------------------------------------------------------------------------------------------------------|
| type                      | Defines the type of the placement modifier to use. This must always be the value `mdex:randomize_input_position`. |
| x_offset               | Defines the random offset to apply for the X axis. This is an [integer provider](https://minecraft.wiki/w/Template:Nbt_inherit/int_provider/template). |
| y_offset               | Defines the random offset to apply for the Y axis. This is an [integer provider](https://minecraft.wiki/w/Template:Nbt_inherit/int_provider/template). |
| z_offset               | Defines the random offset to apply for the Z axis. This is an [integer provider](https://minecraft.wiki/w/Template:Nbt_inherit/int_provider/template). |
| positions_count    | Defines the number of random positions to return. This is an [integer provider](https://minecraft.wiki/w/Template:Nbt_inherit/int_provider/template). |


If in case that you have not noticed it, the name for referencing this placement modifier is `mdex:randomize_input_position`.
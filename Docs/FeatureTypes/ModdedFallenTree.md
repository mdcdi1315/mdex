

## The modded fallen tree feature.

> [!WARNING]
This feature type was first publisized in 1.6.0 version of the mod.
Although existing in older versions, it was not considered one of 
the mod's shipped features. As such, using it in older versions
neither will have the same syntax defined here, 
nor it is garanteed that will work as expected.
`mdcdi1315` will not take any responsibility about issues 
aroused from such older versions, nor will perform a fix 
for those older versions.

Why this feature is defined in first place?

When I started to think out how I should develop the mod,
I needed for the datapack the fallen tree feature for older versions
due to a biome defined in the `Default MD Experience Pack`.

Thus, I incorporated it initially in the mod, but left it to develop other
features more important to me first. Now, I returned back to it and 
finally added the features I wanted to.

How is this related to the 1.21.5 fallen tree feature?

Just it's inspiration is taken. I knew fallen tree features eariler from Bedrock versions.

The code it uses it is written from the ground up by me. No Minecraft code was used for this.

### Using this feature type

You could use this feature type when you have an underground tree biome.

The feature type is written with that way so that when a single Y change happens, the trunk does not continue along.

Additionally, it does perform itself a check whether the placement position is valid surface.

A vegetation feature is also supported for it. The position that that feature recieves is on the top of the half of the trunk size.

Feature type syntax:

~~~
object(ConfiguredFeature), required, since_mod_version="1.6.0"
{
	"type": resourcelocation(FeatureType), must_have_value="mdex:fallen_tree"
	"config": object(FallenTreeFeatureConfiguration), required 
	{
		"log_block": resourcelocation(Block), required
		"fallen_logs_count": <IntProvider>, required, range=[2 , 18]
		"vegetation_patch": <PlacedFeature>, optional, default_value=null
		"vegetation_patch_placement_probability": optional, range=[0, 1], default_value=1
	}
}
~~~

| Field Name                                                               |  Description                                                                                                                                                                                                                                                           |
|------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------| 
| type                                                                        | Defines the type of this feature. This must always be the value `mdex:fallen_tree`.                                                                                                                                         |
| config                                                                     | Defines the configuration object for this feature object.                                                                                                                                                                                  |
| config.log_block                                                    | Defines the block to use as the trunk for all the trunks placed by this feature configuration object. It is a Minecraft resource location.                                                 |
| config.fallen_logs_count                                      | Defines the number of fallen logs to place down. This value does not account the root trunk, just the other fallen logs after it. It is an [integer provider](https://minecraft.wiki/w/Template:Nbt_inherit/int_provider/template). |
| config.vegetation_patch                                       | Defines the vegetation patch to apply above the fallen logs. It's placement is controlled from whether the half of the requested log blocks were placed, and whether the probability test passes. |
| config.vegetation_patch_placement_probability | Defines a probability value that finally places the vegetation patch feature. A value of 0 never places the feature, and a value of 1 always attempts to place it.          | 




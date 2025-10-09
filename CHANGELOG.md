Now releasing 1.6.0:

-> Added one new feature type, the Create mod layered ore feature. This is useful for referencing it without the Create mod is necessarily installed in your instance.

-> The biome spawn additions object can now either accept a single spawner object or multiple ones wrapped into a list. 
Compatibility with 1.5.0 version of the mod is thus maintained.

-> The fallen tree feature is now officially published (after last changes were done).

Bugfixes and performance optimizations were done, such as:

-> Fixed an issue with the placement of the central room of the customizable mineshaft structure. 
The room should now work as it is expected.

-> The mod's commands should now be able to be created and deleted each time a Minecraft world is created.
This allows for reduced memory footprint while on the title screen.

-> All the features of the mod that were using the SingleTargetBlockState class as a list, 
they are now replaced with a properly allocated and persistent codec. 
That codec is now the only instance that exists as a list codec.

-> Optimized the FeaturePlacementUtils.SafeSetBlock method.

-> The publicly available constants provided through Extensions class could be modified externally and that could be a security threat. It is properly addressed now.

-> Some textures that were still using 64 bits per pixel are now scaled down to 32 bits for textures needing alpha channel and 24 bits for those that do not need alpha channel. 
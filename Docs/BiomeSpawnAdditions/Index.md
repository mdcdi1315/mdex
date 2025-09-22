

## The biome spawn additions registry 

The mod does also provide a custom datapack registry to register additional mob spawns to be done during world loading.

These mob spawn data are COMPLETELY OPTIONAL, that means that they are conditionally loaded based on which mods are loaded into the Minecraft instance.

Entries containing mods that were not found are excluded to be appended to the biomes, thus you can use this registry to selectively load additional entities to be spawned.

The files containing the spawn additions should be located under the `worldgen/mdex_biome_spawn_additions` folder of your pack.

Note that these additions are cleared out once these have been registered to the world instance for as less memory footprint as possible.

You can use these to register additional mob spawns at the Mining Dimension as mods are added to the Minecraft instance to complicate the gameplay.

For more information how such an object is defined in JSON see [this document](BiomeSpawnAdditionObject.md).


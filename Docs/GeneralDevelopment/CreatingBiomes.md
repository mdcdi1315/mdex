
## Creating Mining Dimension biomes

This article describes what to note about creating Mining Dimension biomes - and how you should use them.

### General insight

Biomes are definitions that Minecraft uses to define different interactions in different areas.

For example, Lush Caves surface is covered with moss and clay, and it is also for home for axolotls.

However, Dripstone Caves has dripstone rock formations in the cave surfaces.

Apart from that, each biome defines different hostile mobs to spawn as well as which features to generate.

For example, the Cave Vines Plant is a Lush Caves specific feature.

Biome files are placed on the `biome` subfolder of the `worldgen` subfolder of your pack namespace.

Their JSON syntax can be found [here](https://minecraft.wiki/w/Biome_definition).

### Additional notes about Mining Dimension Biomes

> [!WARNING]
Any new biome you specify for the Mining Dimension it should be also declared in the `mdcdi1315_md:mining_dimension_biomes` tag. <br />
This tag is used by the mod to identify the mining dimension biomes and to apply to them some proper settings making these suitable to be finally generated in the mining dimension biome.

> [!NOTE]
For your biome to generate, you must appropriately declare it in the overridden dimension file you earlier defined.

> [!CAUTION]
In any case, you should also make use of the `mdcdi1315_md:md_default_biome` provided with the mod. <br />
It is called 'default biome' because this will be the biome that you will make it to be non-specific,
and it will be used by Minecraft to fill wherever all the other biomes you specify have failed to be placed. <br />
You should also override this biome to provide the default features and ores that you want to be found by other players.




## Mining Dimension: EX shipped tags.

The mod does provide and some block and biome tags for declaring capabilities to the mod.

The mod uses then these tags to apply the desired behavior.

### The `mdcdi1315_md:hardstone_ore_replaceables` tag

This block tag is filled in by the mod and provides the hardstone blocks that can be replaced with their ore variants.

Useful for when defining your custom ore feature definitions.

### The `mdcdi1315_md:md_infiniburn_blocks` tag

This block tag defines the blocks where fire does never burn out.

You usually override this to define additional blocks that you want to never burn out when a fire has been spawned on them.

### The `mdcdi1315_md:md_stone_blocks` tag

This block tag defines the default stone blocks used to comprise the Mining Dimension default stone blocks.

You both use it and override it.

For the first case, to match these blocks for ore features and the second to provide additional stone blocks that you will generate in the dimension.

### The `mdcdi1315_md:md_deepslate_blocks` tag

This block tag default the default deepslate stone blocks used to comprise the Mining Dimension default deepslate blocks.

You both use it and override it.

For the first case, to match these blocks for ore features and the second to provide additional deepslate stone blocks that you will generate in the dimension.

### The `mdcdi1315_md:md_cave_stone_blocks` tag

This block tag combines the above two tags in order to use it for generically generate features where you are not interested about which stone type is used.

### The `mdcdi1315_md:needs_copper_tool` tag

This block tag is defined so that it could be overridden by you to define blocks that need the copper tier tools provided by the mod in order to break properly. Better tier tools do also pass this check.

### The `mdcdi1315_md:mining_dimension_biomes` tag

This biome tag is overridden by you to define which biomes will be generated in the Mining Dimension.

You should always override this tag to provide your own biomes that will be generated in the dimension.

Do not forget to include ALL the biomes you define here!!!

### Where these tags are located?

The block tags described above are located in the `blocks` subfolder (`block` for 1.21.1 and later) which is contained in the `tags` subfolder of the `mdcdi1315_md` namespace. 

The biome tags respectively are into the `biome` subfolder of the `worldgen` subfolder of the `tags` subfolder of the `mdcdi1315_md` namespace.



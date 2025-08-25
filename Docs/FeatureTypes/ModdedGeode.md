

### Modded Geode feature type

This feature type has the identical fields as the `minecraft:geode` one,
without any major changes on how it works, rather than performance 
optimizations and fixes the bug where the `invalid_blocks` field was not working.

The only thing that you should know during 
development is that you need to replace all the 
state provider types with ID `minecraft` to `mdex`.
This is done so that the feature can work correctly.

This feature type can be referenced with the resource location `mdex:modded_geode`.

More info and documentation is located [here](https://minecraft.wiki/w/Amethyst_geode).
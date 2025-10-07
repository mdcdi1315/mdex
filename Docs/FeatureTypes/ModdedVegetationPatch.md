

## The Modded Vegetation Patch feature.

This feature type works exactly as Minecraft's vegetation patch feature type,
except that it does some performance optimizations and it is a modded feature.

Additionally, block state providers defined for the ground state for 
this feature type must be defined with the [mod-provided block state providers](../AbstractBlockStateProvider.md) instead.

The feature type can be referenced with the name `mdex:modded_vegetation_patch`.

Details on how configuring such a feature can be found [here](https://minecraft.wiki/w/Vegetation_patch).

There is also provided the waterlogged patch feature variant of this one, called `mdex:modded_waterlogged_vegetation_patch`.
It has an identical configuration to the normal one, 
just the behavior changes in that all placed blocks will become waterlogged instead.


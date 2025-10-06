Now releasing 1.5.0:

-> Added two new modded feature types, the Modded Spring and the Modded Classic ore feature.
-> The biome spawn additions subsystem had a code re-write allowing for more simple, flexible and consistent definitions.
-> Added additional debugging options in the config. 

Bugfixes and performance optimizations were done, such as:

-> Weight utilities defined only for the 1.21.5 flavor of the mod is now backported.

-> The custom placement modifiers were not effectively using the Distributed Compilation Object logic. Now they do, and they are now considered as a public and shipped feature of the mod.

-> Simplified the process how the Block State Providers registry codec is loaded.

-> Some recipes were not unlocked to the player as appropriate, now they do.

-> Fixed some texture glitches and storage, especially for the tools.

-> Cleaned the code for how the biome spawn additions subsystem works.

-> Performance fixes for Modded Vegetation Patch feature types
### Now releasing 1.7.0:

-> Mostly a release doing performance optimizations.

-> The classic ore vein feature is now officially published after testing and patching was done.

-> The teleporting manager handling code is re-written. Because the changes are too many, see the additional comment below.

-> Loot table helpers are now considered stable features. They will be documented soon.

#### Bugfixes and performance optimizations were done, such as:

-> Reduced code size where possible. Some simple 'if' statements were converted to one-line statements using the ? and : operators.

-> A list codec abstraction is now provided by the mod. This allows it to better assure compatibility between versions.

-> A field in the BiomeEntitySpawnEntry class was not cleaned up after disposal. Now, it is.

-> Fixed an issue where noise-based generation ore features were using invalid positions for generating.

-> Fixed an issue where block solidness used heavily by the feature types was not determined in the correct manner.

-> Due to the changes done to the Teleporting Manager, it is now less error-prone.

#### Changes to the Teleporting Manager

The teleporting manager internals were completely changed. 
As such, the entire teleportation process has been changed.

Change #1: The saved teleporter data:

The association over how players are associated with the teleporters has been dramatically overhauled.

The saved teleporter data now holds for each player the following data:

- The teleporter position that the player used
- The exact position of the player of the dimension before he/she uses the teleporter to travel to another dimension.
- The dimension that the player travelled from. The destination dimension is that from where the saved data is located to.
- The player's camera rotation details of the source dimension

This assures mostly three things:

- Any other dimension except the Mining Dimension can now be used to enter the Mining Dimension.

- It is guaranteed that the player will always return to the dimension that he used to enter the Mining Dimension.

- The exact player position is now retained as well as where he was looking at. This allows for fewer disruptions while using the teleporter.

Change #2: The teleporting process:

Due to the above changes, the teleporting workflow has become overhauled, less error-prone and always assures of correct destinations.

So the players are now guaranteed to:

- Not lose the exact teleporting position under race conditions. Since the teleporter position is now appropriately used,
the manager uses that to actually prove that there is a teleporter. If not, it will generate the teleporter feature again.

- They always return from where they came from.

- Teleportation failures are now more difficult to happen.

Change #3: Teleporting manager extensibility:

There was work done so that in the version 2 this class will be able to be used by other mods with their own teleporter blocks as well.

Change #4: Controllable teleportation:

A mod configuration option was now added to explicitly control whether the players should be transferred
to the Mining Dimension at all. This allows server administrators to stop allowing to go to the Mining Dimension
for some time, or they disable it for various reasons.

However, any players still on the Mining Dimension after this option is enabled will remain there, 
but they will be able to return back to their home dimension.

To additionally alleviate this, a new 'evacuate all players' command is provided, which does 
teleport all the Mining Dimension players to the specified dimension immediately.

If the players have teleporting data for the target dimension, the command will appropriately use them.


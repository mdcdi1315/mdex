

### Overview of the Aggressive Spawner registry.

> [!WARNING]
This series of documentation are referring to the Aggressive Spawner Registry,
which is a feature added to the **2.2.3** version of the mod. If used on versions before 
that, the entries won't be even recognized by the mod.

The Aggressive Spawner registry provides a way to aggressively spawn mobs in the Mining Dimension. 
This was added to overcome Minecraft spawn cap limits and increase difficulty while in the Mining Dimension.

> [!NOTE]
This subsystem does not replace the Biome Spawn Additions registry; that feature will remain fully supported.

Additionally, it allows conditional loading of it's entries just like the Biome 
Spawn Additions registry, respecting the mods loaded in a Minecraft instance.

This subsystem works in a simple manner, that does remove mob cap limits.
Note that there is still an implicit mob cap amount but that is rather
to maintain the dimension playable, in terms of bandwidth.

This subsystem is defined on the Minecraft's custom spawner logic, and as such it works with tick time.

Spawn process:

- Check the tick time and see whether the time matches one of the aggressiveness level 
constants. If that stands true, continue executing spawn logic. Otherwise return.

- Iterate through all the players in the Mining Dimension.

- For each of the spawn entries defined in that aggressiveness level, try to spawn them.
To do that, a mob spawn entry is randomly picked from the list.

- If applicable, spawn the mob in a random position around the player's area.
If that succeeds, the algorithm proceeds to try to spawn a pack of the mob.
Then, it does complete execution.

- If no mobs could be spawned in the dimension, the spawner tries to spawn 
mobs of the same type where the player looks at. Then, the above step is repeated.

- Complete execution.

#### Specifying entries for the Aggressive Spawner Registry

Entries of Aggressive Spawners are specified by a registry
exported to datapacks, under the `worldgen/mdex_aggressive_spawners` folder
of a given data pack.

It's format is described [in this document](AggressiveSpawnerEntry.md).



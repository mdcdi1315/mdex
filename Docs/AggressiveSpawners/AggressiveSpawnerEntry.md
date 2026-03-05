
### An entry of an aggressive spawner.


Aggressive Spawner Entries are specified by the following syntax:

~~~
object(AggressiveSpawner), required, since_mod_version="2.2.3"
{
	"mod_ids": list<string>, optional, can_be_empty, default_value=[]
	"biomes": either<object(TagKey<Block>), list<resourcelocation(Biome)>>, required
	"spawn_entries": list<object(AggressiveSpawnerEntryList)>, required
}
~~~

Fields:

| Field Name | Description                                                                                                                                                                                                                                                                                               |
|------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| biomes   | Specifies the biome ID(s) where this aggressive spawner will be applied to. The biome(s) must exist. If a biome tag, all the biomes specified in that tag are used by the object.                            |
| mod_ids     | Defines a list of strings that represent mod ID's required in order this object entity entries can be applied to the specified biome.                                                                                             |
| spawners   | Defines a list of objects for spawn entry objects. The spawn entry list object syntax is described below.                                                                                                                                     |

Where an object of type `AggressiveSpawnerEntryList` is described as:

~~~
object(AggressiveSpawnerEntryList), required, since_mod_version="2.2.3"
{
	"category": object(MobCategory), required
	"difficulty": object(AggressivenessLevel), required
	"spawners": list<object(AggressiveSpawnerEntry)>, required
}
~~~

Fields:

| Field Name  | Description                                                                                                                                                                                                                                                                                                                                                                           |
|---------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| category   | Defines the mob spawn category that this aggressive spawner object will be applied as. This value must be one of `monster`, `creature`, `ambient`, `water_creature`, `underground_water_creature`, `water_ambient`, `misc`, or `axolotls`. |
| difficulty    | Defines the aggressiveness level that specifies when the `spawners` field is applied over time.  This value must be one of `LOW`, `MEDIUM`, `HIGH`, `VERY_HIGH`, and `BOSS`.                                                                                                            |
| spawners  | Defines the individual mob entries to be aggressively spawned.                                                                                                                                                                                                                                                                                        |

Where an object of type `AggressiveSpawnerEntry` is described as:

~~~
object(AggressiveSpawnerEntry), required, since_mod_version="2.2.3"
{
	"weight": int, required
	"entity": object(EntityType), required
	"min_inclusive": int, required, range=[1, 127]
	"max_inclusive": int, required, range=[1, 127]
}
~~~

Fields:

| Field Name       | Description                                                                                                                                                                                                                                                                                                                                                                           |
|------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| weight            | A value that specifies the likeness of selecting this entry instead of other entries being on the same list. Can be a zero or positive value. If zero, the entry will be never selected.                                                                                          | 
| entity             | A namespaced resource location ID that specifies the mob to spawn in the world.  Example: `minecraft:enderman`.                                                                                                                                                                                                        |
| min_inclusive | The minimum number of mobs of the selected type to spawn as a pack, if possible. Must be a positive value with upper bound the number 127.                                                                                                                                                             |
| max_inclusive| The maximum number of mobs of the selected type to spawn as a pack, if possible. Must be a positive value with upper bound the number 127, and must be equal to or greater from the value of the `min_inclusive` field.                                |


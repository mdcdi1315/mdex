

### A single Biome Spawn addition object

> [!NOTE]
The syntax of biome spawn addition objects has been changed in 1.5.0 with a better one. 
For older mod versions documentation, you can see [this file](./BiomeSpawnAdditionObject-old.md) instead.

The syntax for a simple Biome Spawn addition object (from mod version 1.5.0 and later) is this:

~~~
object(BiomeSpawnAdditions), required, since_mod_version="1.5.0"
{
    "biomes": either<object(TagKey<Block>), list<resourcelocation(Biome)>>, required
    "modids": list<string>, optional, can_be_empty, default_value=[]
    "spawners": either<<BiomeEntitySpawnList>, list<BiomeEntitySpawnList>>, required, can_be_empty
}
~~~

Fields:

| Field Name | Description                                                                                                                                                                                                                            |
|------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| biomes   | Specifies the biome ID(s) where this biome spawn addition will be applied to. The biome(s) must exist. If a biome tag, all the biomes specified in that tag are used by the object.                                                                                                                                       |
| modids     | Defines a list of strings that represent mod ID's required in order this object entity entries can be applied to the specified biome.                                                                                                  |
| spawners   | Defines the object or a list of objects for spawn entry objects. The spawn entry list object syntax is described below. Note that the list of objects can be only used in 1.6.0 and later mod versions. |


Syntax for a biome spawn entry object list:

~~~
object(BiomeEntitySpawnList), required, since_mod_version="1.5.0"
{
    "category": object(MobCategory), required
    "entities": list<BiomeSpawnEntity>, required, can_be_empty
}
~~~

| Field Name  | Description                                                                                                                                      |
|---------------|--------------------------------------------------------------------------------------------------------------------------------------------------|
| category   | Defines the mob spawn category that this biome spawn additions object will modify. This value must be one of `monster`, `creature`, `ambient`, `water_creature`, `underground_water_creature`, `water_ambient`, `misc`, or `axolotls`. |
| entities       | Defines a list of biome spawn addition entity entries to apply to the specified biome(s). |

Syntax for a biome spawn addition entity entry:

~~~
object(BiomeSpawnEntity) , required, since_mod_version="1.5.0"
{
    "entity_id": resourcelocation(EntityType), required
    "weight": <Weight>, required, range=[0 , 2147483647]
    "min_count": int, required, range=[1 , 127]
    "max_count": int, required, range=[1 , 127]
    "spawn_costs": <SpawnCost>, optional, default_value={ "energy_budget": 0 , "charge": 0 }
    {
        "energy_budget": double, required
        "charge": double, required
    }
}
~~~

Fields:

| Field Name                                | Description                                                                                                                                      |
|-------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------|
| entity_id                                  | Defines the entity type that this entry will spawn.                                                                                              |
| weight                                      | Defines how often this entry will be selected, proportional to other entries in this file and to the entries defined in the original biome file. |
| min_count                                | Defines the minimum count of mobs to spawn in a pack. Must be greater than 0.                                                                    |
| max_count                               | Defines the maximum count of mobs to spawn in a pack. Must be greater than 0. And must be not less than `min_count`.                             |
| spawn_costs                           | Defines the spawning costs for the current entity. Optional.                                                                                                                         |
| spawn_costs.energy_budget | The entity's maximum potential.                                                                                                                       |
| spawn_costs.charge              |  The entity's charge.                                                                                                                                    |


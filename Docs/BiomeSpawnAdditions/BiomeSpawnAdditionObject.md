

### A single Biome Spawn addition object

The syntax for a simple Biome Spawn addition object is this:

~~~
object(BiomeSpawnAdditions), required
{
    "biome_id": resourcelocation(Biome), required
    "modids": list<string>, optional, can_be_empty, default_value=[]
    "category": object(MobCategory), required
    "spawners": list<<BiomeSpawnEntity>>, required, can_be_empty
}
~~~

Fields:

| Field Name | Description                                                                                                                                                                                                                            |
|------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| biome_id   | Specifies the biome ID where this biome spawn addition will be applied to. The biome must exist.                                                                                                                                       |
| modids     | Defines a list of strings that represent mod ID's required in order this object entity entries can be applied to the specified biome.                                                                                                  |
| category   | Defines the mob spawn category that this biome spawn additions object will modify. This value must be one of `monster`, `creature`, `ambient`, `water_creature`, `underground_water_creature`, `water_ambient`, `misc`, or `axolotls`. |
| spawners   | Defines an array of spawn entry objects. The spawn entry object syntax is described below.                                                                                                                                             |


Syntax for a biome spawn addition entity entry:

~~~
object(BiomeSpawnEntity) , required
{
    "id": resourcelocation(EntityType), required
    "weight": <Weight>, required, range=[0 , 2147483647]
    "min_count": int, required, range=[0 , 2147483647]
    "max_count": int, required, range=[0 , 2147483647]
}
~~~

Fields:

| Field Name | Description                                                                                                                                      |
|------------|--------------------------------------------------------------------------------------------------------------------------------------------------|
| id         | Defines the entity type that this entry will spawn.                                                                                              |
| weight     | Defines how often this entry will be selected, proportional to other entries in this file and to the entries defined in the original biome file. |
| min_count  | Defines the minimum count of mobs to spawn in a pack. Must be greater than 0.                                                                    |
| max_count  | Defines the maximum count of mobs to spawn in a pack. Must be greater than 0. And must be not less than `min_count`.                             |


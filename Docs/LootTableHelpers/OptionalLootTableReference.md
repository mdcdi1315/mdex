

## The optional loot table reference container entry type.

This is an loot table container entry whose `type` field is `mdex:optional_loot_table_reference`.

It can be used for loading loot tables from mods and other sources as well.

Container entry type syntax:
~~~
object(BaseSingletonLootPoolEntryContainer), required, since_mod_version="1.5.0"
{
	"type": resourcelocation(LootPoolEntryContainer), must_have_value="mdex:optional_loot_table_reference"
	"weight": int, range=[0, 2147483647], optional, default_value=1
	"quality": int, range=[0, 2147483647], optional, default_value=0
	"function": list<object(LootItemFunction)>, optional, can_be_empty, default_value=[]
	"name": resourcelocation(LootTable), required
}
~~~

| Field                           | Description                                                                                                                                                                                                                                |
|---------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| type                           | Defines the type of this loot pool entry container. This must always be the value `mdex:optional_loot_table_reference`.                                                 |
| weight                         | Defines a value how this loot pool entry container is selected proportionally to other containers in the loot table object. Optional and defaults to 1. |
| quality                        | Defines the quality of this loot pool entry container. Usually, containers with higher qualities usually hold better loot.  Optional and defaults to 0.      |
| function                     | Defines an array of functions to further process the container's entries. Optional.                                                                                                          | 
| name                           | Defines the name of the loot table to reference. If the loot table does not exist, this loot container does return nothing.                                             |


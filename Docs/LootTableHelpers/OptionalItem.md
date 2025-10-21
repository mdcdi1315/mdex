
## The optional item container entry type.

This container entry type defines items that may not be present 
during the game's lifetime.

Instead failing the loot table with an error this type will log a warning and will return 
nothing when the requested item is not found.

Container entry type syntax:
~~~
object(BaseSingletonLootPoolEntryContainer), required, since_mod_version="1.5.0"
{
	"type": resourcelocation(LootPoolEntryContainer), must_have_value="mdex:optional_item"
	"weight": int, range=[0, 2147483647], optional, default_value=1
	"quality": int, range=[0, 2147483647], optional, default_value=0
	"function": list<object(LootItemFunction)>, optional, can_be_empty, default_value=[]
	"item": resourcelocation(Item), required
}
~~~

| Field                            | Description                                                                                                                                                                                                                                |
|---------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| type                            | Defines the type of this loot pool entry container. This must always be the value `mdex:optional_item`.                                                 |
| weight                         | Defines a value how this loot pool entry container is selected proportionally to other containers in the loot table object. Optional and defaults to 1. |
| quality                        | Defines the quality of this loot pool entry container. Usually, containers with higher qualities usually hold better loot.  Optional and defaults to 0.      |
| function                     | Defines an array of functions to further process the container's entries. Optional.                                                                                                          | 
| item                             | Defines the name of the item to append to the loot table pool. If it does not exist, this container will return an empty item stack.                                     |



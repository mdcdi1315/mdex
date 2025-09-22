

### Specific loot appender processor.

This structure processor is conveniently used to append specific loot to your structure template chests. 

It may be desirable sometimes to do that instead of injecting on your own the loot items directly because 
these items may come from a mod that is not loaded. 

To avoid crashes due to hard dependencies , you can use this structure processor instead to do that work only when the mods you require are loaded.

Processor syntax:

~~~
object(AbstractStructureProcessor), required
{
    "processor_type": resourcelocation(StructureProcessorType), must_have_value="mdex:specific_loot_appender"
    "modids": list<string>, optional, can_be_empty, default_value=[]
    "containerid": resourcelocation(Block), required
    "stacks": list<<ItemStackChestPlacement>>, required, can_be_empty 
    "probability": float, range=[0 , 1], optional, default_value=1
}
~~~

| Field Name     | Description                                                                                                                                                                        |
|----------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| processor_type | Defines the type of structure processor to select. This must always be the value `mdex:specific_loot_appender`.                                                                    |
| modids         | Defines a list of strings that represent mod ID's required in order this structure processor can generate the specific loot.                                                       |  
| containerid    | Defines the block ID that is a container that you wish to be appended the specific loot.                                                                                           |
| stacks         | Defines an array of item stacks and defines settings for how and when these should be placed in the container.                                                                     |
| probability    | Optional. Defines an additional random probability to control item stack appending by a random value. A value of 1 always appends the item stacks to the found container position. |

Where an Item Stack Chest Placement object is this:

~~~
object(ItemStackChestPlacement), required
{
    "stack": object(ItemStack), required
    "index": int , required, range=[0 , 2147483647]
    "probability": float , range=[0 , 1] , optional, default_value=1
}
~~~

| Field Name     | Description                                                                                                                                                                                                               |
|----------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| stack          | Defines the item to be placed at the chest, as well as how many items must exist in that container slot.                                                                                                                  |
| index          | Defines a container index representing the exact slot where the specified item stack defined in `stack` field will be placed to. Will log a warning if the specified index is outside of the container slot count bounds. |
| probability    | Defines an additional probability for finally selecting the specified item stack to be placed at `index`. Can be used for also randomizing the results.                                                                   |





## The customizable mineshaft structure type.

This type is a special variant of the hardcoded `minecraft:mineshaft` 
structure type in that all the blocks used to generate it (except the chains) can be of your selection.

Additionally, the loot tables found in minecarts in the structure can also be one specified by you,
if you wish so.

> [!CAUTION]
The structure type was first defined in version 1.3.0 of the mod. Earlier versions of it do not have this feature described below,
and if attempted to be used in an older one, Minecraft will justifiably complain that it cannot find the structure type. 

Structure type syntax:

~~~
object(AbstractStructure), required, since_mod_version="1.3.0"
{
    "type": resourcelocation(AbstractStructureType), must_have_value="mdex:customizable_mineshaft"
    "modids": list<string>, optional, can_be_empty, default_value=[]
    "planks_state": object(CompilableBlockState), required
    "fence_state": object(CompilableBlockState), required
    "torch_state": object(CompilableBlockState), required
    "wood_state": object(CompilableBlockState), required
}
~~~

> [!NOTE]
Structure types do inherently get and other fields too, common for all structure types. Those fields are documented [here](https://minecraft.wiki/w/Structure_definition#JSON_format).

Fields:

| Field Name   | Description                                                                                                                                                         |
|--------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| type         | Defines the type of structure to select. This must always be the value `mdex:customizable_mineshaft`.                                                               |
| modids       | Defines a list of strings that represent mod ID's required in order this structure can generate. You may have defined modded blocks to make up this structure type. |
| planks_state | Defines a block state to use for defining the planks used for shafts and bridges.                                                                                   |
| fence_state  | Defines a block state to use for defining the fence used for shafts and bridges.                                                                                    |
| torch_state  | Defines a block state to use for defining the torch that is placed randomly on some shafts.                                                                         |
| wood_state   | Defines a block state to use for bridge columns.                                                                                                                    |



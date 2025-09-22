

### Block phases structure processor

This structure processor could be considered a better alternative to `minecraft:block_age` structure processor 
because it can define the exact block states to match.

You can additionally match and through a list of other block states too, because this structure processor requires to be passed a list of block state providers.

What it does is simple in fact:

For each block in the structure get the block, and test whether the block matches with any block state from the specified providers list.

The block state provider list will be searched up and to the previous block state provider from the last one. 

If there is a match, roll the next block state provider from the list and return its block state.

Thus, in this way you can implement a block age behavior with any block.

You can additionally apply mossiness due to the fact that you can specify block state providers instead.

Structure processor syntax:

~~~
object(AbstractStructureProcessor), required
{
    "type": resourcelocation(StructureProcessorType), must_have_value="mdex:increment_block_phase"
    "modids": list<string>, optional, can_be_empty, default_value=[]
    "states": list<AbstractBlockStateProvider>, required
    "probability": float, range=[0 , 1] , optional, default_value=1
}
~~~

Fields:

| Field Name     | Description                                                                                                                                                                                                                          |
|----------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| processor_type | Defines the type of structure processor to select. This must always be the value `mdex:increment_block_phase`.                                                                                                                       |
| modids         | Defines a list of strings that represent mod ID's required in order this structure processor can perform block phases.                                                                                                               |
| states         | Defines an array of [block state providers](../AbstractBlockStateProvider.md) so that to understand on what blocks block phasing should be done. This array must have at least two elements, or the processor will fail compilation. |
| probability    | Defines a probability value whether the current processor should process or not the specified block position. A value of 1 causes the processing to happen at all the blocks of the structure.                                       |


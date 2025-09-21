
## Mod-defined rule test types

You can also use the following rule test types along with the default ones provided by Minecraft.

They can be useful in a number of cases depending on what you want to do.


### Any matching tag rule test type

This rule test type is similar to the `minecraft:tag_match` rule test type 
that matches whether the current block is in the list of blocks specified by the block tag,
but instead it can pass an array of tags to test instead. 

If the current block is a member of at least one of the specified block tags, the test is considered successfull.

If the current block is not a member of all the specified block tags, the test has failed.

Note that when specifying only one tag entry in the array, it behaves exactly the same as the `minecraft:tag_match` rule test type.

Syntax:

~~~
object(AnyMatchingTagRuleTestType), required
{
    "predicate_type": resourcelocation(RuleTestType), must_have_value="mdex:any_matching_tag"
    "tags": list<object(TagKey<Block>)>, required, can_be_empty
}
~~~

Example: 

~~~JSON
{
	"predicate_type": "mdex:any_matching_tag",
    "tags": [
        "minecraft:base_stone_overworld",
        "mdcdi1315_md:md_deepslate_blocks"
    ]
}
~~~

### Any random block state match rule test type

This rule test type is similar of the `minecraft:random_blockstate_match` rule test type
but instead can specify multiple block states to match. 

If any of the specified block states make a match and the probability test passes too , the test is considered successfull.

Syntax:

~~~
object(RandomBlockStatesMatchRuleTestType), required
{
    "predicate_type": resourcelocation(RuleTestType), must_have_value="mdex:any_random_blockstate_match"
    "random_states": list<object(CompilableTargetBlockState)>, required, can_be_empty
    "probability": float, range=[0 , 1] 
}
~~~

Example:

~~~JSON
{
    "predicate_type": "mdex:any_random_blockstate_match",
    "probability": 0.32,
    "random_states": [
        {
            "Name": "minecraft:deepslate"
        }, 
        {
            "Name": "minecraft:cobbled_deepslate"
        }
    ]
}
~~~


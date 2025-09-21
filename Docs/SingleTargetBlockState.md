

## About the Condition-Guarded placement settings

These settings are used currently only by modded ore features.

These settings are used to selectively transform block states during the generation process.

In the prime example of modded ore features, these settings define which block it can be replaced with another existing block in the dimension.

When such conditions are satisfied the specific block requested for that entry is instead placed on the world and replaces the existing one.

One such entry, thus, it contains the condition and the new block placement data, if test was successfull at that block position. 

Their syntax is as follows:

~~~
object(SingleTargetBlockState), required
{
    "state": object(CompilableTargetBlockState), required
    "target": object(RuleTest), required
}
~~~

Fields: 

| Name   | Purpose                                                                                                    |
|--------|------------------------------------------------------------------------------------------------------------|
| state  | The actual block state to be used if the test defined at the `target` object is successfull.               |
| target | The test to define so that the block defined in `state` object is replaced with the test's matching block. |

Note that the `target` field denotes a structure rule test predicate. For more information about the default rule test types that Minecraft provides, you can see [this article](https://minecraft.wiki/w/Processor_list#Rule_test). 

Additionally, the mod does provide and some additional rule test predicates which can be viewed [here](./ModDefinedRuleTestTypes.md).



### The mod's internals.

This article attempts to describe how the mod is currently written,
and how it intercepts Minecraft to provide it's own datapack utilities.

This is targeted to those interested to learn how the mod works in action.

Acquaintance with the [Java](https://docs.oracle.com/en/java/) programming language and it's concepts are presumed throughout the span of the article.

Additionallly, knowledge about what are the Minecraft registries is required.

#### So, how it is done?

All are starting from abstraction layers - they are important for the mod
as they provide the starting base for all other stuff defined in the mod.

So a considerable amount of time has been devoted to write such useful layers.

The result: faster releases and easier porting between Minecraft versions.

Common utilities are provided for such cases.

All of them are described below.

-> Block Utilities

This layer contains common and useful methods for manipulating Minecraft blocks and block states.

Additionally, it provides manipulation for fluid states as well.

Using this class allows to have the same desired effects on each different Minecraft version.

Finally, it makes the code using these cleaner and self-explanatory.

-> Feature Placement Utilities

This layer contains common and useful methods for working with Minecraft's features subsystem.

Some of the stuff commonly done many times in the Minecraft code are defined through this class.

-> Weight Utilities

This abstracts the logic over how does Minecraft considers weighted calculations in a loot table, for example.

Because this has changed radically between almost all Minecraft versions, the mod does provide it's own
utilities for that, and it does provide a stable framework to reliably work on.

It is also more memory-friendly.

-> Codec Utilities

Minecraft de/serializes everything by using a convenient interface called `Codec`. A `Codec` is responsible over how a single object will be decoded and encoded.

Because exporting functionality to datapacks requires these, I have written a lot 
and useful utilities for that, while focusing on the memory footprint that these do occupy.



#### The base interface for everything: The `Compilable` interface.

For the mod to provide modding case support, a placeholder abstraction had to be created
so that all objects participating to this support do behave the same.

Thus, the result of that was the `Compilable` interface.

It is simplistic, just it defines two methods:

~~~Java
public interface Compilable
{
    void Compile();
    boolean IsCompiled();
}
~~~

I think that is obvious what this interface defines.

From this point and after the Distributed Compilation Object logic is defined.

This interface controls over how compilation is done, and can make known the compilation status to other compilable objects as well.

Almost everything you use from the mod declares, in any way, an implementation of this interface.

For example, modded feature configurations implement the `Compilable` interface, but they have much more functionality from that.


#### The base abstraction layers for common stuff

For the Distributed Compilation Object logic (DCO will be mentioned from now on) 
to actually work, it must intercept the normal process that Minecraft does for creating it's datapack objects somewhere in the object pipeline.

The best way to do this is during the object creation and after all the required fields for the object have been defined.

For example, all the modded feature configurations make a call to the `Compile` method after they declare all their fields required for compilation.

Once `Compile` is called, the transformation (thus, compilation) stage begins. 

In this stage, all fields needing compilation are compiled.
Fields not be able to be compiled must remain unmodified so that DCO logic runs uninterruptibly.

During this stage the following roughly happen for each DCO:

~~~mermaid
flowchart TD

0000["Object Creation (Constructor runs)"] --> 0001

0001["Set all the object fields"] --> 0002

0002[Begin Compilation] --> 0003

0003[Create safe execution environment] --> 0004

0004{Are all compilable fields compiled?} --> |No|0005

0004 --> |Yes|0009

0005[Take next field] --> 0006

0006[Compile field] --> 0007

0007{Is compilation for field successfull?}

0007 --> |Yes|0004

0008[Do error handling] --> 0009

0007 --> |No|0008

0009[Unload safe execution environment] --> 0010

0010[Finish compilation] --> 0011

0011[Object creation completes]

~~~

Note that even if failure with one compilable object happens, the compilation should finish normally,
even if that failed compilation.

However, on failed cases all other fields that are not yet compiled must remain that way.

This allows intercepting objects to appropriately change their behavior to avoid inclusion issues, and saves performance since the failure is already known.

For example, modded features detect when an object has failed compilation. 
If yes, the object is not used for generation (and it is subsequently not generated).
Feature types do also invalidate any compilable fields by assigning to them the `null` value,
for reduced memory usage.

It is also desirable during compilation completion to run additional code for the object,
if that is deemed necessary. Also failure code runs here if the object has failed compilation.

Thus, every declarable object is intercepted with this way. Minecraft does not understand that
since it only sees what it wants to see - an object suitable to be passed where it needs to.

That's why a double interception is required, into the object creation, but also into that object's usage.

When there is an object that has failed compilation, it's usage must not instead trigger an exception but rather silently fail -
for example on feature types the `false` value is returned, on structure types no generation stub is returned,
and structure processors return the current block to process unprocessed.

This is roughly how the mod works in action. 
With that way, modding support is enabled.

#### Performance optimizations done due to DCO presence

There is a performance optimization for failed DCO feature type objects.

During each biome building process, the builder adds the features to generate in a collection of placed features.

Just before building, the mod intercepts that process to detect whether there are any features that are backed
by modded feature configurations, and then if it detects any invalid configurations, they are stripped out from the 
generation of the biome, just like they did never existed on the actual biome file declaration.

This does not however destroy somehow the failed objects; it just ensures that they will not run directly during generation,
where they would possibly consume CPU resources.

They are still needed to be in that state so that other features referencing the failed ones are not generated.

























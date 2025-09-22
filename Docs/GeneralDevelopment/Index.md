

## Creating a data pack for the Mining Dimension: EX mod

This series is a common guide that describes the known exported things that 
data pack developers should override to provide their own Mining Dimension experience.

This document does also generally describe the process of developing such 
a pack, as well as the common pitfalls that must be taken into account.

First and foremost , decide *WHAT* features you want to embed into your own Mining Dimension.

After you have decided that, you can proceed reading.


### Understanding the mod's namespace

The mod exports a custom namespace defining all the customization for the dimension, and it is the name `mdcdi1315_md`.

Here in, all the base data (including the dimension definition itself) are defined.

> [!NOTE]
The mod does also export and another namespace to Minecraft, that is the `mdex` namespace. <br />
Be noted, though, it is FOR INTERNAL USAGE OF THE MOD FOR ITS OWN SUPPORT. <br />
Thus, anything defined there are subject to change without notification by `mdcdi1315`.

### Overriding the dimension file

To begin developing your datapack, you must always override one file: That is, the mining dimension definition.

This file is located in the `dimension` folder and has the name `mining_dim.json`

Thus, you must create a file of the same name under the `mdcdi1315_md` namespace of your pack which it will have a 
subfolder named `dimension`, and that folder will contain a file named `mining_dim.json`.

> [!NOTE]
It is desirable for you to define all your data files in another namespace than `mdcdi1315_md`, and it is recommended to do that.
You just also need to provide that namespace to your data pack so that finally the mod can detect your own dimension definition.

#### Creating an empty mining dimension definition

Although that you have overridden the file, now the hard work starts: The dimension creation.

You first define the type of the dimension. This must be `mdcdi1315_md:mining_dim`.

Thus, in JSON you will have this so far:

~~~JSON
{
    "type": "mdcdi1315_md:mining_dim"
}
~~~

You just defined that the dimension's type will be the Mining Dimension also offered by the mod.

Now, let's define the cave generation for the new dimension.

The mod also offers a cave generation very similar to 1.18+ overworld cave generation with the difference that
there is no surface and that all the dimension from up to down is filled with caves only.

It is however a more optimized variant of the usual generation, thus is faster than the overworld's one.

Here is the definition so far:

~~~JSON
{
    "type": "mdcdi1315_md:mining_dim",
    "generator": {
        "type": "minecraft:noise", 
        "settings": "mdcdi1315_md:md_default_noise_gen"
    }
}
~~~

Notice out the `settings` field. This definition is the mod-provided noise router for the dimension.

> [!NOTE]
You may also override the noise generation if you wish by providing to the `settings` field your own noise settings. <br />
However, this is an advanced task that needs a lot of knowledge about perlin noises and how Minecraft uses them to generate worlds. <br />
Unless you want to create other cave types, do not try to create your own noise settings.

#### Selecting the biomes to generate

Here now the fun part starts! It is time to select which set of biomes you will generate in your mining dimension.

You can also define as well and your own ones. 

If you select one biome only you should write then the following:

~~~JSON
{
    "type": "mdcdi1315_md:mining_dim", 
    "generator": {
        "type": "minecraft:noise", 
        "settings": "mdcdi1315_md:md_default_noise_gen",
        "biome_source": {
            "type": "minecraft:fixed",
            "biome": "YOUR_BIOME_ID"
        }
    }
}
~~~

Where `YOUR_BIOME_ID` a Minecraft resource location of a biome you wish to be generated.

If you want more (which it will be the common case for you) you should write then the following:

~~~JSON
{
    "type": "mdcdi1315_md:mining_dim", 
    "generator": {
        "type": "minecraft:noise", 
        "settings": "mdcdi1315_md:md_default_noise_gen", 
        "biome_source": {
            "type": "minecraft:multi_noise", 
            "biomes": [
                // TODO: Fill the biomes in here. Remove this comment when you are done.
            ]
        }
    }
}
~~~

That was it! You managed to create a valid Mining Dimension data pack up to this point.

For more information how the `minecraft:multi_noise` biome source type is defined and generally for the dimension file you can see [this article](https://minecraft.wiki/w/Dimension_definition).

From now on, individual articles are defined for each aspect of the dimension definition.

Here is provided a table with the articles I do provide:

| Article Entry                       | Link                        | 
|-------------------------------------|-----------------------------|
| Creating biomes                     | [Link](./CreatingBiomes.md) |
| About the provided tags             | [Link](./ProvidedTags.md)   |
| Miscellaneous mod-provided features | [Link](./MiscFeatures.md)   |
| Mod Configuration Options           | [Link](./ConfigOptions.md)  |


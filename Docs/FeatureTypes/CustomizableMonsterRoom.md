
## The customizable monster room feature type

This feature type extends the `minecraft:monster_room` feature 
type in that this one is fully extensible by a datapack only.

It does also define even which loot table to use for the generated chests, if any.

Feature Type Syntax

~~~
object(ConfiguredFeature), required
{
    "type": resourcelocation(FeatureType), must_have_value="mdex:customizable_monster_room"
    "config": object(CustomizableMonsterRoomConfiguration), required
    {
        "modids": list<string>, optional, can_be_empty, default_value=[]
        "spawner_block": resourcelocation(Block), required
        "base_stone_block": resourcelocation(Block), required
        "rare_stone_placement_probability": float, range=[0.0 , 1.0], optional, default_value=0.5
        "reward_chest_placement": object(ChestPlacementConfig) , optional, default_value={ "minecraft:chests/simple_dungeon" , { "type": "minecraft:constant" , "value": 6 } }
        {
            "loot_table_id": resourcelocation(LootTable) , required
            "tries": <IntProvider> , required
        }
        "rare_stone_blocks": list<<WeightedBlockEntry>>, required, can_be_empty
        "additional_spawns": list<<WeightedEntityEntry>>, required, can_be_empty
        "spawner_block_entity_candidates": list<<WeightedCountedEntityEntry>>, required, can_be_empty
    }
}
~~~

Fields:

| Name                                        | Purpose                                                                                                                                                                                                                                    |
|---------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| type                                        | Defines the feature type. Must be set to `mdex:customizable_monster_room`.                                                                                                                                                                 |
| config                                      | Defines the feature's configuration object.                                                                                                                                                                                                |
| config.modids                               | Defines a list of strings that do represent mod ID's. If the feature does need a specific mod but is not loaded, it is disabled. All the mods in this list must be valid at the specified run time in order for this feature to be placed. |
| config.spawner_block                        | The ID of the block to use as a spawner. It's default state is used.                                                                                                                                                                       |
| config.base_stone_block                     | The ID of the block to use as the block that will make the walls of the room. It's default state is used.                                                                                                                                  |
| config.rare_stone_placement_probability     | The probability for a rare block to be placed from the `config.rare_stone_blocks` list instead of the `config.base_stone_block` to be used. A floating point value ranging from 0 to 1 inclusive.                                          |
| config.reward_chest_placement               | An object defining how the placement of reward chests should be done.                                                                                                                                                                      |
| config.reward_chest_placement.loot_table_id | The ID of the loot table that all the reward chests will make use of.                                                                                                                                                                      |
| config.reward_chest_placement.tries         | Number of attempts for placing a chest in the newly generated room. This is an [integer provider](https://minecraft.wiki/w/Template:Nbt_inherit/int_provider/template).                                                                    |
| config.rare_stone_blocks                    | A list of weighted block entries to use as rare block placements, so as to spice up the room's walls and not be only with the `config.base_stone_block` block.                                                                             |
| config.additional_spawns                    | A list of weighted counted entity entries, for doing additional spawns inside the monster room during generation. This happens only once during it's generation. Spawned mobs are persistent during world sessions.                        |
| config.spawner_block_entity_candidates      | A list of entities specifying the possible entities that can be specified into the room's spawner. If no such candidates are specified, the entries are instead built by the contents of the `config.additional_spawns` list.              |




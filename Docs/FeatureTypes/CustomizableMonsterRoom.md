
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
        "stone_block_provider": object(AbstractBlockStateProvider), required
        "reward_chest_placement": object(ChestPlacementConfig) , optional, default_value={ "minecraft:chests/simple_dungeon" , { "type": "minecraft:constant" , "value": 6 } }
        {
            "loot_table_id": resourcelocation(LootTable) , required
            "tries": <IntProvider> , required
        }
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
| config.stone_block_provider                 | A block state provider, providing the block(s) to place for the floor and the walls of the feature.                                                                                                                                        |
| config.reward_chest_placement               | An object defining how the placement of reward chests should be done.                                                                                                                                                                      |
| config.reward_chest_placement.tries         | Number of attempts for placing a chest in the newly generated room. This is an [integer provider](https://minecraft.wiki/w/Template:Nbt_inherit/int_provider/template).                                                                    |
| config.reward_chest_placement.loot_table_id | The ID of the loot table that all the reward chests will make use of.                                                                                                                                                                      |
| config.additional_spawns                    | A list of weighted entity entries, for doing additional spawns inside the monster room during generation. This happens only once during it's generation. Spawned mobs are persistent during world sessions.                                |
| config.spawner_block_entity_candidates      | A list of entities specifying the possible entities that can be specified into the room's spawner. If no such candidates are specified, the entries are instead built by the contents of the `config.additional_spawns` list.              |






### The loot table appender processor.

Assuming that you have a valid structure template that has chests in it and you want to append a specific loot table to them.

With ordinary Minecraft structure processors, appending a loot table for all the chests of a template would be this painful process:

~~~JSON
{
    "processor_type": "minecraft:rule",
    "rules": [	        
        {
            "input_predicate": {
                "block_state": {
                    "Name": "minecraft:chest",
                    "Properties": {
                        "facing": "east"								
                    }
                },
                "predicate_type": "minecraft:blockstate_match"
            },
            "location_predicate": {
                "predicate_type": "minecraft:always_true"
            },
            "output_state": {
                "Name": "minecraft:chest",
                "Properties": {
                    "facing": "east"
                }
            },
            "block_entity_modifier": {
                "type": "minecraft:append_loot",
                "loot_table": "YOUR_LOOT_TABLE_ID"
            }
        },
        {
            "input_predicate": {
                "block_state": {
                    "Name": "minecraft:chest",
                    "Properties": {
                        "facing": "west"
                    }
                },
                "predicate_type": "minecraft:blockstate_match"
            },
            "location_predicate": {
                "predicate_type": "minecraft:always_true"
            },
            "output_state": {
                "Name": "minecraft:chest",
                "Properties": {
                    "facing": "west"
                }
            },
            "block_entity_modifier": {
                "type": "minecraft:append_loot",
                "loot_table": "YOUR_LOOT_TABLE_ID"
            }
        },
        {
            "input_predicate": {
                "block_state": {
                    "Name": "minecraft:chest",
                    "Properties": {
                        "facing": "north"								
                    }
                },
                "predicate_type": "minecraft:blockstate_match"
            },
            "location_predicate": {
                "predicate_type": "minecraft:always_true"
            },
            "output_state": {
                "Name": "minecraft:chest",
                "Properties": {
                    "facing": "north"
                }
            },
            "block_entity_modifier": {
                "type": "minecraft:append_loot",
                "loot_table": "YOUR_LOOT_TABLE_ID"
            }
        },
        {
            "input_predicate": {
                "block_state": {
                    "Name": "minecraft:chest",
                    "Properties": {
                        "facing": "south"								
                    }
                },
                "predicate_type": "minecraft:blockstate_match"
            },
            "location_predicate": {
                "predicate_type": "minecraft:always_true"
            },
            "output_state": {
                "Name": "minecraft:chest",
                "Properties": {
                    "facing": "south"
                }
            },
            "block_entity_modifier": {
                "type": "minecraft:append_loot",
                "loot_table": "YOUR_LOOT_TABLE_ID"
            }
        }
    ]
}
~~~

But now, it can become this:

~~~JSON
{
	"processor_type": "mdex:loot_table_appender",
	"containerid": "minecraft:chest",
	"loot_table": "YOUR_LOOT_TABLE_ID",
	"probability": 1
}
~~~

Thus resulting in a much less bloat, less error-prone code and perfectly does the job for any block state for the container block that the structure may have. 

Structure processor type syntax:

~~~
object(AbstractStructureProcessor), required
{
    "processor_type": resourcelocation(StructureProcessorType), must_have_value="mdex:loot_table_appender"
    "containerid": resourcelocation(Block), required
    "loot_table": resourcelocation(LootTable), required
    "probability": float, range=[0 , 1], required
}
~~~

Fields:

| Field Name      | Description                                                                                                                                                             |
|-----------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| processor_type  | Defines the type of structure processor to select. This must always be the value `mdex:loot_table_appender`.                                                            |
| containerid     | Defines the block ID that is a container that you wish to be appended a loot table.                                                                                     |
| loot_table      | Defines the loot table ID to append to the found container.                                                                                                             |
| probability     | Defines an additional random probability to control loot table appending by a random value. A value of 1 always appends the loot table to the found container position. |


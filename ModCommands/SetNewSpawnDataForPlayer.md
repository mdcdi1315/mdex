

### Set New Spawn Data for player.

This command updates the Spawn Data (Player Logical Data) on 
the Teleporting Manager for a given player, for future use.

> [!NOTE]
This command can be executed *even if* the player which you 
are going to set the data for has not used the Teleporting
Manager ever.

Command Syntax:
~~~
/mdex teleporter_spawn_data set_for_player <dimension> <player> <position>
~~~

| Argument          | Meaning                                                                                                                                                                                                                                                                                                   |
|--------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `dimension`        | The target dimension of the current player. Note that the target dimension must have a valid teleporter entry. If not, the command will fail.                                                                              |
| `player`            | The player to set the Logical data upon.                                                                                                                                                                                                                                              |
| `position`           | The position within the target dimension where the player will be placed to.                                                                                                                                                                                       |





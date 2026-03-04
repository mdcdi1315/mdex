

### Evacuate all players to dimension command.

This command mainly exists to complement the `DisableTeleportations` configuration option first introduced 
in the 1.7.0 version of the mod.

It mainly aids the server administrators to kick all players from the Mining Dimension, but respecting their teleporter spawn data.

For using the command in a appropriate manner, a dimension ID must be specified that ALL the players 
from the Mining Dimension (if any) will be teleported to the specified dimension.

Command Syntax:

~~~
/mdex teleporting_manager evacuate_all_players_to <dimension> 
~~~

| Argument          | Meaning                                                                                                                                                                                                                                                                                                   |
|--------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `dimension`        | The target dimension that is the target dimension that the player(s) will spawn (evacuated) into.                                                                                                                                                  |


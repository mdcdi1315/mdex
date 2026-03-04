

### Teleport player command.

This command teleports a player to/from the Mining Dimension, respecting the Teleporting Manager logical data.

Additionally, this command directly submits to the Teleporting Manager the request, for cases where the data of
the player saved by the mod are required for spawning him in the intended location.

Command Syntax:

~~~
/mdex teleporting_manager teleport <dimension> [player]
~~~

| Argument          | Meaning                                                                                                                                                                                                                                                                                                   |
|--------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `dimension`        | The target dimension that is the target dimension that the player will spawn into.                                                                                                                                                                             |
| `player`            | The player to execute the command on. Optional, if the player is an operator and is the one who executes it. As such, in a server command-line, without this argument, the command will fail. |

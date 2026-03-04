
### Reset Starter Chest Placement command.

This command reverts the current value of the Starter Chest Placed value to it's default, Not Placed.

This command exists as part of the `ShouldPlaceStarterChestAtFirstTime` configuration option 
first introduced in the 1.2.0 version of the mod.

This is useful only for debugging whether the Starter Chest is placed once someone goes to the Mining
Dimension for the first time, and for debugging the loot table of the chest itself.

> [!NOTE]
If the `ShouldPlaceStarterChestAtFirstTime` configuration option is off,
then this command does not have any effect on gameplay.

Command Syntax:

~~~
/mdex teleporter_spawn_data reset_starter_chest_placement
~~~

No additional arguments are required to execute this command.
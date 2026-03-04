

### Get Starter Chest Placement Status command.

This command queries whether a Starter Chest has been placed on the Mining Dimension or not.

This command exists as part of the `ShouldPlaceStarterChestAtFirstTime` configuration option 
first introduced in the 1.2.0 version of the mod.

This is useful during debugging to find out whether a Starter Chest has been actually placed or not.

> [!NOTE]
If the `ShouldPlaceStarterChestAtFirstTime` configuration option is off,
then this command will always report that the chest has not been placed.
However, if this option was turned off after the chest was placed, it will 
continue to correctly report whether it was actually placed or not.

Command Syntax:

~~~
/mdex teleporter_spawn_data get_starter_chest_placement_status
~~~

No additional arguments are required to execute this command.

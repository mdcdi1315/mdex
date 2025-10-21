

## The mod-provided loot table helpers

The mod does also provides some loot table helpers for conditionally
loading loot tables from mods.

Since DCO logic cannot be applied yet to these helpers due to how Minecraft 1.20.1 
has the loot table system written, the check for actually determining whether the 
objects requested do exist is made, regardless whether the mod defining them
does exist or not.

They are defined first in 1.3.0 and became stable in 1.5.0 version.

They were not described before because there was a problem over 
how to decode the 'functions' field safely.

This was fixed in 1.7.0.

Note, these can be used from older mod versions, but may cause breakage in the 
loot tables themselves, especially if some entries are invalid.

The below table shows which are the currently defined loot table container entry types:

| Container entry type                | Link                            |
|---------------------------------------|---------------------------|
| Optional loot table reference  | [Link](./OptionalLootTableReference.md)                       |
| Optional item                               | [Link](./OptionalItem.md)                      | 





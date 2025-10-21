
### Mod-defined configuration settings

Apart from the settings set in the data packs, the mod does also offer some configuration options as well 
for debugging/enabling/disabling mod capabilities.

For reference, that configuration file is named as `mdex-common.toml`.

Configuration options are described below.

| Option                             | Since mod version | Description                                                                                                                                                                                                                                                                                                    |
|------------------------------------|-------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| HomeDimension                      | 1.0.0             | Defines the dimension that is considered the 'home' dimension for the player. This is a Minecraft resource location. By default, it is set to the overworld dimension ID.                                                                                                                                      |
| ShouldPlaceStarterChestAtFirstTime | 1.2.0             | Defines a boolean value whether the starter chest configuration created by a data pack should be used after all. Useful for server administrators that believe that they do not need the starter chest to exist due to server policies.                                                                        |
| ShouldSpawnPortalInDeep            | 1.0.0             | Defines a boolean value whether the teleporter placed in the Mining Dimension should be placed at lower Y levels. Useful for server administrators that want to restrict teleporter placement in lower Y levels.                                                                                               |
| DebugFeatureConfigurations         | 1.0.0             | Defines a boolean value whether the mod should display additional debugging messages for the mod-provided feature configurations. Mostly useful for the datapack developers when they develop such. In all other cases, this must remain disabled because this can heavily impact world loading performance.   |
| DebugStructureConfigurations       | 1.3.0             | Defines a boolean value whether the mod should display additional debugging messages for the mod-provided structure configurations. Mostly useful for the datapack developers when they develop such. In all other cases, this must remain disabled because this can heavily impact world loading performance. |
| DebugFeaturePlacementModifierConfigurations | 1.5.0      | Defines a boolean value whether the mod should display additional debugging messages for the mod-provided feature placement modifier configurations. Mostly useful for the datapack developers when they develop such. In all other cases, this must remain disabled because this can heavily impact world loading performance.   |
| DebugStructureProcessorConfigurations | 1.5.0         | Defines a boolean value whether the mod should display additional debugging messages for the mod-provided structure processor configurations. Mostly useful for the datapack developers when they develop such. In all other cases, this must remain disabled because this can heavily impact world loading performance.   |
| DisableTeleportations           | 1.7.0                  | When enabled, disables all teleporting requests to the Mining Dimension. Players still into it after this option is enabled remain there and can return back to their home dimension. This is useful for server administrators which they do wish to stop allowing going to the Mining Dimension for some time. |

package com.github.mdcdi1315.mdex;

/**
 * Defines logging flags for all the mod-related DCO features.
 * @param Feature When {@code true}, logs all DCO compilation process for all the mod-defined feature types.
 * @param Structure When {@code true}, logs all DCO compilation process for all the mod-defined structure types.
 * @param FeaturePlacementModifier When {@code true}, logs all DCO compilation process for all the mod-defined feature placement modifier types.
 * @param StructureProcessors When {@code true}, logs all DCO compilation process for all the mod-defined structure processor types.
 */
public record MDEXDCOLoggingFlags(
        boolean Feature ,
        boolean Structure,
        boolean FeaturePlacementModifier,
        boolean StructureProcessors
) {
    public MDEXDCOLoggingFlags(MDEXModConfig config)
    {
        this(
             config.DebugFeatureConfigurations,
             config.DebugStructureConfigurations,
             config.DebugFeaturePlacementModifierConfigurations,
             config.DebugStructureProcessorConfigurations
        );
    }
}

package com.github.mdcdi1315.mdex.features.config;

/**
 * Interface for specifying your actual configuration data.
 */
public interface IModdedFeatureConfigurationDetails
{
    /**
     * Compiles the data that can be compiled. This method should throw on any erroring condition.
     */
    default void Compile() {}
}

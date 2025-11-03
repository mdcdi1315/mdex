package com.github.mdcdi1315.mdex.features;

import com.github.mdcdi1315.mdex.features.config.IModdedFeatureConfigurationDetails;
import net.minecraft.core.BlockPos;
import com.mojang.serialization.Codec;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

import com.github.mdcdi1315.mdex.features.config.ModdedFeatureConfiguration;

import java.util.Optional;

/**
 * Defines the Mining Dimension: EX base type for all modded feature types. <br />
 * Modded feature types provide the following services: <br /> <br />
 * -> They adopt the Distributed Compilation Object logic. This ensures that modded things can be accessed safely without however asserting on invalid feature configurations. <br />
 * -> Provide a stable and performant framework to work with <br />
 * -> Invalidated modded features are automatically removed before a Minecraft world is loaded
 *
 * @param <TCONFIG> The config class for the current feature type. Must extend from {@link ModdedFeatureConfiguration}.
 */
public abstract class ModdedFeature<TCONFIG extends ModdedFeatureConfiguration<? extends IModdedFeatureConfigurationDetails>>
    extends Feature<TCONFIG>
{
    public ModdedFeature(Codec<TCONFIG> codec)
    {
        super(codec);
    }

    /**
     * Place the modded feature with the specified context.
     * <p>You do not use the place method to specify the feature logic, you instead specify it here.</p>
     * <p>Remarks:</p>
     * <p>
     *     You should not call from the config instance the isInvalid and getAllModIdsAreLoaded methods.
     *     Doing so you double-call these since they are already handled by the overridden place method.
     * </p>
     * @param fpc The place context so that you can generate the feature.
     * @return A value whether the feature was eventually placed.
     */
    protected abstract boolean PlaceModdedFeature(FeaturePlaceContext<TCONFIG> fpc);

    @Override
    public final boolean place(FeaturePlaceContext<TCONFIG> fpc)
    {
        // Although that invalid feature configurations in biome structures are now removed by a mixin,
        // keep this for any features that use their own mechanisms (Such as configured features transitively accessing the placed feature registry)
        return fpc.config().IsCompiled() && PlaceModdedFeature(fpc);
    }

    // Also override this overload to provide an even faster alternative
    // compared to Minecraft's implementation.
    @Override
    public final boolean place(TCONFIG config, WorldGenLevel level, ChunkGenerator cg, RandomSource random, BlockPos origin)
    {
        // Run check for ensureCanWrite after we have checked that the configuration is valid
        // Split AND conditions to two so that to check first for the left-hand condition
        return (config.IsCompiled() && level.ensureCanWrite(origin)) && PlaceModdedFeature(
                new FeaturePlaceContext<>(Optional.empty() , level , cg , random , origin , config)
        );
    }
}

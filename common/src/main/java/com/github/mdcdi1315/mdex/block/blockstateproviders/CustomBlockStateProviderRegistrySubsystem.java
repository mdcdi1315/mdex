package com.github.mdcdi1315.mdex.block.blockstateproviders;

import com.github.mdcdi1315.DotNetLayer.System.ArgumentNullException;
import com.github.mdcdi1315.mdex.MDEXBalmLayer;
import com.mojang.serialization.Lifecycle;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

/**
 * Provides registry and JSON serialization services for custom block state providers.
 */
public final class CustomBlockStateProviderRegistrySubsystem
{
    public static Registry<AbstractBlockStateProviderType<?>> REGISTRY;
    public static ResourceKey<Registry<AbstractBlockStateProviderType<?>>> REGISTRYKEY;
    public static NoiseProviderType NOISE_PROVIDER;
    public static DualNoiseProviderType DUAL_NOISE_PROVIDER;
    public static NoiseThresholdProviderType NOISE_THRESHOLD_PROVIDER;
    public static RandomizedIntStateProviderType RANDOMIZED_INT_STATE_PROVIDER;
    public static RotatedBlockProviderType ROTATED_BLOCK_PROVIDER;
    public static SimpleStateProviderType SIMPLE_STATE_PROVIDER;
    public static WeightedStateProviderType WEIGHTED_STATE_PROVIDER;

    // Private so that this class cannot be instantiated as an object
    private CustomBlockStateProviderRegistrySubsystem() {}

    public static void InitializeRegistry()
    {
        MDEXBalmLayer.LOGGER.info("Initializing custom block state providers registry.");
        REGISTRYKEY = ResourceKey.createRegistryKey(MDEXBalmLayer.id("custom_blockstate_provider_types"));
        REGISTRY = new MappedRegistry<>(REGISTRYKEY , Lifecycle.stable());
        RegisterBlockStateProviders();
    }

    public static void DestroyRegistry()
    {
        MDEXBalmLayer.LOGGER.info("Destroying custom block state providers registry.");
        REGISTRYKEY = null;
        REGISTRY = null;
        AbstractBlockStateProvider.CODEC = null;
    }

    public static <T extends AbstractBlockStateProvider , G extends AbstractBlockStateProviderType<T>> G Register(G any, ResourceLocation boundname)
    {
        ArgumentNullException.ThrowIfNull(any , "any");
        ArgumentNullException.ThrowIfNull(boundname , "boundname");
        MDEXBalmLayer.LOGGER.trace("Registering custom block state provider with ID {}" , boundname);
        Registry.register(REGISTRY , boundname , any);
        return any;
    }

    public static <T extends AbstractBlockStateProvider , G extends AbstractBlockStateProviderType<T>> G Register(G any, String name)
    {
        return Register(any , MDEXBalmLayer.id(name));
    }

    private static void RegisterBlockStateProviders()
    {
        AbstractBlockStateProvider.CODEC = REGISTRY.byNameCodec().dispatch(AbstractBlockStateProvider::type , AbstractBlockStateProviderType::Codec);
        NOISE_PROVIDER = Register(new NoiseProviderType() , "noise_provider");
        DUAL_NOISE_PROVIDER = Register(new DualNoiseProviderType() , "dual_noise_provider");
        ROTATED_BLOCK_PROVIDER = Register(new RotatedBlockProviderType() , "rotated_block_provider");
        SIMPLE_STATE_PROVIDER = Register(new SimpleStateProviderType() , "simple_state_provider");
        WEIGHTED_STATE_PROVIDER = Register(new WeightedStateProviderType() , "weighted_state_provider");
        NOISE_THRESHOLD_PROVIDER = Register(new NoiseThresholdProviderType() , "noise_threshold_provider");
        RANDOMIZED_INT_STATE_PROVIDER = Register(new RandomizedIntStateProviderType() , "randomized_int_state_provider");
        REGISTRY.freeze();
    }
}
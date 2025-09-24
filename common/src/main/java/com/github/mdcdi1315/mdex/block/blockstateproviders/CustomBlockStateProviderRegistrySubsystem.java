package com.github.mdcdi1315.mdex.block.blockstateproviders;

import com.github.mdcdi1315.DotNetLayer.System.ArgumentNullException;

import com.github.mdcdi1315.mdex.MDEXBalmLayer;
import com.github.mdcdi1315.mdex.api.MDEXModAPI;
import com.github.mdcdi1315.mdex.api.IModLoaderRegistry;
import com.github.mdcdi1315.mdex.api.RegistryCreationInformation;
import com.github.mdcdi1315.mdex.codecs.DelayLoadedRegistryByNameCodec;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

/**
 * Provides registry and JSON serialization services for custom block state providers.
 */
public final class CustomBlockStateProviderRegistrySubsystem
{
    public static IModLoaderRegistry<AbstractBlockStateProviderType<?>> REGISTRY;
    public static ResourceKey<Registry<AbstractBlockStateProviderType<?>>> REGISTRYKEY;
    private static DelayLoadedRegistryByNameCodec<AbstractBlockStateProviderType<?>> INTERNAL_CODEC;

    // Private so that this class cannot be instantiated as an object
    private CustomBlockStateProviderRegistrySubsystem() {}

    public static void InitializeRegistry()
    {
        MDEXBalmLayer.LOGGER.info("Initializing custom block state providers registry.");
        REGISTRYKEY = ResourceKey.createRegistryKey(MDEXBalmLayer.id("custom_blockstate_provider_types"));
        MDEXModAPI.getMethodImplementation().CreateSimpleRegistry(new RegistryCreationInformation<>(REGISTRYKEY));
        AbstractBlockStateProvider.CODEC = (INTERNAL_CODEC = new DelayLoadedRegistryByNameCodec<>()).dispatch(AbstractBlockStateProvider::GetType , AbstractBlockStateProviderType::Codec);
        MDEXModAPI.getMethodImplementation().RunMethodOnWhenRegistryIsRegistering(REGISTRYKEY , CustomBlockStateProviderRegistrySubsystem::OnRegistryReady);
    }

    private static void OnRegistryReady(IModLoaderRegistry<AbstractBlockStateProviderType<?>> reg)
    {
        REGISTRY = reg;
        INTERNAL_CODEC.ProvideRegistry(REGISTRY);
        INTERNAL_CODEC = null;
        RegisterBlockStateProviders();
    }

    public static void DestroyRegistry()
    {
        MDEXBalmLayer.LOGGER.info("Destroying custom block state providers registry.");
        REGISTRYKEY = null;
        REGISTRY = null;
        AbstractBlockStateProvider.CODEC = null;
    }

    public static <T extends AbstractBlockStateProvider, G extends AbstractBlockStateProviderType<T>> G Register(G any, ResourceLocation boundname)
    {
        ArgumentNullException.ThrowIfNull(any , "any");
        ArgumentNullException.ThrowIfNull(boundname , "boundname");
        MDEXBalmLayer.LOGGER.trace("Registering custom block state provider with ID {}" , boundname);
        REGISTRY.Register(boundname , any);
        //Registry.register(REGISTRY , boundname , any);
        return any;
    }

    public static <T extends AbstractBlockStateProvider, G extends AbstractBlockStateProviderType<T>> G Register(G any, String name)
    {
        return Register(any , MDEXBalmLayer.id(name));
    }

    private static void RegisterBlockStateProviders()
    {
        //AbstractBlockStateProvider.CODEC = REGISTRY.byNameCodec().dispatch(AbstractBlockStateProvider::type , AbstractBlockStateProviderType::Codec);
        Register(NoiseStateProviderType.INSTANCE , "noise_provider");
        Register(DualNoiseProviderType.INSTANCE , "dual_noise_provider");
        Register(RotatedBlockProviderType.INSTANCE , "rotated_block_provider");
        Register(SimpleStateProviderType.INSTANCE , "simple_state_provider");
        Register(WeightedStateProviderType.INSTANCE , "weighted_state_provider");
        Register(NoiseThresholdProviderType.INSTANCE , "noise_threshold_provider");
        Register(RandomizedIntStateProviderType.INSTANCE , "randomized_int_state_provider");
        Register(RuleTestBasedBlockStateProviderType.INSTANCE , "rule_test_based_provider");
        //REGISTRY.freeze();
    }
}
package com.github.mdcdi1315.mdex;

// Base Mods Library
import com.github.mdcdi1315.basemodslib.item.IItemRegistrar;
import com.github.mdcdi1315.basemodslib.config.ConfigManager;
import com.github.mdcdi1315.basemodslib.block.IBlockRegistrar;
import com.github.mdcdi1315.basemodslib.eventapi.EventManager;
import com.github.mdcdi1315.basemodslib.mods.IServerModInstance;
import com.github.mdcdi1315.basemodslib.world.IWorldGenRegistrar;
import com.github.mdcdi1315.basemodslib.commands.ICommandRegistrar;
import com.github.mdcdi1315.basemodslib.registries.IRegistryRegistrar;
import com.github.mdcdi1315.basemodslib.eventapi.server.ServerStartedEvent;
import com.github.mdcdi1315.basemodslib.block.entity.IBlockEntityRegistrar;
import com.github.mdcdi1315.basemodslib.eventapi.server.ServerStoppingEvent;
import com.github.mdcdi1315.basemodslib.eventapi.mods.ModLoadingCompleteEvent;

// Mod interfaces
import com.github.mdcdi1315.mdex.item.ModItems;
import com.github.mdcdi1315.mdex.block.ModBlocks;
import com.github.mdcdi1315.mdex.tag.ModBlockTags;
import com.github.mdcdi1315.mdex.api.OperationsTasker;
import com.github.mdcdi1315.mdex.api.TeleportingManager;
import com.github.mdcdi1315.mdex.commands.MDEXBaseCommand;
import com.github.mdcdi1315.mdex.api.MDEXDefaultTeleportingManager;

// Registry subsystems
import com.github.mdcdi1315.mdex.loottable.LootTableRegistrySubsystem;
import com.github.mdcdi1315.mdex.structures.RuleTestsRegistrySubsystem;
import com.github.mdcdi1315.mdex.structures.StructuresRegistrySubsystem;
import com.github.mdcdi1315.mdex.features.FeatureTypesRegistrySubsystem;
import com.github.mdcdi1315.mdex.structures.StructureProcessorsRegistrySubsystem;
import com.github.mdcdi1315.mdex.features.placement.PlacementModifierRegistrySubsystem;
import com.github.mdcdi1315.mdex.biomespawnadditions.BiomeSpawnAdditionsRegistrySubsystem;
import com.github.mdcdi1315.mdex.block.blockstateproviders.CustomBlockStateProviderRegistrySubsystem;

// Minecraft stuff
import net.minecraft.resources.ResourceLocation;

// SLF4J logging
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;

public final class MDEXModInstance
    implements IServerModInstance
{
    public static final String MOD_ID = "mdex";
    public static Logger LOGGER;

    public static MDEXModConfig CONFIG;

    public static MDEXDCOLoggingFlags LoggingFlags;

    public static final String COMPATIBILITY_NAMESPACE = "mdcdi1315_md";

    public static ResourceLocation MINING_DIM_IDENTIFIER;

    private static OperationsTasker TASKER;

    public static TeleportingManager MANAGER;

    public static ResourceLocation ID(String id) { return ResourceLocation.tryBuild(MOD_ID, id); }

    public static ResourceLocation BlockID(String id) {
        return ResourceLocation.tryBuild(MOD_ID , id);
    }

    @Override
    public void Initialize() {
        MANAGER = null;
        LOGGER = LoggerFactory.getLogger("Mining Dimension: EX mod logger");
        LOGGER.info("Now initializing the Mining Dimension: EX mod!");
        TASKER = new OperationsTasker();
        MINING_DIM_IDENTIFIER = ResourceLocation.tryBuild(COMPATIBILITY_NAMESPACE , "mining_dim");
        ModBlockTags.Initialize();
    }

    private static void TeleporterImplementation(ServerStartedEvent sse) {
        MANAGER = new MDEXDefaultTeleportingManager(sse.server());
    }

    @Override
    public void SetupConfigurationFiles(ConfigManager manager) {
        manager.TrackJsonConfigurationFile(MDEXModConfig.class, MDEXModConfig::new);
        CONFIG = manager.LoadOrCreateConfigurationFile(MDEXModConfig.class);
        LoggingFlags = new MDEXDCOLoggingFlags(CONFIG);
    }

    @Override
    public void OnInitializeEnd() {

    }

    @Override
    public void RegisterEvents(EventManager manager) {
        manager.AddEventListener(ServerStartedEvent.class , MDEXModInstance::OnServerStarted);
        manager.AddEventListener(ServerStoppingEvent.class, MDEXModInstance::OnServerStopping);
        manager.AddEventListener(ModLoadingCompleteEvent.class, ModBlocks::OnModLoadingComplete);
        manager.AddEventListener(ServerStartedEvent.class , MDEXModInstance::TeleporterImplementation);
    }

    private static void OnServerStarted(ServerStartedEvent sse) {
        BiomeSpawnAdditionsRegistrySubsystem.ApplyCurrentBiomeSpawnAdditions(sse.server());
    }

    private static void OnServerStopping(ServerStoppingEvent sse)
    {
        OnServerShutdownInternal();
    }

    private static void OnServerShutdownInternal()
    {
        if (MANAGER != null)
        {
            MANAGER.Dispose();
            MANAGER = null;
        }
        if (TASKER != null)
        {
            TASKER.Dispose();
            TASKER = null;
        }
    }

    public static void RunTaskAsync(Runnable runnable)
    {
        if (TASKER == null) {
            TASKER = new OperationsTasker();
        }
        TASKER.Add(runnable);
    }

    @Override
    public void RegisterBlocks(IBlockRegistrar registrar) {
        ModBlocks.Initialize(registrar);
    }

    @Override
    public void RegisterBlockEntities(IBlockEntityRegistrar registrar) {
        ModBlocks.InitBlockEntities(registrar);
    }

    @Override
    public void RegisterRegistryItems(IRegistryRegistrar registrar) {
        LootTableRegistrySubsystem.Initialize(registrar);
        StructuresRegistrySubsystem.RegisterEntries(registrar);
        RuleTestsRegistrySubsystem.RegisterRuleTests(registrar);
        BiomeSpawnAdditionsRegistrySubsystem.InitializeRegistry(registrar);
        CustomBlockStateProviderRegistrySubsystem.InitializeRegistry(registrar);
        StructureProcessorsRegistrySubsystem.RegisterStructureProcessors(registrar);
    }

    @Override
    public void RegisterWorldGenItems(IWorldGenRegistrar registrar) {
        FeatureTypesRegistrySubsystem.RegisterFeatureTypes(registrar);
        PlacementModifierRegistrySubsystem.RegisterPlacementModifiers(registrar);
    }

    @Override
    public void RegisterItems(IItemRegistrar registrar) {
        ModItems.Initialize(registrar);
    }

    @Override
    public void RegisterCommands(ICommandRegistrar registrar) {
        registrar.RegisterByCommand(MDEXBaseCommand::new);
    }

    @Override
    public String GetModId() {
        return MOD_ID;
    }

    @Override
    public void Dispose() {
        CustomBlockStateProviderRegistrySubsystem.DestroyRegistry();
        LOGGER = null;
    }
}
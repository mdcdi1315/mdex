package com.github.mdcdi1315.mdex;

import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.MaybeNull;

// Mod interfaces
import com.github.mdcdi1315.mdex.item.ModItems;
import com.github.mdcdi1315.mdex.api.MDEXModAPI;
import com.github.mdcdi1315.mdex.block.ModBlocks;
import com.github.mdcdi1315.mdex.tag.ModBlockTags;
import com.github.mdcdi1315.mdex.api.OperationsTasker;
import com.github.mdcdi1315.mdex.network.ModNetworking;
import com.github.mdcdi1315.mdex.util.MDEXInitException;

// Other subsystems
import com.github.mdcdi1315.mdex.api.commands.CommandsSubsystem;
import com.github.mdcdi1315.mdex.permissions.PermissionsSubsystem;

// Registry subsystems
import com.github.mdcdi1315.mdex.loottable.LootTableRegistrySubsystem;
import com.github.mdcdi1315.mdex.structures.RuleTestsRegistrySubsystem;
import com.github.mdcdi1315.mdex.block.BlockPredicatesRegistrySubsystem;
import com.github.mdcdi1315.mdex.features.FeatureTypesRegistrySubsystem;
import com.github.mdcdi1315.mdex.structures.StructuresRegistrySubsystem;
import com.github.mdcdi1315.mdex.structures.StructureProcessorsRegistrySubsystem;
import com.github.mdcdi1315.mdex.features.placement.PlacementModifierRegistrySubsystem;
import com.github.mdcdi1315.mdex.biomespawnadditions.BiomeSpawnAdditionsRegistrySubsystem;
import com.github.mdcdi1315.mdex.block.blockstateproviders.CustomBlockStateProviderRegistrySubsystem;

// Balm
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.BalmRegistries;
import net.blay09.mods.balm.api.event.server.*;
import net.blay09.mods.balm.api.event.BalmEvents;
import net.blay09.mods.balm.api.event.client.DisconnectedFromServerEvent;

// Minecraft
import net.minecraft.resources.ResourceLocation;

// SLF4J logger class
import org.slf4j.Logger;

@SuppressWarnings("unused")
public final class MDEXBalmLayer
{
    public static final String MODID = "mdex";

    public static Logger LOGGER;

    public static final String COMPATIBILITY_NAMESPACE = "mdcdi1315_md";

    public static ResourceLocation MINING_DIM_IDENTIFIER;

    public static MDEXDCOLoggingFlags LoggingFlags;

    private static OperationsTasker TASKER;

    public static void Initialize()
    {
        long time = System.nanoTime();
        LOGGER = org.slf4j.LoggerFactory.getLogger("Mining Dimension: EX Mod Logger");
        LOGGER.info("Mining Dimension: EX common code initializer has begun. Currently running on {} environment.", Balm.getPlatform());
        TASKER = new OperationsTasker();
        MINING_DIM_IDENTIFIER = ResourceLocation.tryBuild(COMPATIBILITY_NAMESPACE , "mining_dim");
        MDEXModAPI.InitializeMethods();
        MDEXModConfig.Initialize(Balm.getConfig());

        BalmRegistries regs = Balm.getRegistries();
        try {
            LOGGER.trace("Initializing block definitions.");
            ModBlocks.Initialize(Balm.getBlocks());
            LOGGER.trace("Initializing block entity definitions.");
            ModBlocks.InitBlockEntities(Balm.getBlockEntities());
            LOGGER.trace("Initializing tag keys.");
            ModBlockTags.Initialize();
            // Items must be initialized after all the blocks and tags are loaded
            // Hard dependencies will plague this if runs before the block registrations
            LOGGER.trace("Initializing item definitions.");
            ModItems.Initialize(Balm.getItems());
            LOGGER.trace("Initializing network definitions.");
            ModNetworking.Initialize(Balm.getNetworking());
            LOGGER.trace("Initializing world generation block predicate definitions.");
            BlockPredicatesRegistrySubsystem.RegisterBlockPredicates(regs);
            LOGGER.trace("Initializing custom block state providers.");
            CustomBlockStateProviderRegistrySubsystem.InitializeRegistry();
            LOGGER.trace("Initializing custom biome spawn additions registry.");
            BiomeSpawnAdditionsRegistrySubsystem.InitializeRegistry();
            LOGGER.trace("Initializing world generation feature type definitions.");
            FeatureTypesRegistrySubsystem.RegisterFeatureTypes(Balm.getWorldGen());
            LOGGER.trace("Initializing world generation structure type definitions.");
            StructuresRegistrySubsystem.RegisterEntries(regs);
            LOGGER.trace("Initializing world generation structure processor type definitions.");
            StructureProcessorsRegistrySubsystem.RegisterStructureProcessors(regs);
            LOGGER.trace("Initializing world generation rule test type definitions.");
            RuleTestsRegistrySubsystem.RegisterRuleTests(regs);
            LOGGER.trace("Initializing loot table entry types.");
            LootTableRegistrySubsystem.Initialize(regs);
            LOGGER.trace("Initializing world generation feature placement modifier type definitions.");
            PlacementModifierRegistrySubsystem.RegisterPlacementModifiers(regs);
            // Also note that mod permissions must be set up before the commands since the permissions are needed by the commands.
            LOGGER.trace("Initializing mod permissions.");
            PermissionsSubsystem.Initialize(Balm.getPermissions());
            LOGGER.trace("Initializing mod commands.");
            CommandsSubsystem.Initialize(Balm.getCommands());
            LOGGER.trace("Setting up server-side server stopped events.");
            SetupEvents(Balm.getEvents());
        } catch (Exception e) {
            // Catch any exceptions, wrap them in an Init exception and throw that instead.
            MDEXBalmLayer.LOGGER.info("Cannot construct Mining Dimension: EX mod instance. Killing the instance." , e);
            throw new MDEXInitException("Could not construct the Mining Dimension: EX mod instance. See the log for details on the exception occurred.");
        }
        // For the initialization to be considered successful, the entire try clause must not throw any exceptions.
        // After that, we are good to continue.
        LOGGER.info("Mining Dimension: EX Initial bootstrapping succeeded in {} seconds." , (System.nanoTime() - time) / 1000000000d);
    }

    @MaybeNull
    public static ResourceLocation id(String path) {
        return ResourceLocation.tryBuild(MODID, path);
    }

    public static ResourceLocation BlockID(String path) { return id(path); }

    public static void RunTaskAsync(Runnable runnable)
    {
        if (TASKER == null) {
            TASKER = new OperationsTasker();
        }
        TASKER.Add(runnable);
    }

    /**
     * The mod's destructor. THIS IS AUTO-CALLED, DO NOT CALL THIS!!!!
     */
    // @ApiStatus.Internal
    public static void DestroyModInstanceData()
    {
        OnServerShutdownInternal();
        MDEXModAPI.UninitializeMethods();
        CustomBlockStateProviderRegistrySubsystem.DestroyRegistry();
        LOGGER = null;
        MINING_DIM_IDENTIFIER = null;
    }

    private static void SetupEvents(BalmEvents events)
    {
        events.onEvent(ServerStartingEvent.class , MDEXBalmLayer::Server_LoadCustomBiomeSpawnAdditions);
        events.onEvent(ServerStoppedEvent.class , MDEXBalmLayer::Server_OnServerStopped);
    }

    /**
     * This should be called from client-side only.
     * @param events The client-side events object which is used to register the events.
     */
    public static void SetupClientSideEvents(BalmEvents events)
    {
        events.onEvent(DisconnectedFromServerEvent.class , MDEXBalmLayer::Client_OnServerDisconnect);
    }

    private static void OnServerShutdownInternal()
    {
        if (TASKER != null)
        {
            TASKER.Dispose();
            TASKER = null;
        }
    }

    private static void Client_OnServerDisconnect(DisconnectedFromServerEvent dse)
    {
        OnServerShutdownInternal();
    }

    private static void Server_OnServerStopped(ServerStoppedEvent sse)
    {
        OnServerShutdownInternal();
    }

    private static void Server_LoadCustomBiomeSpawnAdditions(ServerStartingEvent sse)
    {
        MDEXBalmLayer.LOGGER.info("Mining Dimension: EX mod recieved STARTUP event for server.");
        BiomeSpawnAdditionsRegistrySubsystem.ApplyCurrentBiomeSpawnAdditions(sse.getServer());
    }

    static {
        TASKER = null;
        MINING_DIM_IDENTIFIER = null;
    }

}

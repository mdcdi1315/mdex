package com.github.mdcdi1315.mdex.api;

import com.github.mdcdi1315.DotNetLayer.System.IDisposable;
import com.github.mdcdi1315.DotNetLayer.System.ArgumentNullException;
import com.github.mdcdi1315.DotNetLayer.System.InvalidOperationException;
import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.NotNull;
import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.MaybeNull;
import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.AllowNull;
import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.DisallowNull;

import com.github.mdcdi1315.basemodslib.BaseModsLib;
import com.github.mdcdi1315.basemodslib.utils.Extensions;
import com.github.mdcdi1315.basemodslib.utils.ISynchronized;
import com.github.mdcdi1315.basemodslib.utils.collections.SingleLinkedListBasedQueue;
import com.github.mdcdi1315.basemodslib.world.saveddata.PerDimensionWorldDataManager;

import com.github.mdcdi1315.mdex.MDEXModInstance;
import com.github.mdcdi1315.mdex.block.ModBlocks;
import com.github.mdcdi1315.mdex.api.teleporter.*;
import com.github.mdcdi1315.mdex.util.MDEXException;
import com.github.mdcdi1315.mdex.api.areafinding.ChunkAreaFinder;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.resources.ResourceKey;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

import java.util.concurrent.locks.ReentrantLock;

public abstract class TeleportingManagerV2
    implements IDisposable, ISynchronized
{
    private ReentrantLock lock;
    @MaybeNull
    private MinecraftServer Server;
    private TeleporterManagerData data;
    private volatile boolean executing;
    private TeleportingManagerConfiguration config;
    private ServerLevel Mining_Dim_Level, Home_Level;
    private SingleLinkedListBasedQueue<TeleportRequest> requests;
    private ConfiguredFeature<BaseTeleporterPlacementFeatureConfiguration, ? super Feature<BaseTeleporterPlacementFeatureConfiguration>> genfeature;

    private record TeleportRequest(ServerPlayer player, BlockPos teleporter_position, @AllowNull ServerLevel target) { }

    /**
     * Creates a new teleporting manager, defining the server it will operate on.
     * @param server The server where this teleporting manager was created on.
     * @param cfg The object that provides configuration data for the current teleporting manager.
     * @throws ArgumentNullException <em>server</em> was null.
     */
    @SuppressWarnings("unchecked")
    protected TeleportingManagerV2(MinecraftServer server , TeleportingManagerConfiguration cfg)
            throws ArgumentNullException
    {
        ArgumentNullException.ThrowIfNull(cfg, "cfg");
        ArgumentNullException.ThrowIfNull(server, "server");
        config = cfg;
        MDEXModInstance.LOGGER.info("TeleportingManager: Creating teleporting manager for Minecraft Server named as {}", server.getMotd());
        Server = server;
        var rl = ResourceLocation.tryParse(cfg.FeatureLocation);
        if (rl == null) {
            MDEXModInstance.LOGGER.error("Cannot parse feature location.");
            throw new MDEXException("TeleportingManager Feature is missing");
        }
        var cfg_feature_reg = Server.registryAccess().lookup(Registries.CONFIGURED_FEATURE);
        if (cfg_feature_reg.isEmpty()) {
            MDEXModInstance.LOGGER.error("Cannot locate the Configured Feature Registry.");
            throw new MDEXException("Corrupted server instance detected.");
        }
        var o = cfg_feature_reg.get().getValue(rl);
        if (o == null) {
            MDEXModInstance.LOGGER.error("Cannot find feature with ID {}.", cfg.FeatureLocation);
            throw new MDEXException("TeleportingManager Feature is missing");
        }
        if (o.config() instanceof BaseTeleporterPlacementFeatureConfiguration) {
            // Not unchecked cast because we have safely checked it with the above statement
            // Now allows to be used by other mods by properly providing a feature type that has as a config the BaseTeleporterPlacementFeatureConfiguration class.
            genfeature = (ConfiguredFeature<BaseTeleporterPlacementFeatureConfiguration, ? super Feature<BaseTeleporterPlacementFeatureConfiguration>>) o;
        } else {
            throw new MDEXException("TeleportingManager Feature misconfiguration. The feature type is not BaseTeleporterPlacementFeatureType.");
        }
        Mining_Dim_Level = Server.getLevel(ResourceKey.create(Registries.DIMENSION, cfg.MiningDimension));
        if (Mining_Dim_Level == null) {
            Server = null;
            MDEXModInstance.LOGGER.warn("The mining dimension does not exist in the server. Disabling teleporter implementation for this instance.");
        }
        Home_Level = Server.getLevel(ResourceKey.create(Registries.DIMENSION, cfg.HomeDimension));
        if (Home_Level == null) {
            Server = null;
            MDEXModInstance.LOGGER.warn("The home dimension does not exist in the server. Disabling teleporter implementation for this instance.");
        }
        data = new PerDimensionWorldDataManager(Mining_Dim_Level).ComputeIfAbsent(cfg.DimensionFileName, TeleporterManagerData::new);
        lock = new ReentrantLock();
        executing = false;
        requests = new SingleLinkedListBasedQueue<>();
    }

    private TeleportRequestState TeleportImpl(TeleportRequest request)
    {
        ServerLevel source = request.player.serverLevel();
        PlayerLogicalData source_data = data.GetPlayerLogicalData(request.player);

        ServerLevel level;
        TeleporterLogicalEntry target_teleporter = null;
        int index = source_data.used_teleporter_index;
        if (request.target != null) {
            // Use the target level specified in the request.
            index = -1;
            level = request.target;
        } else if (index == -1) {
            // We do not have a target teleporter.
            if (source == Mining_Dim_Level) {
                // The target dimension in this case is the home dimension.
                level = Home_Level;
            } else {
                // Target is the Mining Dimension.
                level = Mining_Dim_Level;
            }
        } else {
            // We do have a target teleporter.
            target_teleporter = data.GetTeleporterEntry(index);
            level = Server.getLevel(target_teleporter.teleporter_position.level());
        }

        if (level == source) {
            // Resolved teleporter is cycling in the same dimension.
            // This happens if and only if the player dies in the Mining Dimension.
            // To fix this, we will set index = -1 to get to the below if statement.
            // NOTE: Do not reorder this to be a subcase of the below if statement, we need null detection!
            MDEXModInstance.LOGGER.warn("TeleportingManagerV2: Detected a dimension cycle for player with UUID '{}': Dimension: '{}'", request.player.getUUID(), level.dimension().location());
            if (level == Mining_Dim_Level) {
                // Cycling through Mining Dimension, use home dimension.
                level = Home_Level;
            } else {
                // Otherwise define the Mining Dimension.
                level = Mining_Dim_Level;
            }
            index = source_data.last_teleporter_index;
            if (index > -1)
            {
                target_teleporter = data.GetTeleporterEntry(index);
                if (target_teleporter.teleporter_position.level().equals(level.dimension())) {
                    MDEXModInstance.LOGGER.info("TeleportingManagerV2: Mitigated dimension cycle for player with UUID '{}' by using previous teleporter implementation.", request.player.getUUID());
                    PatchPlayerLogicalData(source_data, request.player, target_teleporter.teleporter_position.position());
                } else {
                    index = -1;
                    target_teleporter = null;
                }
            }
            if (index == -1 || target_teleporter == null) {
                MDEXModInstance.LOGGER.warn("TeleportingManagerV2: Could not mitigate dimension cycle by previous teleporting data, manufacturing a new one.");
            }
        }

        if (level == null) {
            MDEXModInstance.LOGGER.info("TeleportingManagerV2: Cannot teleport player with UUID '{}' because a target dimension could not be computed.", request.player.getUUID());
            request.player.displayClientMessage(Component.literal("Cannot teleport. This is an implementation bug. Please report to mdcdi1315.") , true);
            return TeleportRequestState.FAILED;
        } else if (config.DisableTeleportations && level == Mining_Dim_Level) {
            request.player.sendSystemMessage(
                    Component.translatable("mdex.teleportmanager.msg.teleporting_to_specified_dim_is_disabled"),
                    true
            );
            return TeleportRequestState.FAILED;
        }

        if (index == -1)
        {
            // Maybe we can resolve the teleporter to use if we perform a lookup on the teleporters...
            TeleporterLogicalEntry e;
            int n_teleporters = data.GetTeleporters();
            for (int I = 0; I < n_teleporters && target_teleporter == null; I++)
            {
                e = data.GetTeleporterEntry(I);
                if (Server.getLevel(e.teleporter_position.level()) == level)
                {
                    int U = e.target_teleporter_index;
                    // To spice up a bit, accept only if the random source has green light.
                    if (U > -1 && level.random.nextBoolean())
                    {
                        var g = data.GetTeleporterEntry(U);
                        if (Server.getLevel(g.teleporter_position.level()) == source)
                        {
                            // OK, we can use this, so, we do not need to manufacture a teleporter.
                            target_teleporter = e;
                            MDEXModInstance.LOGGER.info("TeleportingManagerV2: Found a compatible teleporter to teleport the player of UUID '{}'. Target dimension: {}, Position: {}", request.player.getUUID(), e.teleporter_position.level(), e.teleporter_position.position());
                        }
                    }
                }
            }
            if (target_teleporter == null)
            {
                int target_index = ManufactureTeleporter(request.teleporter_position, source, level);
                if (target_index == -1) {
                    MDEXModInstance.LOGGER.info("TeleportingManagerV2: Cannot teleport player with UUID '{}' because a teleporter could not be manufactured.", request.player.getUUID());
                    request.player.displayClientMessage(Component.literal("Cannot teleport. This is an implementation bug. Please report to mdcdi1315.") , true);
                    return TeleportRequestState.FAILED;
                } else {
                    target_teleporter = data.GetTeleporterEntry(target_index);
                }
            }
            // We need to also patch the player logical data to teleport the player to the correct position - otherwise we will always teleport him to 0, 0, 0 and that's bad.
            PatchPlayerLogicalData(source_data, request.player, target_teleporter.teleporter_position.position());
        }

        var cxt = data.BeginUpdateLogicalData(request.player);
        boolean success = TeleportImpl(request.player, level, source_data, source != level);
        if (success)
        {
            // Verify that we have a teleporter on the source dimension. Things can go very wrong sometimes so
            // at least we want to ensure that the player can teleport back.
            // Only the block of interest will be changed, if needed; the entire feature won't be placed.
            if (!TeleporterExists(source.getBlockState(request.teleporter_position))) {
                // We need to place it down.
                level.setBlock(request.teleporter_position, ModBlocks.TELEPORTER.defaultBlockState(), 0);
            }

            cxt.StoreCurrentTeleporterIndex(source_data.used_teleporter_index);
            cxt.StoreTargetTeleporterIndex(target_teleporter.target_teleporter_index = data.GetTeleporterIndex(source, request.teleporter_position));
            MDEXModInstance.LOGGER.info("TeleportingManagerV2: Successfully teleported player with UUID '{}' from dimension '{}' to dimension '{}'.", request.player.getUUID(), source.dimension().location(), level.dimension().location());
        }

        return success ? TeleportRequestState.COMPLETED : TeleportRequestState.FAILED;
    }

    private int ManufactureTeleporter(BlockPos req_teleporter_position, ServerLevel source, ServerLevel target)
    {
        BlockPos temp;
        boolean use_starter_chest = false;
        double coord_factor = target.dimensionType().coordinateScale() / source.dimensionType().coordinateScale();
        BlockPos.MutableBlockPos teleporter_position = target.getWorldBorder().clampToBounds(req_teleporter_position.getX() * coord_factor, req_teleporter_position.getY(), req_teleporter_position.getZ() * coord_factor).mutable();

        if (teleporter_position.getY() + 20 > target.getMaxY()) { teleporter_position.setY(target.getMinY() - 20); }

        if (target == Home_Level) {
            // Mining Dimension -> Home dimension (Typically the Overworld)
            // The home level is typically an overworld-like dimension that has
            // the property of sea biomes, as such we want the player to spawn above
            // the surface because spawning in a cave is not a solution.
            // So, that's why this is overridden here with this code.
            // The heightmap does not that, btw.
            // TODO: check spawn biome and make sure to exclude the mountainous ones.

            int sea_level = target.getSeaLevel();

            if (teleporter_position.getY() < sea_level) {
                teleporter_position.setY(sea_level);
                MDEXModInstance.LOGGER.warn("TeleportingManagerV2: Explicitly reassigned the Y coordinate of the teleporter position to {} because it was too low!", teleporter_position.getY());
            }

            // Next step - find a suitable position to place our teleporter feature.
            temp = new ChunkAreaFinder(target.getChunkAt(teleporter_position)).FindArea(new FindTeleporterArea_OverworldImpl(), teleporter_position);
        } else if (target == Mining_Dim_Level) {
            // Any Dimension -> Mining Dimension

            if (config.ShouldSpawnPortalInDeep && teleporter_position.getY() > 50) {
                teleporter_position.setY(Extensions.RandomBetweenInclusiveUnsafe(target.random, 10, 50) - 40);
            }

            temp = new ChunkAreaFinder(target.getChunkAt(teleporter_position)).FindArea(new FindTeleporterArea_MiningDimImpl(), teleporter_position);

            use_starter_chest = config.ShouldPlaceStarterChestAtFirstTime && data.GetPlacementInfo() == StarterChestPlacementInfo.NOT_PLACED;
        } else {
            // Mining Dimension -> Any Dimension
            temp = new ChunkAreaFinder(
                    target.getChunkAt(
                            target.getHeightmapPos(Heightmap.Types.WORLD_SURFACE, teleporter_position)
                    )
            ).FindArea(new FindTeleporterArea_OverworldImpl());
        }

        if (temp == null) {
            // Will always be non-null by definition.
            // We won't run the position transformer since this is not a valid placement position.
            temp = teleporter_position;
        } else {
            // Be noted, any call to FindArea would return the position that above it is the surface.
            // As such, adjustments may be required.
            temp = AdjustTeleporterPositionForFeature(temp, target);
            if (temp == null) {
                // We don't trust the derived class output.
                throw new InvalidOperationException("Teleporter position transformer failed to give a result!");
            }
        }

        // Build the feature here
        genfeature.config().PlaceStarterChest = use_starter_chest;
        boolean p = genfeature.place(target, target.getChunkSource().getGenerator(), target.random, temp);
        data.SetChestPlacementAsPlaced();
        genfeature.config().PlaceStarterChest = false;
        return p ? data.GetTeleporterIndex(target, temp) : -1;
    }

    private static void PatchPlayerLogicalData(PlayerLogicalData data, ServerPlayer player, BlockPos teleporter_position)
    {
        data.current_position = Vec3.upFromBottomCenterOf(teleporter_position, 0.6f);
        data.x_rotation = player.getXRot();
        data.y_rotation = player.getYRot();
    }

    private void RunRequests()
    {
        try {
            if (executing) { return; }
            executing = true;
            TeleportRequest req;
            TeleportRequestState state;
            while ((req = GetRequestSafe()) != null)
            {
                state = TeleportImpl(req);
                BaseModsLib.GetEventsManager().FireEvent(new TeleportResultReceivedEvent(state, req.player));
            }
        } finally {
            executing = false;
        }
    }

    private TeleportRequest GetRequestSafe()
    {
        lock.lock();
        try {
            return requests.TryDequeue();
        } finally {
            lock.unlock();
        }
    }

    /**
     * Teleports a player to the Mining Dimension, or it teleports him back to his Home Dimension.
     * @param player The player to teleport to the Mining Dimension.
     * @param teleporter_position The position of the touched teleporter block. If executed by external means, this must place a teleporter feature there.
     * @param target Overrides the target dimension, and instead teleports the player to the specified dimension. If {@code null}, the default computation method is used.
     * @return A status value.
     */
    @NotNull
    public TeleportRequestState Teleport(ServerPlayer player, BlockPos teleporter_position, @AllowNull ServerLevel target)
    {
        if (Server == null) {
            return TeleportRequestState.FAILED;
        } else {
            lock.lock();
            try {
                requests.Enqueue(new TeleportRequest(player, teleporter_position, target));
                Server.execute(this::RunRequests);
                TeleportRequestState state = requests.GetCount() > 1 ? TeleportRequestState.SCHEDULED : TeleportRequestState.COMPLETED;
                if (state == TeleportRequestState.SCHEDULED) {
                    BaseModsLib.GetEventsManager().FireEvent(new TeleportResultReceivedEvent(state, player));
                }
                return state;
            } finally {
                lock.unlock();
            }
        }
    }

    /**
     * Teleports a player to the Mining Dimension, or it teleports him back to his Home Dimension.
     * @param player The player to teleport to the Mining Dimension.
     * @param teleporter_position The position of the touched teleporter block. If executed by external means, this must place a teleporter feature there.
     * @return A status value.
     */
    @NotNull
    public TeleportRequestState Teleport(ServerPlayer player, BlockPos teleporter_position)  { return Teleport(player, teleporter_position, null); }

    /**
     * Returns the data storage of this {@link TeleportingManagerV2} instance.
     * @return The data storage.
     */
    @NotNull
    public TeleporterManagerData GetData() { return data; }

    /**
     * Returns the {@link ServerLevel} object associated with the Mining Dimension.
     * @return The {@link ServerLevel} associated with the Mining Dimension.
     */
    @NotNull
    public ServerLevel GetMiningDimensionLevel() { return Mining_Dim_Level; }

    /**
     * Returns the {@link ServerLevel} object associated with the home dimension.
     * @return The {@link ServerLevel} associated with the home dimension.
     */
    @NotNull
    public ServerLevel GetHomeDimensionLevel() { return Home_Level; }

    /**
     * Defines the actual teleporting implementation. This implementation may vary by mod loader and Minecraft version.
     * @param player The server-side player to be transferred.
     * @param target The target server level.
     * @param player_logical_data The player's teleporting logical data.
     * @param play_teleport_sound A value whether a teleporting sound should be played as well during teleportation.
     * @return A value whether teleporting was successful.
     */
    protected abstract boolean TeleportImpl(
            @DisallowNull ServerPlayer player ,
            @DisallowNull ServerLevel target ,
            @DisallowNull PlayerLogicalData player_logical_data,
            boolean play_teleport_sound
    );

    /**
     * Gets a value whether the passed in block state is the teleporter block.
     * @param state The {@link BlockState} to get the result.
     * @return A value whether {@code state} is the teleporter block.
     */
    protected abstract boolean TeleporterExists(@DisallowNull BlockState state);

    /**
     * Transforms the input position for adjusting the teleporter feature position as appropriate. <br />
     * For example, the default teleporting implementation returns always one block above the input one so that the teleporter is naturally placed on the surface. <br />
     * This is called only when a possible position for generating the teleporter feature has been found.
     * @param position The input position to transform.
     * @param target The target server level, if you need to read something from the level to further adjust the position.
     * @return The transformed output position. Must be non-{@code null}. If it is {@code null}, an exception will be thrown at teleporting time.
     * @since 2.2.5
     */
    @NotNull
    protected abstract BlockPos AdjustTeleporterPositionForFeature(@DisallowNull BlockPos position, @DisallowNull ServerLevel target);

    @Override
    public void Dispose()
    {
        executing = true; // Trash requests, do not process them
        data = null;
        lock = null;
        Server = null;
        config = null;
        requests = null;
        genfeature = null;
        Home_Level = null;
        Mining_Dim_Level = null;
    }
}

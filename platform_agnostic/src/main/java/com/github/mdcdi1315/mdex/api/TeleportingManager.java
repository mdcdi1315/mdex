package com.github.mdcdi1315.mdex.api;

import com.github.mdcdi1315.DotNetLayer.System.IDisposable;
import com.github.mdcdi1315.DotNetLayer.System.ArgumentNullException;
import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.MaybeNull;
import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.DisallowNull;

import com.github.mdcdi1315.basemodslib.world.saveddata.PerDimensionWorldDataManager;

import com.github.mdcdi1315.mdex.MDEXModInstance;
import com.github.mdcdi1315.mdex.api.teleporter.*;
import com.github.mdcdi1315.mdex.block.BlockUtils;
import com.github.mdcdi1315.mdex.util.MDEXException;
import com.github.mdcdi1315.mdex.util.RectAreaIterable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.resources.ResourceKey;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.PalettedContainer;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

import java.util.UUID;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Defines the base class for the Mining Dimension teleporting mechanism. <br />
 * This is common code, meaning it does the same tasks in every mod loader,
 * except for the actual teleporting implementation, which it does need interaction with the mod loader. <br />
 * When version 2 of the mod arrives, the teleporting manager will be fully customizable and can be used by external mods as well.
 */
public abstract class TeleportingManager
        implements IDisposable
{
    private static class TeleportingScheduler
        implements Runnable
    {
        private final ServerPlayer player;
        private final TeleportingManager manager;
        private final BlockPos teleporterposition;

        public TeleportingScheduler(ServerPlayer sp , BlockPos p , TeleportingManager mgr)
        {
            player = sp;
            teleporterposition = p;
            manager = mgr;
        }

        @Override
        public void run() {
            try {
                // Already incremented by the teleport method , we need only to decrement it when done with teleporting
                // manager.requests.incrementAndGet();
                // Wait until the first request is completed
                while (manager.IsMainRequestPending) { Thread.onSpinWait(); }
                manager.TeleportInternal(player , teleporterposition , null);
            } finally {
                manager.requests.decrementAndGet();
            }
        }
    }

    @MaybeNull
    private MinecraftServer Server;
    private AtomicInteger requests;
    private ServerLevel Mining_Dim_Level;
    private volatile boolean IsMainRequestPending;
    private final TeleportingManagerConfiguration config;
    private ConfiguredFeature<BaseTeleporterPlacementFeatureConfiguration , ? super Feature<BaseTeleporterPlacementFeatureConfiguration>> genfeature;

    /**
     * Creates a new teleporting manager, defining the server it will operate on.
     * @param server The server where this teleporting manager was created on.
     * @param cfg The object that provides configuration data for the current teleporting manager.
     * @throws ArgumentNullException <em>server</em> was null.
     */
    @SuppressWarnings("unchecked")
    protected TeleportingManager(MinecraftServer server , TeleportingManagerConfiguration cfg)
        throws ArgumentNullException
    {
        ArgumentNullException.ThrowIfNull(cfg, "cfg");
        ArgumentNullException.ThrowIfNull(server , "server");
        config = cfg;
        MDEXModInstance.LOGGER.info("TeleportingManager: Creating teleporting manager for Minecraft Server named as {}" , server.getMotd());
        Server = server;
        var f = Server.registryAccess().registryOrThrow(Registries.CONFIGURED_FEATURE).get(ResourceLocation.tryParse(cfg.FeatureLocation));
        if (f == null)
        {
            MDEXModInstance.LOGGER.error("Cannot find feature with ID {}." , cfg.FeatureLocation);
            throw new MDEXException("TeleportingManager Feature is missing");
        }
        if (f.config() instanceof BaseTeleporterPlacementFeatureConfiguration) {
            // Not unchecked cast because we have safely checked it with the above statement
            // Now allows to be used by other mods by properly providing a feature type that has as a config the BaseTeleporterPlacementFeatureConfiguration class.
            genfeature = (ConfiguredFeature<BaseTeleporterPlacementFeatureConfiguration , ? super Feature<BaseTeleporterPlacementFeatureConfiguration>>)f;
        } else {
            throw new MDEXException("TeleportingManager Feature misconfiguration. The feature type is not BaseTeleporterPlacementFeatureType.");
        }
        Mining_Dim_Level = Server.getLevel(ResourceKey.create(Registries.DIMENSION , cfg.MiningDimension));
        if (Mining_Dim_Level == null) {
            Server = null;
            MDEXModInstance.LOGGER.warn("The mining dimension does not exist in the server. Disabling teleporter implementation for this instance.");
        }
        requests = new AtomicInteger();
        requests.set(0);
    }

    public TeleportRequestState Teleport(Player player , BlockPos teleporterposcurrentworld)
    {
        if (player instanceof ServerPlayer sp) {
            ArgumentNullException.ThrowIfNull(teleporterposcurrentworld , "teleporterpos");
            if (Server == null) {
                // Teleporting features are disabled. This text is not to be translated.
                sp.displayClientMessage(Component.literal("Unsupported operation.") , true);
                return TeleportRequestState.FAILED;
            }
            if (!IsMainRequestPending && requests.incrementAndGet() == 1) {
                try {
                    IsMainRequestPending = true;
                    return TeleportInternal(sp, teleporterposcurrentworld , null) ? TeleportRequestState.COMPLETED : TeleportRequestState.FAILED;
                } finally {
                    requests.decrementAndGet();
                    IsMainRequestPending = false;
                }
            } else {
                // We must schedule the request.
                MDEXModInstance.RunTaskAsync(new TeleportingScheduler(sp , teleporterposcurrentworld , this));
                return TeleportRequestState.SCHEDULED;
            }
        } else {
            return TeleportRequestState.FAILED;
        }
    }

    public TeleportRequestState TeleportTo(Player player , BlockPos teleporterposcurrentworld, ServerLevel desired)
    {
        if (player instanceof ServerPlayer sp) {
            ArgumentNullException.ThrowIfNull(desired , "desired");
            ArgumentNullException.ThrowIfNull(teleporterposcurrentworld , "teleporterpos");
            if (Server == null) {
                // Teleporting features are disabled. This text is not to be translated.
                sp.displayClientMessage(Component.literal("Unsupported operation.") , true);
                return TeleportRequestState.FAILED;
            }
            if (!IsMainRequestPending && requests.incrementAndGet() == 1) {
                try {
                    IsMainRequestPending = true;
                    return TeleportInternal(sp, teleporterposcurrentworld , desired) ? TeleportRequestState.COMPLETED : TeleportRequestState.FAILED;
                } finally {
                    requests.decrementAndGet();
                    IsMainRequestPending = false;
                }
            } else {
                // Cannot schedule the request, fail.
                return TeleportRequestState.SCHEDULING_FAILED;
            }
        } else {
            return TeleportRequestState.FAILED;
        }
    }

    private boolean TeleportInternal(ServerPlayer sp, BlockPos teleporterposcurrentworld , @MaybeNull ServerLevel desired_level)
    {
        // The below two variables are used to manipulate the spawn data
        PlayerPlacementInformation p;
        TeleporterSpawnData dat;

        Vec3 targetpos = null; // Will hold the exact spawn position of the player to the target dimension

        ServerLevel target; // Will hold the exact target dimension the player needs to be transferred to.

        ServerLevel old = (ServerLevel) sp.level(); // Holds the 'source' server level (from which the player is currently into)

        {
            p = new PerDimensionWorldDataManager(old).ComputeIfAbsent(config.DimensionFileName, TeleporterSpawnData::new).GetOrUpdateEntry(sp);

            if (desired_level == null) {
                targetpos = p.GetSourceDimensionPosition();
                ResourceLocation rl = p.GetSourceDimension();
                // If not a source dimension was assigned, use the Mining Dimension.
                if (rl == null) {
                    target = Mining_Dim_Level;
                    if (p.GetTeleporterPosition() == null) {
                        // Do this to avoid placing for a second time the teleporter feature in the origin dimension
                        // It makes sense to do it since the manager does not have a way to know where the teleporter is placed.
                        p.SetTeleporterPosition(teleporterposcurrentworld);
                    }
                } else {
                    target = Server.getLevel(ResourceKey.create(Registries.DIMENSION, rl));
                }
            } else {
                // User requested to go to a dimension of their choice (either executed through the command or with other means)
                target = desired_level;
                p.SetTeleporterPosition(teleporterposcurrentworld);
            }

            // Set new rotation information before returning
            p.SetPlayerRotationInfo(new PlayerRotationInformation(sp));
        }

        if (target == old) {
            // This happened because the Mining Dimension was selected while in the Mining Dimension.
            // Use the Home Dimension config option instead.
            target = Server.getLevel(ResourceKey.create(Registries.DIMENSION , config.HomeDimension));
        }

        if (config.DisableTeleportations && target == Mining_Dim_Level) {
            sp.displayClientMessage(Component.translatable("mdex.teleportmanager.msg.teleporting_to_specified_dim_is_disabled") , false);
            return false;
        }

        if (target == null) {
            // Still unresolvable, return false.
            return false;
        }

        MDEXModInstance.LOGGER.info("MDEXTELEPORTER_EVENTS: Target dimension is '{}'." , target.dimension().location());

        // Get spawn data for our target level
        dat = new PerDimensionWorldDataManager(target).ComputeIfAbsent(config.DimensionFileName , TeleporterSpawnData::new);

        if (target != Mining_Dim_Level) {
            dat.SetChestPlacementAsIrrelevant();
        }

        // Determine which is the teleporter position.
        BlockPos bp;

        // If applicable and possible, restore the player's rotation too.
        PlayerRotationInformation rot_info = null;

        if ((p = dat.GetLastSpawnInfo(sp)) != null) {
            // We have the exact teleporter position from the spawn data, use that instead.
            bp = p.GetTeleporterPosition();
            rot_info = p.GetPlayerRotationInfo();
        } else {
            // Try and see whether another player has used this teleporter implementation before.
            bp = GetTeleporterInfoByOtherPlayer(sp , dat , teleporterposcurrentworld);
        }

        if (bp == null) {
            // Apply the current coordinate scale defined by the target dimension and the source dimension, and specify the teleporter area.
            double scale = target.dimensionType().coordinateScale() / old.dimensionType().coordinateScale();
            bp = target.getWorldBorder().clampToBounds(scale * teleporterposcurrentworld.getX() , teleporterposcurrentworld.getY() , scale * teleporterposcurrentworld.getZ());
        }

        // If it happens that the teleporter is existing in that particular block, that teleporter will be re-used instead.
        if (!TeleporterIsExisting(target.getBlockState(bp))) {
            MDEXModInstance.LOGGER.info("MDEXTELEPORTER_EVENTS: Teleporter does not exist in block position X = {}, Y = {} and Z = {}, attempting to place the feature." , bp.getX() , bp.getY() , bp.getZ());
            bp = PlaceTeleporterFeature(target , bp , dat);
        }

        if (bp == null) {
            sp.displayClientMessage(Component.literal("Cannot teleport. This is an implementation bug. Please report to mdcdi1315.") , true);
            MDEXModInstance.LOGGER.error("MDEXTELEPORTER_EVENTS: Teleporting failed, no valid spawn position was found");
            return false;
        }

        if (targetpos == null) {
            // If the player goes for the first time to the Mining Dimension, spawn him/her above the teleporter block.
            targetpos = bp.getCenter().add(0 , 1.0006 , 0);
        }

        // Determine the current position of the player, will be used later to save the player position when returning back to the current dimension
        Vec3 sourcepos = sp.getPosition(0f);

        // Target position is determined, as well as where the player will travel to
        // All required spawn data were set/retrieved and teleportation is ready.
        // Source position is set to the spawn data after successful spawning to the target dimension.
        // Teleport Sound Parameter: Play the teleport sound only when actually changing dimensions
        // Ref equality can be used here since these objects are single and unique for the server
        if (TeleportImpl(sp , target , targetpos , rot_info ,old != target)) {
            dat.GetOrUpdateEntry(sp)
                    .SetTeleporterPosition(bp)
                    .SetSourceDimensionPosition(sourcepos)
                    .SetSourceDimension(old.dimension().location());
            MDEXModInstance.LOGGER.info("MDEXTELEPORTER_EVENTS: Player with UUID '{}' was successfully teleported to dimension with ID '{}' through Mining Dimension TeleportingManager mechanism." , sp.getUUID() , target.dimension().location());
            return true;
        } else {
            return false;
        }
    }

    @MaybeNull
    private BlockPos GetTeleporterInfoByOtherPlayer(ServerPlayer player, TeleporterSpawnData data_target, BlockPos old_teleporter_pos)
    {
        // Check if another player has used the source teleporter before.
        // Will help to avoid placing the teleporter feature a second time if already exists there.
        // This will also prevent race conditions on the fact that the teleporter feature is placing but failed to be completely placed due to the above.
        UUID player_key, current_player_key = player.getUUID();
        PlayerPlacementInformation ppi;
        var old_data = new PerDimensionWorldDataManager((ServerLevel) player.level()).Get(config.DimensionFileName, TeleporterSpawnData::new);
        if (old_data != null)
        {
            for (var other_player : old_data.GetSpawnInfos())
            {
                player_key = other_player.getKey();
                if ((!current_player_key.equals(player_key)) &&
                        old_teleporter_pos.equals(other_player.getValue().GetTeleporterPosition()))
                {
                    // OK. We have found a player that has used the same teleporter before.
                    // We can use that to locate where that player was spawned in the target dimension.
                    // Then , return that position if exists for the in question player.
                    ppi = data_target.GetLastSpawnInfoByUUID(player_key);
                    MDEXModInstance.LOGGER.info("MDEXTELEPORTER_EVENTS: Found that the player with UUID '{}' has used the Teleporting Manager mechanism from this teleporter. Will use that player's information (if existing) to spawn '{}' to the desired dimension." , player_key , current_player_key);
                    return ppi == null ? null : ppi.GetTeleporterPosition();
                }
            }
        }
        return null;
    }

    @MaybeNull
    private BlockPos PlaceTeleporterFeature(ServerLevel target , BlockPos basepos , TeleporterSpawnData targetleveldata)
    {
        BlockPos temp , possible;
        if (target.dimension().location().equals(config.MiningDimension)) {
            possible = FindEmptyPlace(
                    target.getChunk(basepos) ,
                    config.ShouldSpawnPortalInDeep ?
                            target.getMinBuildHeight() + 40 :
                            target.getMaxBuildHeight() - 40 ,
                    basepos
            );
            genfeature.config().PlaceStarterChest = config.ShouldPlaceStarterChestAtFirstTime && targetleveldata.GetPlacementInfo() == StarterChestPlacementInfo.NOT_PLACED;
        } else {
            temp = FindSurface(target , basepos);
            possible = Objects.requireNonNullElse(temp, basepos);
        }
        if (possible == null) {
            return null;
        } else if (BlockUtils.HasAnyFluid(target , possible)) {
            // We are into a fluid region???!!
            // We need to remediate this since the player cannot be spawned there, he will die by lava or drown by the water.
            possible = AvoidFluidRegion(target , possible);
            if (possible == null) { return null; }
            MDEXModInstance.LOGGER.info("MDEXTELEPORTER_EVENTS: Attempting to place the teleporter at a higher Y level because it ended up into a fluid region.");
        }
        boolean test = genfeature.place(target , target.getChunkSource().getGenerator(), target.random , possible);
        genfeature.config().PlaceStarterChest = false;
        if (test) {
            targetleveldata.SetChestPlacementAsPlaced();
            return possible;
        } else {
            return null;
        }
    }

    private static BlockPos FindSurface(ServerLevel level , BlockPos basepos)
    {
        int minheight = level.getMinBuildHeight() + 2;

        BlockPos temp;

        for (int I = level.getMaxBuildHeight() - 2; I > minheight; I--)
        {
            temp = basepos.atY(I);
            if (BlockUtils.ReferentIsSolidBlockUnsafe(level.getBlockState(temp)) &&
                    BlockUtils.ReferentIsAirBlockUnsafe(level.getBlockState(temp.above()))) {
                return temp;
            }
        }

        return null;
    }

    private static BlockPos FindEmptyPlace(ChunkAccess access, int endY , BlockPos relto)
    {
        int startY = access.getMinBuildHeight();

        if (endY >= access.getMaxBuildHeight()) {
            endY = access.getMaxBuildHeight() - 1;
        }

        SectionPos pg;

        for (int i = endY; i > startY; i -= LevelChunkSection.SECTION_HEIGHT)
        {
            var c = access.getSection(access.getSectionIndex(i));
            pg = GetSectionRelativePosition(c.getStates() , LevelChunkSection.SECTION_WIDTH , LevelChunkSection.SECTION_HEIGHT);
            if (pg != null) {
                return new BlockPos(
                        relto.getX() + pg.getX(),
                        i + pg.getY(),
                        relto.getZ() + pg.getZ()
                );
            }
        }

        return null;
    }

    private static SectionPos GetSectionRelativePosition(PalettedContainer<BlockState> states , int chunkxsize , int chunkheight)
    {
        for (BlockPos temp : new RectAreaIterable(new BlockPos(0 , 0 , 0) , new BlockPos(chunkxsize-1 , chunkheight-1 , chunkxsize-1)))
        {
            int y = temp.getY();
            if (!states.get(temp.getX() , y , temp.getZ()).isAir() && y < chunkheight && states.get(temp.getX() , y + 1 , temp.getZ()).isAir())
            {
                return SectionPos.of(temp.getX() , temp.getY() , temp.getZ());
            }
        }
        return null;
    }

    private static BlockPos AvoidFluidRegion(ServerLevel target , BlockPos current)
    {
        int ymax = target.getMaxBuildHeight() - 15;

        BlockPos temp;

        for (int I = current.getY(); I < ymax; I++)
        {
            temp = current.atY(I);
            if (BlockUtils.IsEmptyFluid(target , temp) &&
                BlockUtils.IsEmptyFluid(target , temp.above()) &&
                BlockUtils.IsEmptyFluid(target , temp.above(2))
            ) {
                // Nice, we have left the fluid region
                return temp;
            }
        }

        // We cannot leave from the fluid region
        return null;
    }

    /**
     * Returns a value whether the specified block state is the desired teleporter block. <br />
     * Used so that to test whether in the specified block position there the teleporter exists.
     * @param state The state to test against. Might be null.
     * @return A boolean value. When {@code true}, the teleporter is located wherever the specified block state was retrieved.
     */
    protected abstract boolean TeleporterIsExisting(@MaybeNull BlockState state);

    /**
     * Defines the actual teleporting implementation. This implementation may vary by mod loader and Minecraft version.
     * @param player The server-side player to be transferred.
     * @param target The target server level.
     * @param placement_position The position where the player should be teleported to.
     * @param play_teleport_sound A value whether a teleporting sound should be played as well during teleportation.
     * @param rot_info The saved rotation information for the player.
     * The rotation information are constructed by in which rotation the player used the teleporter.
     * Might not be always available depending on how the saved data are defined, so checking for null first before using this parameter becomes mandatory.
     * @return A value whether teleporting was successful.
     */
    protected abstract boolean TeleportImpl(
            @DisallowNull ServerPlayer player ,
            @DisallowNull ServerLevel target ,
            @DisallowNull Vec3 placement_position ,
            @MaybeNull PlayerRotationInformation rot_info,
            boolean play_teleport_sound
    );

    /**
     * Destroys the resources used by the current teleporter manager. <br />
     * If the method needs to be overridden, you must call this one using the {@code super} convention.
     */
    public void Dispose()
    {
        if (requests != null && requests.get() > 0)
        {
            MDEXModInstance.LOGGER.info("MDEXTELEPORTER_EVENTS: Waiting for all teleporting requests to finish first.");
            while (requests.get() > 0) { Thread.onSpinWait(); }
            MDEXModInstance.LOGGER.info("MDEXTELEPORTER_EVENTS: Teleporting requests completed, disposal code continues to run.");
        }
        Server = null;
        requests = null;
        genfeature = null;
        Mining_Dim_Level = null;
    }
}
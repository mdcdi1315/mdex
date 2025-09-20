package com.github.mdcdi1315.mdex.api;

import com.github.mdcdi1315.DotNetLayer.System.IDisposable;
import com.github.mdcdi1315.DotNetLayer.System.ArgumentNullException;
import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.NotNull;
import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.MaybeNull;
import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.DisallowNull;

import com.github.mdcdi1315.mdex.MDEXBalmLayer;
import com.github.mdcdi1315.mdex.MDEXModConfig;
import com.github.mdcdi1315.mdex.api.teleporter.*;
import com.github.mdcdi1315.mdex.block.BlockUtils;
import com.github.mdcdi1315.mdex.util.MDEXException;
import com.github.mdcdi1315.mdex.util.RectAreaIterable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceKey;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.PalettedContainer;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Defines the base class for the Mining Dimension teleporting mechanism. <br />
 * This is common code, meaning it does the same tasks in every mod loader,
 * except for the actual teleporting implementation, which it does need interaction with the mod loader.
 */
public abstract class TeleportingManager
        implements IDisposable
{
    public static final String TELEPORTER_DATA_DIMFILE_NAME = "MDCDI1315_MDEX_TELEPORTERSPAWNDATA";
    public static final String FEATURE_RESOURCE_LOCATION = MDEXBalmLayer.COMPATIBILITY_NAMESPACE + ":teleporter_placement_feature";

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
                manager.requests.incrementAndGet();
                manager.TeleportInternal(player , teleporterposition);
            } finally {
                manager.requests.decrementAndGet();
            }
        }
    }

    private AtomicInteger requests;
    @MaybeNull
    private MinecraftServer Server;
    private ResourceKey<Level> TargetDim;
    private SavedData.Factory<TeleporterSpawnData> factory;
    private ConfiguredFeature<BaseTeleporterPlacementFeatureConfiguration , BaseTeleporterPlacementFeatureType> genfeature;

    /**
     * Creates a new teleporting manager, defining the server it will operate on.
     * @param server The server where this teleporting manager was created on.
     * @throws ArgumentNullException <em>server</em> was null.
     */
    @SuppressWarnings("unchecked")
    protected TeleportingManager(MinecraftServer server)
        throws ArgumentNullException
    {
        ArgumentNullException.ThrowIfNull(server , "server");
        MDEXBalmLayer.LOGGER.info("TeleportingManager: Creating teleporting manager for Minecraft Server named as {}" , server.getMotd());
        Server = server;
        factory = new SavedData.Factory<>(TeleporterSpawnData::new , TeleportingManager::SpawnDataLoader , DataFixTypes.LEVEL);
        var f = Server.registryAccess().registryOrThrow(Registries.CONFIGURED_FEATURE).get(ResourceLocation.tryParse(FEATURE_RESOURCE_LOCATION));
        if (f == null)
        {
            MDEXBalmLayer.LOGGER.error("Cannot find feature with ID {}." , FEATURE_RESOURCE_LOCATION);
            throw new MDEXException("TeleportingManager Feature is missing");
        }
        if (f.feature() instanceof BaseTeleporterPlacementFeatureType) {
            // Not unchecked cast because we have safely checked it with the above statement
            genfeature = (ConfiguredFeature<BaseTeleporterPlacementFeatureConfiguration , BaseTeleporterPlacementFeatureType>)f;
        } else {
            throw new MDEXException("TeleportingManager Feature misconfiguration. The feature type is not BaseTeleporterPlacementFeatureType.");
        }
        var mining_dim = Server.getLevel(ResourceKey.create(Registries.DIMENSION , MDEXBalmLayer.MINING_DIM_IDENTIFIER));
        if (mining_dim == null) {
            Server = null;
            MDEXBalmLayer.LOGGER.warn("The mining_dim dimension does not exist in the server. Disabling teleporter implementation for this instance.");
        }
        requests = new AtomicInteger();
        requests.set(0);
    }

    /**
     * Sets the target dimension for a teleporting request. <br />
     * Required, if a new request is performed later.
     * @param id The resource location of the dimension to get to.
     */
    public void SetTargetDimension(ResourceLocation id)
        throws ArgumentNullException
    {
        ArgumentNullException.ThrowIfNull(id , "id");
        TargetDim = ResourceKey.create(Registries.DIMENSION , id);
    }

    @MaybeNull
    public ServerLevel ComputeTargetLevel() {
        return Server.getLevel(TargetDim);
    }

    /**
     * Gets the target dimension that the next {@link TeleportingManager#Teleport(Player, BlockPos)} call will perform the change to.
     * @return The target dimension, as a {@link ResourceKey} of {@link Level}.
     */
    public ResourceKey<Level> GetTargetDimension() {
        return TargetDim;
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
            if (requests.incrementAndGet() == 1) {
                boolean v = TeleportInternal(sp , teleporterposcurrentworld);
                requests.decrementAndGet();
                return v ? TeleportRequestState.COMPLETED : TeleportRequestState.FAILED;
            } else {
                // We must schedule the request.
                MDEXBalmLayer.RunTaskAsync(new TeleportingScheduler(sp , teleporterposcurrentworld , this));
                return TeleportRequestState.SCHEDULED;
            }
        } else {
            return TeleportRequestState.FAILED;
        }
    }

    private boolean TeleportInternal(ServerPlayer sp , BlockPos teleporterposcurrentworld)
    {
        ServerLevel lvl = ComputeTargetLevel();
        if (lvl == null) { return false; }
        ((ServerLevel)sp.level()).getDataStorage().computeIfAbsent(factory , TELEPORTER_DATA_DIMFILE_NAME).AddEntry(sp , teleporterposcurrentworld.above());
        var tlvldat = lvl.getDataStorage().computeIfAbsent(factory , TELEPORTER_DATA_DIMFILE_NAME);
        if (!TargetDim.location().equals(MDEXBalmLayer.MINING_DIM_IDENTIFIER)) {
            tlvldat.SetChestPlacementAsIrrelevant();
        }
        BlockPos bp = FindTeleporterArea(sp , lvl , teleporterposcurrentworld , tlvldat);
        if (!TeleporterIsExisting(lvl.getBlockState(bp.below()))) {
            bp = PlaceTeleporterFeature(lvl , bp , tlvldat);
        }
        if (bp == null) {
            sp.displayClientMessage(Component.literal("Cannot teleport. This is an implementation bug. Please report to mdcdi1315.") , true);
            MDEXBalmLayer.LOGGER.error("MDEXTELEPORTER_EVENTS: Teleporting failed, no spawn position was found");
            return false;
        }
        // Set the block position by one block above
        // That is the point where the player will be placed to
        // Teleport Sound Parameter: Play the teleport sound only when actually changing dimensions
        // Ref equality can be used here since these objects are single and unique for the server
        if (TeleportImpl(sp , lvl , bp.above() ,sp.level() != lvl))
        {
            tlvldat.AddEntry(sp , bp);
            MDEXBalmLayer.LOGGER.info("MDEXTELEPORTER_EVENTS: Player with UUID '{}' was successfully teleported to dimension with ID '{}' through Mining Dimension TeleportingManager mechanism." , sp.getUUID() , sp.level().dimension().location());
            return true;
        }
        return false;
    }

    private static TeleporterSpawnData SpawnDataLoader(CompoundTag c , HolderLookup.Provider p)
    {
        TeleporterSpawnData t = new TeleporterSpawnData();
        t.FromDeserialized(c);
        return t;
    }

    private BlockPos FindTeleporterArea(ServerPlayer sp ,ServerLevel target , BlockPos tps , TeleporterSpawnData tsd)
    {
        // Load the last spawn data or if those do not exist , we need to create our own on the fly.
        BlockPos p = tsd.GetLastSpawnPosition(sp);
        if (p == null)
        {
            // Otherwise apply the current coordinate scale defined by the Mining Dimension and the home dimension, and specify the teleporter area.
            var scale = DimensionType.getTeleportationScale(target.dimensionType() , sp.level().dimensionType());
            p = target.getWorldBorder().clampToBounds(scale * tps.getX() , tps.getY() , scale * tps.getZ());
        }
        return p;
    }

    @MaybeNull
    private BlockPos PlaceTeleporterFeature(ServerLevel target , BlockPos basepos , TeleporterSpawnData targetleveldata)
    {
        BlockPos temp , possible;
        if (TargetDim.location().equals(MDEXBalmLayer.MINING_DIM_IDENTIFIER)) {
            var mc = MDEXModConfig.getActive();
            possible = FindEmptyPlace(
                    target.getChunk(basepos) ,
                    mc.ShouldSpawnPortalInDeep ?
                            target.getMinBuildHeight() + 40 :
                            target.getMaxBuildHeight() - 40 ,
                    basepos
            );
            genfeature.config().PlaceStarterChest = mc.ShouldPlaceStarterChestAtFirstTime && targetleveldata.GetPlacementInfo() == StarterChestPlacementInfo.NOT_PLACED;
        } else {
            temp = FindSurface(target , basepos);
            possible = Objects.requireNonNullElse(temp, basepos);
        }
        if (possible == null) {
            return null;
        }
        if (!target.getFluidState(possible).is(Fluids.EMPTY))
        {
            // We are into a fluid region???!!
            // We need to remediate this since the player cannot be spawned there, he will die by lava or drown by the water.
            possible = AvoidFluidRegion(target , possible);
            if (possible == null) { return null; }
            MDEXBalmLayer.LOGGER.info("MDEXTELEPORTER_EVENTS: Attempting to place the teleporter at a higher because it ended up into a fluid region.");
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
            if (BlockUtils.BlockIsSolidAndAboveIsAir(level , temp))
            {
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

        for (int i = endY; i > startY; i -= 16)
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
                return temp.above(2);
            }
        }

        // We cannot leave from the fluid region
        return null;
    }

    @NotNull
    public SavedData.Factory<TeleporterSpawnData> GetSavedTeleporterDataFactory() {
        return factory;
    }

    /**
     * Returns a value whether the specified block state is the desired teleporter block. <br />
     * Used so that to test whether in the specified block position there the teleporter exists.
     * @param state The state to test against.
     * @return A boolean value. When {@code true}, the teleporter is located wherever the specified block state was retrieved.
     */
    protected abstract boolean TeleporterIsExisting(@MaybeNull BlockState state);

    /**
     * Defines the actual teleporting implementation. This implementation may vary by mod loader and Minecraft version.
     * @param player The server-side player to be transferred.
     * @param target The target server level.
     * @param teleporterposition The position where the player should be teleported to.
     * @param playteleportsound A value whether a teleporting sound should be played as well.
     * @return A value whether teleporting was successful.
     */
    protected abstract boolean TeleportImpl(@DisallowNull ServerPlayer player , @DisallowNull ServerLevel target , @DisallowNull BlockPos teleporterposition , boolean playteleportsound);

    /**
     * Destroys the resources used by the current teleporter manager. <br />
     * If the method needs to be overridden, you must call this one using the {@code super} convention.
     */
    public void Dispose()
    {
        Server = null;
        factory = null;
        if (requests != null && requests.get() > 0)
        {
            MDEXBalmLayer.LOGGER.info("MDEXTELEPORTER_EVENTS: Waiting for all teleporting requests to finish first.");
            while (requests.get() > 0) { try { Thread.sleep(10); } catch (InterruptedException e) { break; } }
        }
        requests = null;
        TargetDim = null;
        genfeature = null;
    }
}

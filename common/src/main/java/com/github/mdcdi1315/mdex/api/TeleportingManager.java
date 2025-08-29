package com.github.mdcdi1315.mdex.api;

import com.github.mdcdi1315.DotNetLayer.System.IDisposable;
import com.github.mdcdi1315.DotNetLayer.System.ArgumentNullException;
import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.DisallowNull;
import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.MaybeNull;


import com.github.mdcdi1315.mdex.MDEXBalmLayer;
import com.github.mdcdi1315.mdex.MDEXModConfig;

import com.github.mdcdi1315.mdex.api.teleporter.BaseTeleporterPlacementFeatureConfiguration;
import com.github.mdcdi1315.mdex.api.teleporter.BaseTeleporterPlacementFeatureType;
import com.github.mdcdi1315.mdex.api.teleporter.TeleporterSpawnData;
import com.github.mdcdi1315.mdex.block.BlockUtils;
import com.github.mdcdi1315.mdex.util.MDEXException;
import com.github.mdcdi1315.mdex.util.RectAreaIterable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.PalettedContainer;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.saveddata.SavedDataType;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

import java.util.Objects;

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
    @MaybeNull
    private MinecraftServer Server;
    private ResourceKey<Level> TargetDim;
    private SavedDataType<TeleporterSpawnData> factory;
    private ConfiguredFeature<BaseTeleporterPlacementFeatureConfiguration , BaseTeleporterPlacementFeatureType> genfeature;

    /**
     * Creates a new teleporting manager, defining the server it will operate on.
     * @param server The server where this teleporting manager was created on.
     * @throws ArgumentNullException <em>server</em> was null.
     */
    protected TeleportingManager(MinecraftServer server)
        throws ArgumentNullException
    {
        ArgumentNullException.ThrowIfNull(server , "server");
        MDEXBalmLayer.LOGGER.info("TeleportingManager: Creating teleporting manager for Minecraft Server named as {}" , server.getMotd());
        Server = server;
        factory = new SavedDataType<>(TELEPORTER_DATA_DIMFILE_NAME, TeleporterSpawnData::new , TeleporterSpawnData.GetCodec() , DataFixTypes.LEVEL);
        var ra = Server.registryAccess().lookup(Registries.CONFIGURED_FEATURE);
        if (ra.isEmpty()) {
            throw new MDEXException("Cannot look-up the configured feature registry!!!???");
        }
        var rc = ResourceLocation.tryParse(FEATURE_RESOURCE_LOCATION);
        if (rc == null) {
            throw new MDEXException("Cannot build the feature resource location.");
        }
        var f = ra.get().get(rc);
        if (f.isEmpty())
        {
            MDEXBalmLayer.LOGGER.error("Cannot find feature with ID {}." , FEATURE_RESOURCE_LOCATION);
            throw new MDEXException("TeleportingManager Feature is missing");
        }
        if (f.get().value().feature() instanceof BaseTeleporterPlacementFeatureType) {
            // Not unchecked cast because we have safely checked it with the above statement
            genfeature = (ConfiguredFeature<BaseTeleporterPlacementFeatureConfiguration , BaseTeleporterPlacementFeatureType>)f.get().value();
        } else {
            throw new MDEXException("TeleportingManager Feature misconfiguration. The feature type is not BaseTeleporterPlacementFeatureType.");
        }
        var mining_dim = Server.getLevel(ResourceKey.create(Registries.DIMENSION , MDEXBalmLayer.MINING_DIM_IDENTIFIER));
        if (mining_dim == null) {
            Server = null;
            MDEXBalmLayer.LOGGER.warn("The mining_dim dimension does not exist in the server. Disabling teleporter implementation for this instance.");
        }
    }

    /**
     * Sets the target dimension for a teleporting request. <br />
     * Required, if a new request is performed later.
     * @param id The resource location of the dimension to get to.
     */
    public void SetTargetDimension(ResourceLocation id) {
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

    public boolean Teleport(Player player , BlockPos teleporterposcurrentworld)
    {
        if (player instanceof ServerPlayer sp) {
            return TeleportInternal(sp , teleporterposcurrentworld);
        } else {
            return false;
        }
    }

    private boolean TeleportInternal(ServerPlayer sp , BlockPos teleporterposcurrentworld)
    {
        ArgumentNullException.ThrowIfNull(sp , "sp");
        ArgumentNullException.ThrowIfNull(teleporterposcurrentworld , "teleporterpos");
        if (Server == null) {
            // Teleporting features are disabled. This text is not to be translated.
            sp.displayClientMessage(Component.literal("Unsupported operation.") , true);
            return false;
        }
        ServerLevel lvl = ComputeTargetLevel();
        if (lvl == null) { return false; }
        ((ServerLevel)sp.level()).getDataStorage().computeIfAbsent(factory).AddEntry(sp , teleporterposcurrentworld.above());
        var lvldat = lvl.getDataStorage().computeIfAbsent(factory);
        BlockPos bp = FindTeleporterArea(sp , lvl , teleporterposcurrentworld , lvldat);
        if (!TeleporterIsExisting(lvl.getBlockState(bp.below()))) {
            bp = PlaceTeleporterFeature(lvl , bp);
        }
        if (bp == null) {
            sp.displayClientMessage(Component.literal("Cannot teleport. This is an implementation bug. Please report to mdcdi1315.") , true);
            MDEXBalmLayer.LOGGER.error("MDEXTELEPORTER_EVENTS: Teleporting failed, no spawn position was found");
            return false;
        }
        // Set the block position by one block above
        // That is the point where the player will be placed to
        if (TeleportImpl(sp , lvl , bp , true))
        {
            lvldat.AddEntry(sp , bp);
            MDEXBalmLayer.LOGGER.info("MDEXTELEPORTER_EVENTS: Player with UUID '{}' was successfully teleported to dimension with ID '{}' through Mining Dimension TeleportingManager mechanism." , sp.getUUID() , sp.level().dimension().location());
            return true;
        }
        return false;
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
    private BlockPos PlaceTeleporterFeature(ServerLevel target , BlockPos basepos)
    {
        BlockPos temp , possible;
        if (target.dimension().location().equals(MDEXBalmLayer.MINING_DIM_IDENTIFIER)) {
            possible = FindEmptyPlace(
                    target.getChunk(basepos) ,
                    MDEXModConfig.getActive().ShouldSpawnPortalInDeep ?
                            target.getMinY() + 40 :
                            target.getMaxY() - 40 ,
                    basepos
            );
        } else {
            temp = FindSurface(target , basepos);
            possible = Objects.requireNonNullElse(temp, basepos);
        }
        if (possible == null) {
            return null;
        } else {
            if (!target.getFluidState(possible).is(Fluids.EMPTY))
            {
                // We are into a fluid region???!!
                // We need to remediate this since the player cannot be spawned there, he will die by lava or drown by the water.
                possible = AvoidFluidRegion(target , possible);
                if (possible == null) { return null; }
                MDEXBalmLayer.LOGGER.info("MDEXTELEPORTER_EVENTS: Attempting to place the teleporter at a higher level because it ended up into a fluid region.");
            }
            return genfeature.place(target , target.getChunkSource().getGenerator(), target.random , possible) ? possible : null;
        }
    }

    private static BlockPos FindSurface(ServerLevel level , BlockPos basepos)
    {
        int minheight = level.getMinY() + 2;

        BlockPos temp;

        for (int I = level.getMaxY() - 2; I > minheight; I--)
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
        int startY = access.getMinY();

        if (endY >= access.getMaxY()) {
            endY = access.getMaxY() - 1;
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
        int ymax = target.getMaxY() - 15;

        BlockPos temp;

        for (int I = current.getY(); I < ymax; I++)
        {
            temp = current.atY(I);
            if (target.getFluidState(temp).is(Fluids.EMPTY) &&
                    target.getFluidState(temp.above()).is(Fluids.EMPTY) &&
                    target.getFluidState(temp.above(2)).is(Fluids.EMPTY)
            ) {
                // Nice, we have left the fluid region
                return temp.above(2);
            }
        }

        // We cannot leave from the fluid region
        return null;
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
        TargetDim = null;
        genfeature = null;
    }
}

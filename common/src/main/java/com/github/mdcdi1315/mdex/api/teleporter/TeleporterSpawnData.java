package com.github.mdcdi1315.mdex.api.teleporter;

import com.github.mdcdi1315.DotNetLayer.System.ArgumentException;
import com.github.mdcdi1315.DotNetLayer.System.ArgumentNullException;
import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.NotNull;
import com.github.mdcdi1315.DotNetLayer.System.Collections.Generic.KeyValuePair;
import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.MaybeNull;
import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.DisallowNull;

import com.github.mdcdi1315.mdex.MDEXBalmLayer;
import com.github.mdcdi1315.mdex.api.saveddata.ISavedData;
import com.github.mdcdi1315.mdex.api.saveddata.SavedDataCommonHeader;
import com.github.mdcdi1315.mdex.api.saveddata.IncorrectSavedDataFormatException;

import com.google.common.collect.ImmutableList;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;

import java.util.*;

public final class TeleporterSpawnData
    implements ISavedData
{
    public static final String MAGIC = "MDCDI1315_MDEX_SAVEDWORLDDATA";
    public static final short SPAWN_DATA_VERSION = 2;
    private Map<UUID, PlayerPlacementInformation> PlayerMap;
    private StarterChestPlacementInfo placementinfo;

    public TeleporterSpawnData()
    {
        PlayerMap = new HashMap<>();
        // Initially consider that no starter chest is placed.
        placementinfo = StarterChestPlacementInfo.NOT_PLACED;
    }

    /**
     * Sets the chest placement as irrelevant to the dimension that this teleporter spawn data file is bound to.
     */
    public void SetChestPlacementAsIrrelevant() {
        placementinfo = StarterChestPlacementInfo.IRRELEVANT;
    }

    /**
     * Sets the chest placement as placed. <br />
     * That means that the starter chest has been now placed. <br />
     * Does not do anything if the chest placement is invalid for this dimension.
     */
    public void SetChestPlacementAsPlaced() {
        if (placementinfo != StarterChestPlacementInfo.IRRELEVANT) {
            placementinfo = StarterChestPlacementInfo.PLACED;
        }
    }

    /**
     * Sets the chest placement to a specific value. <br />
     * You cannot set, however, from here the irrelevant value as this is dimension-specific.
     * @param inf The new placement information to apply.
     * @throws ArgumentException Passed placement information is invalid.
     */
    public void SetChestPlacementToValue(@DisallowNull StarterChestPlacementInfo inf)
            throws ArgumentException
    {
        switch (inf)
        {
            case PLACED:
            case NOT_PLACED:
                placementinfo = inf;
                break;
            default:
                throw new ArgumentException(String.format("Value not allowed or invalid: %s" , inf));
        }
    }

    /**
     * Gets starter chest placement information about the current dimension. <br />
     * May also return irrelevance if this dimension is not the mining dimension
     * @return The starter chest placement information.
     */
    public StarterChestPlacementInfo GetPlacementInfo() {
        return placementinfo;
    }

    /**
     * Adds a new spawn entry to the current dimension. If the player did not exist before, it is added now. <br />
     * Otherwise, his last known position is returned and is updated with the new one. <br />
     * If the new one is null then the entry is effectively cleared.
     * @param player The Server Player to operate on.
     * @param lastdata The last known teleporting data of the player.
     * @return The old spawn data of the player, if there was one.
     * @throws ArgumentNullException The player argument was null.
     */
    @MaybeNull
    public PlayerPlacementInformation AddEntry(ServerPlayer player , @MaybeNull PlayerPlacementInformation lastdata)
            throws ArgumentNullException
    {
        ArgumentNullException.ThrowIfNull(player , "player");
        return PlayerMap.put(player.getUUID() , lastdata);
    }

    /**
     * Gets a player spawn placement entry or returns an empty one, suitable to be modified.
     * @param player The Server Player to operate on.
     * @return An empty object that will describe spawn data for the player, or an existing object that describes the player's spawn data.
     */
    @NotNull
    public PlayerPlacementInformation GetOrUpdateEntry(ServerPlayer player)
            throws ArgumentNullException
    {
        ArgumentNullException.ThrowIfNull(player , "player");
        return PlayerMap.computeIfAbsent(player.getUUID() , (UUID u) -> new PlayerPlacementInformation());
    }

    /**
     * Gets the last known spawn position of the specified server player. <br />
     * If the current player is not known (i.e. a new player), null is returned.
     * @param player The {@link ServerPlayer} to get the last spawn position.
     * @return The old spawn data of the player, if any.
     * @throws ArgumentNullException player was null.
     */
    @MaybeNull
    public PlayerPlacementInformation GetLastSpawnInfo(ServerPlayer player)
            throws ArgumentNullException
    {
        ArgumentNullException.ThrowIfNull(player , "player");
        return PlayerMap.get(player.getUUID());
    }

    /**
     * Retrieves a list of key-value pairs, specifying the spawn information for all the players for this dimension.
     * @return A list of key-value pairs.
     */
    @NotNull
    public List<KeyValuePair<UUID , PlayerPlacementInformation>> GetSpawnInfos()
    {
        ArrayList<KeyValuePair<UUID , PlayerPlacementInformation>> list = new ArrayList<>(PlayerMap.size());
        PlayerPlacementInformation v;
        for (var g : PlayerMap.entrySet())
        {
            v = g.getValue();
            if (v != null) {
                list.add(new KeyValuePair<>(g.getKey() , v));
            }
        }
        return ImmutableList.copyOf(list);
    }

    @Override
    public void LoadFrom(SavedDataCommonHeader header)
    {
        CompoundTag ct = header.GetData();
        if (header.GetVersion() > SPAWN_DATA_VERSION) {
            MDEXBalmLayer.LOGGER.error("WORLDDIMSAVEDDATA: Cannot deserialize the specified spawn data file with version {}." , header.GetVersion());
        } else {
            Optional<String> os = ct.getString("magic");
            if (os.isPresent()) {
                if (!os.get().equals(MAGIC)) {
                    MDEXBalmLayer.LOGGER.error("WORLDDIMSAVEDATA: The magic value is not the one expected. Found: {}" , os.get());
                } else if (header.GetVersion() == 1) {
                    LoadV1(ct);
                } else {
                    LoadV2(ct);
                }
            } else {
                MDEXBalmLayer.LOGGER.error("WORLDDIMSAVEDATA: Cannot deserialize the specified spawn data file because the 'magic' field does not exist or is invalid.");
            }
            if (header.GetVersion() == 1) {
                Optional<Byte> ob = ct.getByte("starter_chest_placement_info");
                if (ob.isPresent()) {
                    placementinfo = StarterChestPlacementInfo.FromValue(ob.get());
                } else {
                    MDEXBalmLayer.LOGGER.error("WORLDDIMSAVEDATA: Cannot deserialize the specified spawn data file because the 'starter_chest_placement_info' field does not exist or is invalid.");
                }
            } else {
                // If reading the older version, always consider the placement as irrelevant
                placementinfo = StarterChestPlacementInfo.IRRELEVANT;
            }
        }
    }

    private void LoadV1(CompoundTag ct)
    {
        Optional<CompoundTag> oct = ct.getCompound("teleporter_data");
        if (oct.isPresent()) {
            var data = oct.get();
            var entries = data.entrySet();
            PlayerMap = new HashMap<>(entries.size());
            for (var kvp : entries)
            {
                try {
                    UUID u = UUID.fromString(kvp.getKey());
                    int[] d = kvp.getValue().asIntArray().get();
                    PlayerMap.put(u, new PlayerPlacementInformation().SetTeleporterPosition(new BlockPos(d[0] , d[1] , d[2]))); // BlockPos is now translated from now on as Vec3.
                } catch (IllegalArgumentException iae) {
                    MDEXBalmLayer.LOGGER.warn("WORLDDIMSAVEDATA: Cannot deserialize the specified spawn data for the key {} because the key is malformed." , kvp.getKey());
                } catch (NoSuchElementException nsee) {
                    MDEXBalmLayer.LOGGER.warn("WORLDDIMSAVEDATA: Cannot deserialize the specified spawn data for the key {} because the value is malformed." , kvp.getKey());
                }
            }
        } else {
            MDEXBalmLayer.LOGGER.error("WORLDDIMSAVEDATA: Cannot deserialize the specified spawn data file because the 'teleporter_data' field does not exist or is invalid.");
        }
    }

    private void LoadV2(CompoundTag ct)
    {
        Optional<CompoundTag> oct = ct.getCompound("teleporter_data");
        if (oct.isPresent()) {
            var data = oct.get();
            var entries = data.entrySet();
            PlayerMap = new HashMap<>(entries.size());
            for (var k : entries)
            {
                try {
                    UUID u = UUID.fromString(k.getKey());
                    PlayerMap.put(u, new PlayerPlacementInformation(k.getValue().asCompound().get()));
                } catch (IllegalArgumentException iae) {
                    MDEXBalmLayer.LOGGER.warn("WORLDDIMSAVEDATA: Cannot deserialize the specified spawn data for the key {} because the key is malformed." , k.getKey());
                } catch (IncorrectSavedDataFormatException e) {
                    MDEXBalmLayer.LOGGER.warn("WORLDDIMSAVEDATA: Cannot deserialize the specified spawn data for the key {} due to an exception: {}", k.getKey(), e);
                } catch (NoSuchElementException nsee) {
                    MDEXBalmLayer.LOGGER.warn("WORLDDIMSAVEDATA: Cannot deserialize the specified spawn data for the key {} because the value is malformed." , k.getKey());
                }
            }
        } else {
            MDEXBalmLayer.LOGGER.error("WORLDDIMSAVEDATA: Cannot deserialize the specified spawn data file because the 'teleporter_data' field does not exist or is invalid.");
        }
    }

    private void Save(CompoundTag ct)
    {
        CompoundTag ctg = new CompoundTag();
        for (var g : PlayerMap.entrySet())
        {
            var p = g.getValue();
            if (p != null) {
                ctg.put(g.getKey().toString() , p.Encode());
            }
        }
        ct.put("teleporter_data" , ctg);
    }

    @Override
    public SavedDataCommonHeader Save()
    {
        CompoundTag ct = new CompoundTag();
        ct.putString("magic" , MAGIC);
        ct.putByte("starter_chest_placement_info" , placementinfo.GetValue());
        Save(ct);
        return SavedDataCommonHeader.CreateHeader(SPAWN_DATA_VERSION , ct);
    }
}

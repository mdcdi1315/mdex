package com.github.mdcdi1315.mdex.api.teleporter;

import com.github.mdcdi1315.DotNetLayer.System.ArgumentException;
import com.github.mdcdi1315.DotNetLayer.System.ArgumentNullException;
import com.github.mdcdi1315.DotNetLayer.System.Collections.Generic.KeyValuePair;
import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.NotNull;
import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.MaybeNull;
import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.DisallowNull;

import com.github.mdcdi1315.mdex.MDEXBalmLayer;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.Tag;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.saveddata.SavedData;

import com.google.common.collect.ImmutableList;

import java.util.*;

public final class TeleporterSpawnData
    extends SavedData
{
    public static final String MAGIC = "MDCDI1315_MDEX_SAVEDWORLDDATA";
    public static final byte SPAWN_DATA_VERSION = 1;
    private Map<UUID, BlockPos> PlayerMap;
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
     * @param lastknownposition The last known position of the player.
     * @return The old block position of the player, if there was one.
     * @throws ArgumentNullException The player argument was null.
     */
    @MaybeNull
    public BlockPos AddEntry(ServerPlayer player , @MaybeNull BlockPos lastknownposition)
            throws ArgumentNullException
    {
        ArgumentNullException.ThrowIfNull(player , "player");
        BlockPos old = PlayerMap.put(player.getUUID() , lastknownposition);
        setDirty(true);
        return old;
    }

    /**
     * Gets the last known spawn position of the specified server player. <br />
     * If the current player is not known (i.e. a new player), null is returned.
     * @param player The {@link ServerPlayer} to get the last spawn position.
     * @return The last known spawn position, if any.
     * @throws ArgumentNullException player was null.
     */
    @MaybeNull
    public BlockPos GetLastSpawnPosition(ServerPlayer player)
            throws ArgumentNullException
    {
        ArgumentNullException.ThrowIfNull(player , "player");
        return PlayerMap.get(player.getUUID());
    }

    /**
     * Retrieves a list of key-value pairs, specifying the spawn positions for all the players for this dimension.
     * @return A list of key-value pairs.
     */
    @NotNull
    public List<KeyValuePair<UUID , BlockPos>> GetSpawnPositionsForAllPlayers()
    {
        ArrayList<KeyValuePair<UUID , BlockPos>> list = new ArrayList<>(PlayerMap.size());
        BlockPos v;
        for (var g : PlayerMap.entrySet())
        {
            v = g.getValue();
            if (v != null) {
                list.add(new KeyValuePair<>(g.getKey() , v));
            }
        }
        return ImmutableList.copyOf(list);
    }

    public void FromDeserialized(CompoundTag ct)
    {
        if (ct.contains("version" , Tag.TAG_BYTE))
        {
            var version = ct.getByte("version");
            if (version > SPAWN_DATA_VERSION) {
                MDEXBalmLayer.LOGGER.error("WORLDDIMSAVEDDATA: Cannot deserialize the specified spawn data file with version {}." ,version);
            } else {
                if (ct.contains("magic" , Tag.TAG_STRING)) {
                    var magic = ct.getString("magic");
                    if (!magic.equals(MAGIC)) {
                        MDEXBalmLayer.LOGGER.error("WORLDDIMSAVEDATA: The magic value is not the one expected. Found: {}" , magic);
                    } else {
                        if (ct.contains("teleporter_data" , Tag.TAG_COMPOUND)) {
                            var data = ct.getCompound("teleporter_data");
                            var keys = data.getAllKeys();
                            PlayerMap = new HashMap<>(keys.size());
                            for (var k : keys)
                            {
                                try {
                                    UUID u = UUID.fromString(k);
                                    int[] d = data.getIntArray(k);
                                    PlayerMap.put(u, new BlockPos(d[0] , d[1] , d[2]));
                                } catch (IllegalArgumentException iae) {
                                    MDEXBalmLayer.LOGGER.warn("WORLDDIMSAVEDATA: Cannot deserialize the specified spawn data for the key {} because the key is malformed." , k);
                                }
                            }
                        } else {
                            MDEXBalmLayer.LOGGER.error("WORLDDIMSAVEDATA: Cannot deserialize the specified spawn data file because the 'teleporter_data' field does not exist or is invalid.");
                        }
                    }
                } else {
                    MDEXBalmLayer.LOGGER.error("WORLDDIMSAVEDATA: Cannot deserialize the specified spawn data file because the 'magic' field does not exist or is invalid.");
                }
                if (version == 1) {
                    if (ct.contains("starter_chest_placement_info" , Tag.TAG_BYTE)) {
                        placementinfo = StarterChestPlacementInfo.FromValue(ct.getByte("starter_chest_placement_info"));
                    } else {
                        MDEXBalmLayer.LOGGER.error("WORLDDIMSAVEDATA: Cannot deserialize the specified spawn data file because the 'starter_chest_placement_info' field does not exist or is invalid.");
                    }
                } else {
                    // If reading the older version, always consider the placement as irrelevant
                    placementinfo = StarterChestPlacementInfo.IRRELEVANT;
                }
            }
        } else {
            MDEXBalmLayer.LOGGER.error("WORLDDIMSAVEDATA: Cannot deserialize the specified spawn data file because the 'version' field does not exist or is invalid.");
        }
    }

    @Override
    public CompoundTag save(CompoundTag compoundTag, HolderLookup.Provider provider)
    {
        CompoundTag ct = new CompoundTag();
        ct.putByte("version" , SPAWN_DATA_VERSION);
        ct.putString("magic" , MAGIC);
        ct.putByte("starter_chest_placement_info" , placementinfo.GetValue());
        CompoundTag td = new CompoundTag();
        CreateTagData(td);
        ct.put("teleporter_data" , td);
        return ct;
    }

    private void CreateTagData(CompoundTag ct)
    {
        for (var g : PlayerMap.entrySet())
        {
            var p = g.getValue();
            if (p != null) {
                ct.putIntArray(g.getKey().toString() , new int[] { p.getX() , p.getY() , p.getZ() });
            }
        }
    }
}
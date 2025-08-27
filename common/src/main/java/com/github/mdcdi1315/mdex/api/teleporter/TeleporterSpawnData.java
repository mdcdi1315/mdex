package com.github.mdcdi1315.mdex.api.teleporter;

import com.github.mdcdi1315.DotNetLayer.System.ArgumentNullException;
import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.MaybeNull;
import com.github.mdcdi1315.mdex.MDEXBalmLayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class TeleporterSpawnData
    extends SavedData
{
    public static final String MAGIC = "MDCDI1315_MDEX_SAVEDWORLDDATA";
    public static final byte SPAWN_DATA_VERSION = 0;
    private Map<UUID, BlockPos> PlayerMap;

    public TeleporterSpawnData()
    {
        PlayerMap = new HashMap<>();
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

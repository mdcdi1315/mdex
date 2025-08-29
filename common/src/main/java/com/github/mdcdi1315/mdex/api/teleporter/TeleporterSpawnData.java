package com.github.mdcdi1315.mdex.api.teleporter;

import com.github.mdcdi1315.DotNetLayer.System.ArgumentNullException;
import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.MaybeNull;

import net.minecraft.core.BlockPos;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.server.level.ServerPlayer;
import com.github.mdcdi1315.mdex.codecs.CodecUtils;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class TeleporterSpawnData
    extends SavedData
{
    public static final String MAGIC = "MDCDI1315_MDEX_SAVEDWORLDDATA";
    public static final byte SPAWN_DATA_VERSION = 0;
    private byte version;
    private String magicinternal;
    private Map<UUID, BlockPos> PlayerMap;

    public static Codec<TeleporterSpawnData> GetCodec()
    {
        return CodecUtils.CreateCodecDirect(
                Codec.STRING.fieldOf("magic").flatXmap(TeleporterSpawnData::VerifyMagic , DataResult::success).forGetter((TeleporterSpawnData d) -> d.magicinternal),
                Codec.BYTE.fieldOf("version").flatXmap(TeleporterSpawnData::VerifyVersion , DataResult::success).forGetter((TeleporterSpawnData d) -> d.version),
                Codec.unboundedMap(Codec.STRING.flatXmap(TeleporterSpawnData::GetUUIDFromString , TeleporterSpawnData::GetStringFromUUID), BlockPos.CODEC).fieldOf("teleporter_data").forGetter((TeleporterSpawnData d) -> d.PlayerMap),
                TeleporterSpawnData::new
        );
    }

    private static DataResult<Byte> VerifyVersion(byte b)
    {
        if (b > SPAWN_DATA_VERSION) {
            return DataResult.error(() -> String.format("Cannot deserialize the specified spawn data file with version %s." , b));
        }
        return DataResult.success(b);
    }

    private static DataResult<String> VerifyMagic(String mg)
    {
        if (mg == null) {
            return DataResult.error(() -> "Cannot verify magic because the value is null.");
        } else if (!mg.equals(MAGIC)) {
            return DataResult.error(() -> String.format("Magic value does not match: %s" , mg));
        } else {
            return DataResult.success(mg);
        }
    }

    private static DataResult<UUID> GetUUIDFromString(String str)
    {
        if (str == null) {
            return DataResult.error(() -> "Cannot get UUID from string because the string is null reference.");
        }
        try {
            return DataResult.success(UUID.fromString(str));
        } catch (IllegalArgumentException iae) {
            return DataResult.error(() -> String.format("Cannot get UUID from string because it is malformed.\nException data: %s" , iae));
        }
    }

    private static DataResult<String> GetStringFromUUID(UUID uuid)
    {
        if (uuid == null) {
            return DataResult.error(() -> "Cannot get string from UUID because the string is null reference.");
        }
        return DataResult.success(uuid.toString());
    }

    private TeleporterSpawnData(String magic , byte version , Map<UUID, BlockPos> map)
    {
        this.version = version;
        magicinternal = magic;
        PlayerMap = new HashMap<>(map);
    }

    public TeleporterSpawnData()
    {
        version = 0;
        magicinternal = MAGIC;
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
}

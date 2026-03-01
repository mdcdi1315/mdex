package com.github.mdcdi1315.mdex.api.teleporter;

import com.github.mdcdi1315.DotNetLayer.System.ArgumentException;
import com.github.mdcdi1315.DotNetLayer.System.ArgumentNullException;

import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.DisallowNull;
import com.github.mdcdi1315.basemodslib.codecs.CodecUtils;
import com.github.mdcdi1315.basemodslib.world.GlobalPosition;
import com.github.mdcdi1315.basemodslib.utils.ElementSupplier;
import com.github.mdcdi1315.basemodslib.codecs.StrictListCodec;

import com.github.mdcdi1315.basemodslib.world.saveddata.ISavedData;
import com.github.mdcdi1315.basemodslib.world.saveddata.SavedDataCommonHeader;
import com.github.mdcdi1315.basemodslib.world.saveddata.IncorrectSavedDataFormatException;

import com.mojang.datafixers.util.Pair;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;

import net.minecraft.nbt.Tag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;

import java.util.*;

// For this implementation of the teleporter manager data we will keep it as simple as possible:

// -> We will create 'teleporter entries'. Entries that by definition do contain each teleporter position and where it is.
// We will use the GlobalPosition class for this purpose.
// -> Additionally, we need each teleporter to be bound to a teleporter to the target dimension.
// We will do that with an index value indicating the logical entry of the teleporter to the teleporter list.
// By convention, we will allocate a -1 value to indicate that the teleporter has not been bound to a target teleporter.
// Values larger than -1 indicate which teleporter entry to access.
// Note that it is possible more than 1 teleporters to bind to a single teleporter in another dimension.

// Players move around the dimensions using those teleporters.
// When a teleporting request will be done, the data retain all the player-specific info (His rotation, speed, view angle and goes on) are saved.
// Additionally, the dimension from where the request was dispatched is also saved.
// This is information saved per-player basis.

public final class TeleporterManagerData
    implements ISavedData
{
    private static final short VERSION = 1;

    private boolean needs_update;
    private Map<UUID, PlayerLogicalData> players;
    private List<TeleporterLogicalEntry> teleporters;
    private StarterChestPlacementInfo starter_chest_status;

    private record Data(Map<UUID, PlayerLogicalData> players, List<TeleporterLogicalEntry> teleporters, StarterChestPlacementInfo starter_chest_status)
    {
        private static DataResult<UUID> StringToUUID(String s)
        {
            try {
                return DataResult.success(UUID.fromString(s));
            } catch (Exception ex) {
                return DataResult.error(new ElementSupplier<>(ex.getMessage()));
            }
        }

        private static DataResult<String> UUIDToString(UUID uuid) { return DataResult.success(uuid.toString()); }

        public static final Codec<Data> CODEC = CodecUtils.CreateCodecDirect(
                Codec.unboundedMap(Codec.STRING.flatXmap(Data::StringToUUID, Data::UUIDToString), PlayerLogicalData.GetCodec()).fieldOf("players").forGetter(Data::players),
                new StrictListCodec<>(TeleporterLogicalEntry.GetCodec()).fieldOf("teleporters").forGetter(Data::teleporters),
                StarterChestPlacementInfo.GetCodec().fieldOf("starter_chest_placement").forGetter(Data::starter_chest_status),
                Data::new
        );
    }

    public static final class PlayerLogicalDataUpdateContext
    {
        private final PlayerLogicalData data;

        private PlayerLogicalDataUpdateContext(PlayerLogicalData data) { this.data = data; }

        public void EndUpdateAndStore(int teleporter_index) { data.used_teleporter_index = teleporter_index; }
    }

    public TeleporterManagerData()
    {
        needs_update = false;
        this.players = new HashMap<>();
        this.teleporters = new ArrayList<>();
        starter_chest_status = StarterChestPlacementInfo.NOT_PLACED;
    }

    public int GetTeleporterIndex(Level level, BlockPos pos)
    {
        GlobalPosition p;
        TeleporterLogicalEntry e;
        int s = teleporters.size();
        ResourceKey<Level> levelKey = level.dimension();
        for (int I = 0; I < s; I++)
        {
            p = teleporters.get(I).teleporter_position;
            if (p.level().equals(levelKey) && p.position().equals(pos)) { return I; }
        }
        e = new TeleporterLogicalEntry(new GlobalPosition(levelKey, pos));
        teleporters.add(e);
        needs_update = true;
        return s;
    }

    public int GetTeleporters() { return teleporters.size(); }

    public TeleporterLogicalEntry GetTeleporterEntry(int index) { return teleporters.get(index); }

    public PlayerLogicalDataUpdateContext BeginUpdateLogicalData(Player player)
    {
        PlayerLogicalData lld = new PlayerLogicalData();
        lld.x_rotation = player.getXRot();
        lld.y_rotation = player.getYRot();
        lld.current_position = player.position();
        players.put(player.getUUID(), lld);
        needs_update = true;
        return new PlayerLogicalDataUpdateContext(lld);
    }

    public PlayerLogicalData GetPlayerLogicalData(Player player)
    {
        ArgumentNullException.ThrowIfNull(player, "player");
        PlayerLogicalData pld = players.get(player.getUUID());
        return (pld == null) ? new PlayerLogicalData() : pld;
    }

    public Iterable<Map.Entry<UUID, PlayerLogicalData>> GetPlayerLogicalDataEntries() { return players.entrySet(); }

    /**
     * Sets the chest placement as irrelevant to the dimension that this teleporter spawn data file is bound to.
     */
    public void SetChestPlacementAsIrrelevant() { starter_chest_status = StarterChestPlacementInfo.IRRELEVANT; }

    /**
     * Gets starter chest placement information about the current dimension. <br />
     * May also return irrelevance if this dimension is not the mining dimension
     * @return The starter chest placement information.
     */
    public StarterChestPlacementInfo GetPlacementInfo() { return starter_chest_status; }

    /**
     * Sets the chest placement as placed. <br />
     * That means that the starter chest has been now placed. <br />
     * Does not do anything if the chest placement is invalid for this dimension.
     */
    public void SetChestPlacementAsPlaced() {
        if (starter_chest_status != StarterChestPlacementInfo.IRRELEVANT) {
            starter_chest_status = StarterChestPlacementInfo.PLACED;
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
                starter_chest_status = inf;
                break;
            default:
                throw new ArgumentException(String.format("Value not allowed or invalid: %s" , inf));
        }
    }

    @Override
    public void LoadFrom(SavedDataCommonHeader header)
    {
        DataResult<Pair<Data, Tag>> dt = Data.CODEC.decode(NbtOps.INSTANCE, header.GetData());
        if (dt.isSuccess()) {
            Data d = dt.result().get().getFirst();
            players = new HashMap<>(d.players);
            teleporters = new ArrayList<>(d.teleporters);
            starter_chest_status = d.starter_chest_status;
        } else {
            throw new IncorrectSavedDataFormatException(dt.error().get().message());
        }
    }

    @Override
    public SavedDataCommonHeader Save()
    {
        var dr = Data.CODEC.encode(new Data(players, teleporters, starter_chest_status), NbtOps.INSTANCE, NbtOps.INSTANCE.empty());
        if (dr.isSuccess()) {
            return SavedDataCommonHeader.CreateHeader(VERSION, (CompoundTag) dr.result().get());
        } else {
            throw new IncorrectSavedDataFormatException(dr.error().get().message());
        }
    }

    @Override
    public boolean ShouldSave() { return needs_update; }
}

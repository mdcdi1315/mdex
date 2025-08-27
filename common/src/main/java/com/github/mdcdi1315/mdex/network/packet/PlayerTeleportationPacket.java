package com.github.mdcdi1315.mdex.network.packet;

import com.github.mdcdi1315.mdex.codecs.CodecUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.resources.ResourceLocation;

import java.util.UUID;

public final class PlayerTeleportationPacket
    extends AbstractPacket<PlayerTeleportationPacket>
{
    public static final Type<PlayerTeleportationPacket> PACKET_TYPE = ConstructPacketType("player_teleportation_data_packet");

    private UUID playeruuid;
    private ResourceLocation targetdimid;

    public PlayerTeleportationPacket(UUID playeruuid , ResourceLocation targetdimid)
    {
        this.playeruuid = playeruuid;
        this.targetdimid = targetdimid;
    }

    public ResourceLocation targetdimid() {
        return targetdimid;
    }

    public UUID playeruuid() {
        return playeruuid;
    }

    private static DataResult<UUID> DecodeUUIDString(String s)
    {
        try {
            return DataResult.success(UUID.fromString(s));
        } catch (IllegalArgumentException iae) {
            return DataResult.error(() -> String.format("Cannot parse UUID value %s: %s" , s , iae.getMessage()));
        }
    }

    private static DataResult<String> EncodeUUIDString(UUID u)
    {
        if (u == null) {
            return DataResult.error(() -> "The field value of the player's UUID is null.");
        }
        return DataResult.success(u.toString());
    }

    public static Codec<PlayerTeleportationPacket> GetSerializationCodec()
    {
        return CodecUtils.CreateCodecDirect(
                Codec.STRING.flatXmap(PlayerTeleportationPacket::DecodeUUIDString , PlayerTeleportationPacket::EncodeUUIDString).fieldOf("uuid").forGetter(PlayerTeleportationPacket::playeruuid),
                ResourceLocation.CODEC.fieldOf("target_dimension").forGetter(PlayerTeleportationPacket::targetdimid),
                PlayerTeleportationPacket::new
        );
    }
}

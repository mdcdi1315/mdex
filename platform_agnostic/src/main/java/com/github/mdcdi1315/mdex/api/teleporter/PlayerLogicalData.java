package com.github.mdcdi1315.mdex.api.teleporter;

import com.github.mdcdi1315.basemodslib.codecs.CodecUtils;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

/**
 * Provides a data storage related to each player in a given Minecraft Server for the Mining Dimension teleporter data.
 */
public final class PlayerLogicalData
{
    /**
     * Specifies the TELEPORTER of the DIMENSION that the player was lastly to. <br />
     * That is, if the player requests a teleport from the Mining Dimension to the Overworld,
     * this will retain the teleporter that was situated in the Mining Dimension.
     */
    public int used_teleporter_index;
    /**
     * Specifies the TELEPORTER of the DIMENSION that the player used to teleport to the {@link #used_teleporter_index}. <br />
     * If -1, it does not exist and a new teleporter must be constructed.
     */
    public int last_teleporter_index;
    /**
     * The position of the player as known in the dimension where the {@link #used_teleporter_index} corresponds to.
     */
    public Vec3 current_position;
    public float x_rotation, y_rotation;

    public PlayerLogicalData()
    {
        current_position = Vec3.ZERO;
        used_teleporter_index = -1;
        last_teleporter_index = -1;
        x_rotation = 0f;
        y_rotation = 0f;
    }

    private PlayerLogicalData(Vec3 current_position, int used_teleporter_index , int last_teleporter_index, float x_rotation, float y_rotation)
    {
        this.x_rotation = x_rotation;
        this.y_rotation = y_rotation;
        this.current_position = current_position;
        this.last_teleporter_index = last_teleporter_index;
        this.used_teleporter_index = used_teleporter_index;
    }

    public static Codec<PlayerLogicalData> GetCodec()
    {
        return CodecUtils.CreateCodecDirect(
                Vec3.CODEC.fieldOf("current_position").forGetter((p) -> p.current_position),
                Codec.INT.fieldOf("used_teleporter_index").forGetter((p) -> p.used_teleporter_index),
                Codec.INT.optionalFieldOf("last_teleporter_index", -1).forGetter((p) -> p.last_teleporter_index),
                Codec.FLOAT.fieldOf("x_rotation").forGetter((p) -> p.x_rotation),
                Codec.FLOAT.fieldOf("y_rotation").forGetter((p) -> p.y_rotation),
                PlayerLogicalData::new
        );
    }
}

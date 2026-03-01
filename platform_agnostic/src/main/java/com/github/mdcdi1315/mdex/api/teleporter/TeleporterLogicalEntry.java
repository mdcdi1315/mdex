package com.github.mdcdi1315.mdex.api.teleporter;

import com.github.mdcdi1315.DotNetLayer.System.ArgumentNullException;

import com.github.mdcdi1315.basemodslib.codecs.CodecUtils;
import com.github.mdcdi1315.basemodslib.codecs.GlobalPositionCodec;
import com.github.mdcdi1315.basemodslib.world.GlobalPosition;

import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;

public final class TeleporterLogicalEntry
{
    public int target_teleporter_index;
    public final GlobalPosition teleporter_position;

    public TeleporterLogicalEntry(GlobalPosition pos)
    {
        ArgumentNullException.ThrowIfNull(pos, "pos");
        this.teleporter_position = pos;
        target_teleporter_index = -1;
    }

    private TeleporterLogicalEntry(GlobalPosition pos, int target_teleporter_index)
    {
        this.teleporter_position = pos;
        this.target_teleporter_index = target_teleporter_index;
    }

    public static Codec<TeleporterLogicalEntry> GetCodec()
    {
        return CodecUtils.CreateCodecDirect(
                GlobalPositionCodec.INSTANCE.fieldOf("teleporter_position").forGetter((p) -> p.teleporter_position),
                Codec.INT.fieldOf("target_teleporter_index").forGetter((p) -> p.target_teleporter_index),
                TeleporterLogicalEntry::new
        );
    }
}

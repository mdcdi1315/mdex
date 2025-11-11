package com.github.mdcdi1315.mdex.api.teleporter;

import com.github.mdcdi1315.DotNetLayer.System.ArgumentNullException;
import com.github.mdcdi1315.DotNetLayer.System.InvalidOperationException;
import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.NotNull;
import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.MaybeNull;

import com.github.mdcdi1315.basemodslib.world.saveddata.IncorrectSavedDataFormatException;

import net.minecraft.nbt.Tag;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.Vec3;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

public final class PlayerPlacementInformation
{
    private ResourceLocation source_dimension;
    private Vec3 source_dimension_position;
    private BlockPos teleporter_position;
    private PlayerRotationInformation rotation_info;

    public PlayerPlacementInformation(CompoundTag tag)
    {
        this();
        var sd = tag.getString("source_dimension");
        if (sd.isPresent()) {
            source_dimension = ResourceLocation.tryParse(sd.get());
            if (source_dimension == null) {
                throw new IncorrectSavedDataFormatException("Cannot find or decode correctly the source_dimension field.");
            }
        }
        Optional<CompoundTag> oct = tag.getCompound("source_dimension_position");
        if (oct.isPresent()) {
            source_dimension_position = DecodeVec3(oct.get());
        }
        var tp = tag.getIntArray("teleporter_position");
        if (tp.isPresent()) {
            int[] data = tp.get();
            teleporter_position = new BlockPos(data[0] , data[1] , data[2]);
        }
        oct = tag.getCompound("player_rotation_info");
        if (oct.isPresent()) {
            rotation_info = new PlayerRotationInformation(oct.get());
        }
    }

    public PlayerPlacementInformation()
    {
        rotation_info = null;
        source_dimension = null;
        teleporter_position = null;
        source_dimension_position = null;
    }

    public PlayerPlacementInformation SetSourceDimension(ResourceLocation location)
            throws ArgumentNullException
    {
        ArgumentNullException.ThrowIfNull(location , "location");
        source_dimension = location;
        return this;
    }

    public PlayerPlacementInformation SetSourceDimensionPosition(Vec3 source_position)
            throws ArgumentNullException
    {
        ArgumentNullException.ThrowIfNull(source_position, "source_position");
        source_dimension_position = source_position;
        return this;
    }

    public PlayerPlacementInformation SetTeleporterPosition(BlockPos position)
            throws ArgumentNullException
    {
        ArgumentNullException.ThrowIfNull(position, "position");
        teleporter_position = position;
        return this;
    }

    public PlayerPlacementInformation SetPlayerRotationInfo(PlayerRotationInformation rotation_info)
    {
        ArgumentNullException.ThrowIfNull(rotation_info , "rotation_info");
        this.rotation_info = rotation_info;
        return this;
    }

    @MaybeNull
    public ResourceLocation GetSourceDimension() {
        return source_dimension;
    }

    @MaybeNull
    public Vec3 GetSourceDimensionPosition() {
        return source_dimension_position;
    }

    @MaybeNull
    public BlockPos GetTeleporterPosition() {
        return teleporter_position;
    }

    @MaybeNull
    public PlayerRotationInformation GetPlayerRotationInfo() {
        return rotation_info;
    }

    private static Vec3 DecodeVec3(CompoundTag ct)
    {
        int index = 0;
        double[] position = new double[3];
        Optional<Double> od;
        for (String p : new String[] { "xpos" , "ypos" , "zpos" })
        {
            od = ct.getDouble(p);
            if (od.isEmpty()) {
                throw new IncorrectSavedDataFormatException(String.format("Cannot find position field %s." , p));
            } else {
                position[index++] = od.get();
            }
        }
        return new Vec3(
                position[0],
                position[1],
                position[2]
        );
    }

    private static CompoundTag EncodeVec3(Vec3 v3)
    {
        CompoundTag ct = new CompoundTag();
        ct.putDouble("xpos" , v3.x);
        ct.putDouble("ypos" , v3.y);
        ct.putDouble("zpos" , v3.z);
        return ct;
    }

    @NotNull
    public CompoundTag Encode()
            throws InvalidOperationException
    {
        CompoundTag ct = new CompoundTag();
        if (source_dimension != null) {
            ct.putString("source_dimension", source_dimension.toString());
        }
        if (source_dimension_position != null) {
            ct.put("source_dimension_position", EncodeVec3(source_dimension_position));
        }
        if (teleporter_position != null) {
            ct.putIntArray("teleporter_position" , new int[] { teleporter_position.getX() , teleporter_position.getY() , teleporter_position.getZ() });
        }
        if (rotation_info != null) {
            ct.put("player_rotation_info" , rotation_info.Encode());
        }
        return ct;
    }
}

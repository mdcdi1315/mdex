package com.github.mdcdi1315.mdex.api.teleporter;

import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.MaybeNull;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;

/**
 * Starter chest placement information. <br />
 * Saved to the teleporter spawn data, and this information is accessed from there.
 */
public enum StarterChestPlacementInfo
{
    /**
     * No starter chests have been placed on the dimension. (Or have been placed so it was requested from the user to).
     */
    NOT_PLACED((byte) 0),
    /**
     * A starter chest has been already placed on the dimension.
     */
    PLACED((byte) 1),
    /**
     * Starter chests are irrelevant with this dimension (i.e. is not the mining dimension)
     */
    IRRELEVANT((byte) 255);

    private final byte internalval;

    StarterChestPlacementInfo(byte value) {
        internalval = value;
    }

    public byte GetValue() {
        return internalval;
    }

    private DataResult<Byte> GetValue_Codec() {
        return DataResult.success(internalval);
    }

    // Will be needed by later versions
    public static Codec<StarterChestPlacementInfo> GetCodec()
    {
        return Codec.BYTE.flatXmap(
                StarterChestPlacementInfo::FromValue_Codec,
                StarterChestPlacementInfo::GetValue_Codec
        );
    }

    private static DataResult<StarterChestPlacementInfo> FromValue_Codec(byte value)
    {
        StarterChestPlacementInfo stpi = FromValue(value);
        if (stpi == null) {
            return DataResult.error(() -> "Cannot decode the specified value into a starter chest placement information ordinal.");
        }
        return DataResult.success(stpi);
    }

    @MaybeNull
    public static StarterChestPlacementInfo FromValue(byte value)
    {
        for (var c : StarterChestPlacementInfo.values())
        {
            if (c.internalval == value) {
                return c;
            }
        }
        return null;
    }
}

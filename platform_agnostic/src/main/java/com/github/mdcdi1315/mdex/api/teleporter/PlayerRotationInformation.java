package com.github.mdcdi1315.mdex.api.teleporter;

import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.NotNull;

import net.minecraft.nbt.Tag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

/**
 * Provides rotation information for the player. <br />
 * They are applied before the teleporting is performed.
 * @since 1.7.0
 */
public final class PlayerRotationInformation
{
    private final float xrot, yrot;

    public PlayerRotationInformation(Player p)
    {
        xrot = p.getXRot();
        yrot = p.getYRot();
    }

    public PlayerRotationInformation(CompoundTag ct)
    {
        xrot = ct.getFloatOr("x_rot", 0f);
        yrot = ct.getFloatOr("y_rot", 0f);
    }

    public float GetXRotation() { return xrot; }

    public float GetYRotation() { return yrot; }

    @NotNull
    public CompoundTag Encode()
    {
        CompoundTag ct = new CompoundTag();
        ct.putFloat("x_rot" , xrot);
        ct.putFloat("y_rot" , yrot);
        return ct;
    }
}

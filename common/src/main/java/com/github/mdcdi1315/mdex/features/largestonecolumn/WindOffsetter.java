package com.github.mdcdi1315.mdex.features.largestonecolumn;

import com.github.mdcdi1315.mdex.util.Extensions;
import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.MaybeNull;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.FloatProvider;

public final class WindOffsetter
{
    private final int originY;
    @MaybeNull
    private final Vec3 windSpeed;

    public WindOffsetter(int originY, RandomSource random, FloatProvider magnitude) {
        this.originY = originY;
        float mag = magnitude.sample(random);
        float variance = Extensions.RandomBetweenUnsafe(random, 0.0F, Extensions.PI);
        this.windSpeed = new Vec3(Extensions.Cos(variance) * mag,0.0D, Extensions.Sin(variance) * mag);
    }

    private WindOffsetter() {
        this.originY = 0;
        this.windSpeed = null;
    }

    public static WindOffsetter NoWind() {
        return new WindOffsetter();
    }

    public BlockPos offset(BlockPos pos) {
        if (this.windSpeed == null) {
            return pos;
        } else {
            Vec3 vec3 = this.windSpeed.scale(this.originY - pos.getY());
            return pos.offset(Extensions.Floor(vec3.x), 0, Extensions.Floor(vec3.z));
        }
    }
}
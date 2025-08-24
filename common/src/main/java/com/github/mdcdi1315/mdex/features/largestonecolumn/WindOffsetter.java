package com.github.mdcdi1315.mdex.features.largestonecolumn;


import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public final class WindOffsetter {
    private final int originY;
    @Nullable
    private final Vec3 windSpeed;

    public WindOffsetter(int originY, RandomSource random, FloatProvider magnitude) {
        this.originY = originY;
        float mag = magnitude.sample(random);
        float f1 = Mth.randomBetween(random, 0.0F, (float)Math.PI);
        this.windSpeed = new Vec3((Mth.cos(f1) * mag),0.0D, (Mth.sin(f1) * mag));
    }

    private WindOffsetter() {
        this.originY = 0;
        this.windSpeed = null;
    }

    public static WindOffsetter noWind() {
        return new WindOffsetter();
    }

    public BlockPos offset(BlockPos pos) {
        if (this.windSpeed == null) {
            return pos;
        } else {
            int i = this.originY - pos.getY();
            Vec3 vec3 = this.windSpeed.scale(i);
            return pos.offset(Mth.floor(vec3.x), 0, Mth.floor(vec3.z));
        }
    }
}
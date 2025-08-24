package com.github.mdcdi1315.mdex.features.largestonecolumn;

import net.minecraft.util.Mth;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

public final class Utils
{
    private static final double D0 = 0.384;

    public static double getStoneHeight(double radius, double maxRadius, double scale, double minRadius)
    {
        if (radius < minRadius) {
            radius = minRadius;
        }

        double d1 = radius / maxRadius * D0;
        double d2 = 0.75 * Math.pow(d1, 1.3333333333333333);
        double d3 = Math.pow(d1, 0.6666666666666666);
        double d4 = 0.3333333333333333 * Math.log(d1);
        return Math.max(scale * (d2 - d3 - d4), 0.0) / D0 * maxRadius;
    }

    public static boolean isCircleMostlyEmbeddedInStone(LevelAccessor level, BlockPos pos, int radius)
    {
        if (isEmptyOrWaterOrLava(level, pos)) {
            return false;
        } else {
            float f1 = 6.0F / (float)radius;

            for (float f2 = 0.0F; f2 < ((float)Math.PI * 2F); f2 += f1)
            {
                if (isEmptyOrWaterOrLava(level,
                        pos.offset(
                                (int)(Mth.cos(f2) * (float)radius),
                                0,
                                (int)(Mth.sin(f2) * (float)radius)
                        )
                )) {
                    return false;
                }
            }

            return true;
        }
    }

    public static boolean isEmptyOrWater(LevelAccessor level, BlockPos pos) {
        return level.isStateAtPosition(pos, Utils::isEmptyOrWater);
    }

    public static boolean IsEmptyOrWaterOrDesiredBlock(LevelAccessor level, BlockPos pos , Block desired)
    {
        return level.isStateAtPosition(pos, (BlockState p) -> IsEmptyOrWaterOrDesiredBlock(p , desired));
    }

    public static boolean isEmptyOrWaterOrLava(LevelAccessor level, BlockPos pos) {
        return level.isStateAtPosition(pos, Utils::isEmptyOrWaterOrLava);
    }

    public static boolean isEmptyOrWater(BlockState state) {
        return state.isAir() || state.is(Blocks.WATER);
    }

    public static boolean IsDesiredBlockOrLava(BlockState state , Block desired)
    {
        return state.is(Blocks.LAVA) || state.is(desired);
    }

    public static boolean IsEmptyOrWaterOrDesiredBlock(BlockState state , Block desired)
    {
        return state.isAir() || state.is(Blocks.WATER) || state.is(desired);
    }

    public static boolean isEmptyOrWaterOrLava(BlockState state) {
        return state.isAir() || state.is(Blocks.WATER) || state.is(Blocks.LAVA);
    }
}

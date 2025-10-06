package com.github.mdcdi1315.mdex.features.largestonecolumn;

import com.github.mdcdi1315.mdex.block.BlockUtils;
import com.github.mdcdi1315.mdex.util.Extensions;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.block.state.BlockState;

public final class LargeStoneColumn
{
    private static final double D0 = 0.384;

    private final double bluntness, scale;
    private final boolean pointingUp;
    private BlockPos root;
    private int radius;

    public LargeStoneColumn(BlockPos root, boolean pointingUp, int radius, double bluntness, double scale)
    {
        this.root = root;
        this.pointingUp = pointingUp;
        this.radius = radius;
        this.bluntness = bluntness;
        this.scale = scale;
    }

    public int getHeight() {
        return GetHeightAtRadius(0.0D);
    }

    public int getMinY() {
        return this.pointingUp ? this.root.getY() : this.root.getY() - this.getHeight();
    }

    public int getMaxY() {
        return this.pointingUp ? this.root.getY() + this.getHeight() : this.root.getY();
    }

    public boolean IsSuitableForWind(int min_radius_for_wind , float min_bluntness_for_wind) {
        return this.radius >= min_radius_for_wind && this.bluntness >= min_bluntness_for_wind;
    }

    public boolean MoveBackUntilBaseIsInsideStoneAndShrinkRadiusIfNecessary(LevelAccessor level, WindOffsetter offsetter)
    {
        for (; this.radius > 1; this.radius /= 2)
        {
            BlockPos.MutableBlockPos mutable = this.root.mutable();
            int i = Math.min(10, this.getHeight());

            for (int j = 0; j < i; ++j)
            {
                if (level.getBlockState(mutable).is(Blocks.LAVA)) {
                    return false;
                }

                if (IsCircleMostlyEmbeddedInStone(level, offsetter.offset(mutable))) {
                    this.root = mutable;
                    return true;
                }

                mutable.move(this.pointingUp ? Direction.DOWN : Direction.UP);
            }
        }

        return false;
    }

    private boolean IsCircleMostlyEmbeddedInStone(LevelAccessor level, BlockPos pos)
    {
        if (BlockUtils.BlockIsAirOrWaterOrLavaUnsafe(level, pos)) {
            return false;
        } else {
            float rds = radius;
            float f1 = 6.0F / rds;

            for (float f2 = 0.0F; f2 < Extensions.TWO_PI; f2 += f1)
            {
                if (BlockUtils.BlockIsAirOrWaterOrLavaUnsafe(level,
                        pos.offset(
                                (int)(Extensions.Cos(f2) * rds),
                                0,
                                (int)(Extensions.Sin(f2) * rds)
                        )
                )) {
                    return false;
                }
            }

            return true;
        }
    }

    private int GetHeightAtRadius(double radius)
    {
        if (radius < this.bluntness) {
            radius = this.bluntness;
        }

        double d1 = radius / this.radius * D0;
        return (int) (Math.max(
                this.scale * (
                        (0.75d * Math.pow(d1, 1.3333333333333333)) -
                                Math.pow(d1, 0.6666666666666666) -
                                (0.3333333333333333 * Math.log(d1))
                )
                , 0.0d
        ) / D0 * this.radius);
    }

    public void PlaceBlocks(LevelAccessor level, RandomSource random, WindOffsetter offsetter, BlockState desiredstate)
    {
        for (int i = -this.radius; i <= this.radius; ++i)
        {
            for (int j = -this.radius; j <= this.radius; ++j)
            {
                float f = Extensions.SquareRoot((float)(Extensions.Square(i) + Extensions.Square(j)));
                if (f <= (float)this.radius) {
                    int k = GetHeightAtRadius(f);
                    if (k > 0) {
                        if (random.nextFloat() < 0.2f) {
                            k = (int)((float)k * Extensions.RandomBetweenUnsafe(random, 0.8F, 1.0F));
                        }

                        BlockPos.MutableBlockPos mutable = this.root.offset(i, 0, j).mutable();
                        boolean flag = false;
                        int l = this.pointingUp ? level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, mutable.getX(), mutable.getZ()) : Integer.MAX_VALUE;

                        for (int i1 = 0; i1 < k && mutable.getY() < l; ++i1)
                        {
                            BlockPos blockpos = offsetter.offset(mutable);
                            if (BlockUtils.BlockIsAirOrWaterOrLavaUnsafe(level, blockpos)) {
                                flag = true;
                                level.setBlock(blockpos, desiredstate, 2);
                            } else if (flag && level.getBlockState(blockpos).is(BlockTags.BASE_STONE_OVERWORLD)) {
                                break;
                            }

                            mutable.move(this.pointingUp ? Direction.UP : Direction.DOWN);
                        }
                    }
                }
            }
        }
    }
}
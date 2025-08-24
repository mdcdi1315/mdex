package com.github.mdcdi1315.mdex.features.largestonecolumn;

import net.minecraft.util.Mth;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.block.state.BlockState;

import com.github.mdcdi1315.mdex.features.config.LargeStoneColumnFeatureConfiguration;

public final class LargeStoneColumn
{
        private BlockPos root;
        private final boolean pointingUp;
        private int radius;
        private final double bluntness;
        private final double scale;

        public LargeStoneColumn(BlockPos root, boolean pointingUp, int radius, double bluntness, double scale)
        {
            this.root = root;
            this.pointingUp = pointingUp;
            this.radius = radius;
            this.bluntness = bluntness;
            this.scale = scale;
        }

        public int getHeight() {
            return this.getHeightAtRadius(0.0F);
        }

        public int getMinY() {
            return this.pointingUp ? this.root.getY() : this.root.getY() - this.getHeight();
        }

        public int getMaxY() {
            return !this.pointingUp ? this.root.getY() : this.root.getY() + this.getHeight();
        }

        public boolean moveBackUntilBaseIsInsideStoneAndShrinkRadiusIfNecessary(WorldGenLevel level, WindOffsetter windOffsetter)
        {
            while (this.radius > 1)
            {
                BlockPos.MutableBlockPos blockpos$mutableblockpos = this.root.mutable();
                int i = Math.min(10, this.getHeight());

                for (int j = 0; j < i; ++j)
                {
                    if (level.getBlockState(blockpos$mutableblockpos).is(Blocks.LAVA)) {
                        return false;
                    }

                    if (Utils.isCircleMostlyEmbeddedInStone(level, windOffsetter.offset(blockpos$mutableblockpos), this.radius)) {
                        this.root = blockpos$mutableblockpos;
                        return true;
                    }

                    blockpos$mutableblockpos.move(this.pointingUp ? Direction.DOWN : Direction.UP);
                }

                this.radius /= 2;
            }

            return false;
        }

        private int getHeightAtRadius(float radius) {
            return (int) Utils.getStoneHeight(radius, this.radius, this.scale, this.bluntness);
        }

        public void placeBlocks(WorldGenLevel level, RandomSource random, WindOffsetter windOffsetter , BlockState desiredstate)
        {
            for (int i = -this.radius; i <= this.radius; ++i)
            {
                for (int j = -this.radius; j <= this.radius; ++j)
                {
                    float f = Mth.sqrt((float)(i * i + j * j));
                    if (f <= (float)this.radius) {
                        int k = this.getHeightAtRadius(f);
                        if (k > 0) {
                            if (random.nextFloat() < 0.2f) {
                                k = (int)((float)k * Mth.randomBetween(random, 0.8F, 1.0F));
                            }

                            BlockPos.MutableBlockPos blockpos$mutableblockpos = this.root.offset(i, 0, j).mutable();
                            boolean flag = false;
                            int l = this.pointingUp ? level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, blockpos$mutableblockpos.getX(), blockpos$mutableblockpos.getZ()) : Integer.MAX_VALUE;

                            for (int i1 = 0; i1 < k && blockpos$mutableblockpos.getY() < l; ++i1)
                            {
                                BlockPos blockpos = windOffsetter.offset(blockpos$mutableblockpos);
                                if (Utils.isEmptyOrWaterOrLava(level, blockpos)) {
                                    flag = true;
                                    level.setBlock(blockpos, desiredstate, 2);
                                } else if (flag && level.getBlockState(blockpos).is(BlockTags.BASE_STONE_OVERWORLD)) {
                                    break;
                                }

                                blockpos$mutableblockpos.move(this.pointingUp ? Direction.UP : Direction.DOWN);
                            }
                        }
                    }
                }
            }

        }

        public boolean isSuitableForWind(LargeStoneColumnFeatureConfiguration config) {
            return this.radius >= config.minRadiusForWind && this.bluntness >= (double)config.minBluntnessForWind;
        }
    }
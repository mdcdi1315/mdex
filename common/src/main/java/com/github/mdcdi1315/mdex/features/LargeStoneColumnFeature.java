package com.github.mdcdi1315.mdex.features;

import com.github.mdcdi1315.mdex.features.largestonecolumn.*;
import com.github.mdcdi1315.mdex.features.config.LargeStoneColumnFeatureConfiguration;

import net.minecraft.util.Mth;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import com.mojang.serialization.Codec;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.Column;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

import java.util.Optional;

public final class LargeStoneColumnFeature
    extends ModdedFeature<LargeStoneColumnFeatureConfiguration>    
{
    public LargeStoneColumnFeature(Codec<LargeStoneColumnFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    protected boolean placeModdedFeature(FeaturePlaceContext<LargeStoneColumnFeatureConfiguration> fpc)
    {
        WorldGenLevel worldgenlevel = fpc.level();
        BlockPos blockpos = fpc.origin();
        LargeStoneColumnFeatureConfiguration cfg = fpc.config();
        RandomSource randomsource = fpc.random();
        if (Utils.isEmptyOrWater(worldgenlevel , blockpos))
        {
            Optional<Column> optional = Column.scan(worldgenlevel, blockpos, cfg.floorToCeilingSearchRange, Utils::isEmptyOrWater, (BlockState s) -> Utils.IsDesiredBlockOrLava(s , cfg.BlockState.BlockState.getBlock()));
            if (optional.isPresent() && optional.get() instanceof Column.Range range && range.height() >= 4)
            {
                int i = (int) ((float) range.height() * cfg.maxColumnRadiusToCaveHeightRatio);
                int j = Mth.clamp(i, cfg.columnRadius.getMinValue(), cfg.columnRadius.getMaxValue());
                int k = Mth.randomBetweenInclusive(randomsource, cfg.columnRadius.getMinValue(), j);
                LargeStoneColumn largestonec1 = makeColumn(blockpos.atY(range.ceiling() - 1), false, randomsource, k, cfg.stalactiteBluntness, cfg.heightScale);
                LargeStoneColumn largestonec2 = makeColumn(blockpos.atY(range.floor() + 1), true, randomsource, k, cfg.stalagmiteBluntness, cfg.heightScale);
                WindOffsetter largedripstonefeature$windoffsetter;
                if (largestonec1.isSuitableForWind(cfg) && largestonec2.isSuitableForWind(cfg)) {
                    largedripstonefeature$windoffsetter = new WindOffsetter(blockpos.getY(), randomsource, cfg.windSpeed);
                } else {
                    largedripstonefeature$windoffsetter = WindOffsetter.noWind();
                }

                if (largestonec1.moveBackUntilBaseIsInsideStoneAndShrinkRadiusIfNecessary(worldgenlevel, largedripstonefeature$windoffsetter)) {
                    largestonec1.placeBlocks(worldgenlevel, randomsource, largedripstonefeature$windoffsetter , cfg.BlockState.BlockState);
                }

                if (largestonec2.moveBackUntilBaseIsInsideStoneAndShrinkRadiusIfNecessary(worldgenlevel, largedripstonefeature$windoffsetter)) {
                    largestonec2.placeBlocks(worldgenlevel, randomsource, largedripstonefeature$windoffsetter, cfg.BlockState.BlockState);
                }

                return true;
            }
        }
        return false;
    }

    private static LargeStoneColumn makeColumn(BlockPos root, boolean pointingUp, RandomSource random, int radius, FloatProvider bluntnessBase, FloatProvider scaleBase) {
        return new LargeStoneColumn(root, pointingUp, radius, bluntnessBase.sample(random), scaleBase.sample(random));
    }

    private static void placeDebugMarkers(WorldGenLevel level, BlockPos pos, Column.Range range, WindOffsetter windOffsetter , Block desiredblock)
    {
        level.setBlock(windOffsetter.offset(pos.atY(range.ceiling() - 1)), Blocks.DIAMOND_BLOCK.defaultBlockState(), 2);
        level.setBlock(windOffsetter.offset(pos.atY(range.floor() + 1)), Blocks.GOLD_BLOCK.defaultBlockState(), 2);
        
        for (BlockPos.MutableBlockPos blockpos$mutableblockpos = pos.atY(range.floor() + 2).mutable(); 
             blockpos$mutableblockpos.getY() < range.ceiling() - 1; 
             blockpos$mutableblockpos.move(Direction.UP)) 
        {
            BlockPos blockpos = windOffsetter.offset(blockpos$mutableblockpos);
            if (Utils.IsEmptyOrWaterOrDesiredBlock(level , blockpos , desiredblock)) {
                level.setBlock(blockpos, Blocks.CREEPER_HEAD.defaultBlockState(), 2);
            }
        }
    }
}

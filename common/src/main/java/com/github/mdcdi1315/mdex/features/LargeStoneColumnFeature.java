package com.github.mdcdi1315.mdex.features;

import com.github.mdcdi1315.mdex.util.Extensions;
import com.github.mdcdi1315.mdex.features.largestonecolumn.*;
import com.github.mdcdi1315.mdex.features.config.LargeStoneColumnFeatureConfiguration;

import net.minecraft.core.BlockPos;
import com.mojang.serialization.Codec;
import net.minecraft.util.RandomSource;
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
            if (optional.isPresent() && optional.get() instanceof Column.Range range && range.height() > 3)
            {
                int i = (int) ((float) range.height() * cfg.maxColumnRadiusToCaveHeightRatio);
                int j = Extensions.Clamp(i, cfg.columnRadius.getMinValue(), cfg.columnRadius.getMaxValue());
                int k = Extensions.RandomBetweenInclusiveUnsafe(randomsource, cfg.columnRadius.getMinValue(), j);
                LargeStoneColumn largestonec1 = MakeColumn(blockpos.atY(range.ceiling() - 1), false, randomsource, k, cfg.stalactiteBluntness, cfg.heightScale);
                LargeStoneColumn largestonec2 = MakeColumn(blockpos.atY(range.floor() + 1), true, randomsource, k, cfg.stalagmiteBluntness, cfg.heightScale);
                WindOffsetter offsetter;
                if (largestonec1.isSuitableForWind(cfg) && largestonec2.isSuitableForWind(cfg)) {
                    offsetter = new WindOffsetter(blockpos.getY(), randomsource, cfg.windSpeed);
                } else {
                    offsetter = WindOffsetter.noWind();
                }

                if (largestonec1.moveBackUntilBaseIsInsideStoneAndShrinkRadiusIfNecessary(worldgenlevel, offsetter)) {
                    largestonec1.placeBlocks(worldgenlevel, randomsource, offsetter, cfg.BlockState.BlockState);
                }

                if (largestonec2.moveBackUntilBaseIsInsideStoneAndShrinkRadiusIfNecessary(worldgenlevel, offsetter)) {
                    largestonec2.placeBlocks(worldgenlevel, randomsource, offsetter, cfg.BlockState.BlockState);
                }

                return true;
            }
        }
        return false;
    }

    private static LargeStoneColumn MakeColumn(BlockPos root, boolean pointingUp, RandomSource random, int radius, FloatProvider bluntnessBase, FloatProvider scaleBase) {
        return new LargeStoneColumn(root, pointingUp, radius, bluntnessBase.sample(random), scaleBase.sample(random));
    }

    /*
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
     */
}

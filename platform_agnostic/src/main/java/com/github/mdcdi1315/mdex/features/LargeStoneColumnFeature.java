package com.github.mdcdi1315.mdex.features;

import com.github.mdcdi1315.basemodslib.utils.Extensions;

import com.github.mdcdi1315.mdex.block.BlockUtils;
import com.github.mdcdi1315.mdex.features.largestonecolumn.*;
import com.github.mdcdi1315.mdex.features.config.ModdedFeatureConfiguration;
import com.github.mdcdi1315.mdex.features.config.LargeStoneColumnFeatureConfigurationDetails;

import net.minecraft.core.BlockPos;
import com.mojang.serialization.Codec;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.Column;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

import java.util.Optional;

public final class LargeStoneColumnFeature
    extends ModdedFeature<ModdedFeatureConfiguration<LargeStoneColumnFeatureConfigurationDetails>>
{
    public LargeStoneColumnFeature(Codec<ModdedFeatureConfiguration<LargeStoneColumnFeatureConfigurationDetails>> codec) {
        super(codec);
    }

    @Override
    protected boolean PlaceModdedFeature(FeaturePlaceContext<ModdedFeatureConfiguration<LargeStoneColumnFeatureConfigurationDetails>> fpc)
    {
        LargeStoneColumnFeatureConfigurationDetails cfg = fpc.config().Details;
        BlockPos blockpos = fpc.origin();
        RandomSource randomsource = fpc.random();
        WorldGenLevel worldgenlevel = fpc.level();
        if (BlockUtils.BlockIsEmptyOrWaterUnsafe(worldgenlevel , blockpos))
        {
            Optional<Column> optional = Column.scan(worldgenlevel, blockpos, cfg.floorToCeilingSearchRange, BlockUtils::ReferentIsEmptyOrWaterUnsafe, (BlockState s) -> s.is(Blocks.LAVA) || s.is(cfg.BlockState.BlockState.getBlock()));
            if (optional.isPresent() && optional.get() instanceof Column.Range range && range.height() > 3)
            {
                int radius = Extensions.RandomBetweenInclusiveUnsafe(randomsource, cfg.columnRadius.getMinValue(), Extensions.Clamp((int) ((float) range.height() * cfg.maxColumnRadiusToCaveHeightRatio), cfg.columnRadius.getMinValue(), cfg.columnRadius.getMaxValue()));
                LargeStoneColumn largestonec1 = new LargeStoneColumn(blockpos.atY(range.ceiling() - 1), false, radius, cfg.stalactiteBluntness.sample(randomsource), cfg.heightScale.sample(randomsource));
                LargeStoneColumn largestonec2 = new LargeStoneColumn(blockpos.atY(range.floor() + 1), true, radius, cfg.stalagmiteBluntness.sample(randomsource), cfg.heightScale.sample(randomsource));
                WindOffsetter offsetter;
                if (largestonec1.IsSuitableForWind(cfg.minRadiusForWind , cfg.minBluntnessForWind) && largestonec2.IsSuitableForWind(cfg.minRadiusForWind , cfg.minBluntnessForWind)) {
                    offsetter = new WindOffsetter(blockpos.getY(), randomsource, cfg.windSpeed);
                } else {
                    offsetter = WindOffsetter.NoWind();
                }

                if (largestonec1.MoveBackUntilBaseIsInsideStoneAndShrinkRadiusIfNecessary(worldgenlevel, offsetter)) {
                    largestonec1.PlaceBlocks(worldgenlevel, randomsource, offsetter, cfg.BlockState.BlockState);
                }

                if (largestonec2.MoveBackUntilBaseIsInsideStoneAndShrinkRadiusIfNecessary(worldgenlevel, offsetter)) {
                    largestonec2.PlaceBlocks(worldgenlevel, randomsource, offsetter, cfg.BlockState.BlockState);
                }

                return true;
            }
        }
        return false;
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

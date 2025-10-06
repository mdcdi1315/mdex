package com.github.mdcdi1315.mdex.features;

import com.github.mdcdi1315.mdex.block.BlockUtils;
import com.github.mdcdi1315.mdex.features.config.ModdedSpringFeatureConfiguration;

import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public final class ModdedSpringFeature
    extends ModdedFeature<ModdedSpringFeatureConfiguration>
{
    public ModdedSpringFeature(Codec<ModdedSpringFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    protected boolean placeModdedFeature(FeaturePlaceContext<ModdedSpringFeatureConfiguration> fpc) {
        ModdedSpringFeatureConfiguration springconfiguration = fpc.config();
        WorldGenLevel worldgenlevel = fpc.level();
        BlockPos blockpos = fpc.origin();
        var vbs = springconfiguration.ValidBlocks;
        if (!BlockUtils.IsABlockFromListUnsafe(vbs , worldgenlevel.getBlockState(blockpos.above()))) {
            return false;
        } else if (springconfiguration.RequiresBlockBelow && !BlockUtils.IsABlockFromListUnsafe(vbs , worldgenlevel.getBlockState(blockpos.below()))) {
            return false;
        } else {
            BlockState blockstate = worldgenlevel.getBlockState(blockpos);
            if (BlockUtils.ReferentIsAirBlockUnsafe(blockstate) || BlockUtils.IsABlockFromListUnsafe(vbs, blockstate))
            {
                byte j = 0, k = 0;
                if (BlockUtils.IsABlockFromListUnsafe(vbs, worldgenlevel.getBlockState(blockpos.west()))) {
                    ++j;
                }

                if (BlockUtils.IsABlockFromListUnsafe(vbs, worldgenlevel.getBlockState(blockpos.east()))) {
                    ++j;
                }

                if (BlockUtils.IsABlockFromListUnsafe(vbs, worldgenlevel.getBlockState(blockpos.north()))) {
                    ++j;
                }

                if (BlockUtils.IsABlockFromListUnsafe(vbs, worldgenlevel.getBlockState(blockpos.south()))) {
                    ++j;
                }

                if (BlockUtils.IsABlockFromListUnsafe(vbs, worldgenlevel.getBlockState(blockpos.below()))) {
                    ++j;
                }

                if (worldgenlevel.isEmptyBlock(blockpos.west())) {
                    ++k;
                }

                if (worldgenlevel.isEmptyBlock(blockpos.east())) {
                    ++k;
                }

                if (worldgenlevel.isEmptyBlock(blockpos.north())) {
                    ++k;
                }

                if (worldgenlevel.isEmptyBlock(blockpos.south())) {
                    ++k;
                }

                if (worldgenlevel.isEmptyBlock(blockpos.below())) {
                    ++k;
                }

                if (j == springconfiguration.RockCount && k == springconfiguration.HoleCount) {
                    var fs = springconfiguration.Fluid.FluidState;
                    worldgenlevel.setBlock(blockpos, fs.createLegacyBlock(), 2);
                    worldgenlevel.scheduleTick(blockpos, fs.getType(), 0);
                    return true;
                }
            }
            return false;
        }
    }
}

package com.github.mdcdi1315.mdex.features;

import com.github.mdcdi1315.mdex.features.config.ModdedFeatureConfiguration;
import com.github.mdcdi1315.mdex.features.config.ModdedSimpleBlockFeatureConfigurationDetails;

import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public final class ModdedSimpleBlockFeature
    extends ModdedFeature<ModdedFeatureConfiguration<ModdedSimpleBlockFeatureConfigurationDetails>>
{
    public ModdedSimpleBlockFeature(Codec<ModdedFeatureConfiguration<ModdedSimpleBlockFeatureConfigurationDetails>> codec) {
        super(codec);
    }

    @Override
    protected boolean PlaceModdedFeature(FeaturePlaceContext<ModdedFeatureConfiguration<ModdedSimpleBlockFeatureConfigurationDetails>> fpc) {
        ModdedSimpleBlockFeatureConfigurationDetails simpleblockconfiguration = fpc.config().Details;
        WorldGenLevel worldgenlevel = fpc.level();
        BlockPos blockpos = fpc.origin();
        BlockState blockstate = simpleblockconfiguration.ToPlace.GetBlockState(worldgenlevel, fpc.random(), blockpos);
        if (blockstate.canSurvive(worldgenlevel, blockpos)) {
            if (blockstate.getBlock() instanceof DoublePlantBlock) {
                if (!worldgenlevel.isEmptyBlock(blockpos.above())) {
                    return false;
                }

                DoublePlantBlock.placeAt(worldgenlevel, blockstate, blockpos, 2);
            } else {
                worldgenlevel.setBlock(blockpos, blockstate, 2);
            }

            return true;
        } else {
            return false;
        }
    }
}

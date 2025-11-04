package com.github.mdcdi1315.mdex.features;

import com.github.mdcdi1315.mdex.util.SingleTargetBlockState;
import com.github.mdcdi1315.mdex.features.config.ModdedFeatureConfiguration;
import com.github.mdcdi1315.mdex.features.config.ModdedOreFeatureConfigurationDetails;

import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public final class ModdedScatteredOreFeature
    extends ModdedFeature<ModdedFeatureConfiguration<ModdedOreFeatureConfigurationDetails>>
{
    public ModdedScatteredOreFeature(Codec<ModdedFeatureConfiguration<ModdedOreFeatureConfigurationDetails>> codec) {
        super(codec);
    }

    @Override
    protected boolean PlaceModdedFeature(FeaturePlaceContext<ModdedFeatureConfiguration<ModdedOreFeatureConfigurationDetails>> fpc)
    {
        WorldGenLevel worldgenlevel = fpc.level();
        RandomSource randomsource = fpc.random();
        ModdedOreFeatureConfigurationDetails oreconfiguration = fpc.config().Details;
        BlockPos blockpos = fpc.origin();
        BlockPos.MutableBlockPos current = new BlockPos.MutableBlockPos();

        int attempts = randomsource.nextInt(oreconfiguration.Size + 1);
        boolean atleastone = false;

        for (int j = 0; j < attempts; ++j)
        {
            OffsetTargetPos(current, randomsource, blockpos, Math.min(j, 7));
            BlockState blockstate = worldgenlevel.getBlockState(current);

            for (SingleTargetBlockState targetblockstate : oreconfiguration.TargetStates)
            {
                if (ModdedOreFeature.CanPlaceOre(blockstate, worldgenlevel::getBlockState, randomsource, oreconfiguration.DiscardChanceOnAirExposure, targetblockstate, current))
                {
                    if (worldgenlevel.setBlock(current, targetblockstate.State.BlockState, 2)) {
                        atleastone = true;
                    }
                    break;
                }
            }
        }

        return atleastone;
    }

    private static void OffsetTargetPos(BlockPos.MutableBlockPos mutablePos, RandomSource random, BlockPos pos, int magnitude)
    {
        mutablePos.setWithOffset(
                pos,
                GetRandomPlacementInOneAxisRelativeToOrigin(random, magnitude),
                GetRandomPlacementInOneAxisRelativeToOrigin(random, magnitude),
                GetRandomPlacementInOneAxisRelativeToOrigin(random, magnitude)
        );
    }

    private static int GetRandomPlacementInOneAxisRelativeToOrigin(RandomSource random, int magnitude) {
        return Math.round((random.nextFloat() - random.nextFloat()) * (float)magnitude);
    }
}


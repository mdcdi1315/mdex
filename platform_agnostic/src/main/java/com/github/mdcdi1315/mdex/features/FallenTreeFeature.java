package com.github.mdcdi1315.mdex.features;

import com.github.mdcdi1315.DotNetLayer.System.Predicate;

import com.github.mdcdi1315.basemodslib.utils.Extensions;

import com.github.mdcdi1315.mdex.block.BlockUtils;
import com.github.mdcdi1315.mdex.features.config.ModdedFeatureConfiguration;
import com.github.mdcdi1315.mdex.features.config.FallenTreeConfigurationDetails;

import net.minecraft.core.Holder;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.core.Direction;
import com.mojang.serialization.Codec;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;


public final class FallenTreeFeature
    extends ModdedFeature<ModdedFeatureConfiguration<FallenTreeConfigurationDetails>>
{
    public FallenTreeFeature(Codec<ModdedFeatureConfiguration<FallenTreeConfigurationDetails>> codec) {
        super(codec);
    }

    @Override
    protected boolean PlaceModdedFeature(FeaturePlaceContext<ModdedFeatureConfiguration<FallenTreeConfigurationDetails>> fpc)
    {
        // Check that the current block is solid, and it's top one is air.

        RandomSource rs = fpc.random();
        WorldGenLevel wgl = fpc.level();
        BlockPos startpos = fpc.origin();
        Block log = fpc.config().Details.LogTypeProvider;
        Predicate<BlockState> replaceable = FeaturePlacementUtils.IsReplaceable(BlockTags.FEATURES_CANNOT_REPLACE);

        if (!BlockUtils.BlockIsSolidAndAboveIsAirUnsafe(wgl , startpos)) { return false; }

        startpos = startpos.above();

        // Place the first log. This will always look to the world's y coordinate.
        if (!FeaturePlacementUtils.SafeSetBlock(wgl , startpos , log.defaultBlockState().setValue(RotatedPillarBlock.AXIS , Direction.Axis.Y) , replaceable))
        {
            return false;
        }

        // Get a random direction.
        Direction fd = Extensions.GetRandomDirectionExcludingUpDown(rs);

        byte placed = 0; // Number of fallen logs placed.

        // Calculate the size of the fallen trunk
        int ts = fpc.config().Details.FallenTrunkSize.sample(rs);

        // Compute fallen trunk start position.
        startpos = startpos.relative(fd , 2);

        // Start position has been defined, we can now proceed with placement.

        BlockPos temp;
        // The below is the block state to use for placing the log.
        BlockState computedstate = log.defaultBlockState().setValue(RotatedPillarBlock.AXIS , fd.getAxis());
        for (int I = 0; I < ts; I++)
        {
            temp = startpos.relative(fd , I);
            if (
                    !wgl.getBlockState(temp.below()).isAir() &&
                    FeaturePlacementUtils.SafeSetBlock(wgl, temp , computedstate , replaceable)
            ) { placed++; } else { break; }
        }

        Holder<PlacedFeature> patch;

        // Place the vegetation patch, if possible and if allowed by the probability value
        if (placed >= (ts / 2) && rs.nextFloat() < fpc.config().Details.VegetationPatchPlacementProbability && (patch = fpc.config().Details.VegetationPatch).isBound())
        {
            patch.value().place(fpc.level() , fpc.chunkGenerator(), fpc.random() , startpos.relative(fd , ts / 2).above());
        }

        return placed > 1;
    }
}


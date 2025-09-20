package com.github.mdcdi1315.mdex.features;

import com.github.mdcdi1315.DotNetLayer.System.Predicate;

import com.github.mdcdi1315.mdex.util.Extensions;
import com.github.mdcdi1315.mdex.block.BlockUtils;
import com.github.mdcdi1315.mdex.features.config.FallenTreeConfiguration;

import net.minecraft.core.Holder;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.core.Direction;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;


public final class FallenTreeFeature
    extends ModdedFeature<FallenTreeConfiguration>
{
    public FallenTreeFeature(Codec<FallenTreeConfiguration> codec) {
        super(codec);
    }

    @Override
    protected boolean placeModdedFeature(FeaturePlaceContext<FallenTreeConfiguration> fpc)
    {
        // Check that the current block is solid, and it's top one is air.

        WorldGenLevel wgl = fpc.level();
        BlockPos startpos = fpc.origin();
        Block log = fpc.config().LogTypeProvider;
        Predicate<BlockState> replaceable = FeaturePlacementUtils.IsReplaceable(BlockTags.FEATURES_CANNOT_REPLACE);

        if (!BlockUtils.BlockIsSolidAndAboveIsAir(wgl , startpos)) { return false; }

        // Calculate the size of the fallen trunk
        int ts = fpc.config().FallenTrunkSize.sample(fpc.random());

        // Get a random direction.
        Direction fd = Extensions.GetRandomDirectionExcludingUpDown(fpc.random());

        // Place the first log. This will always look to the world's y coordinate.
        if (!FeaturePlacementUtils.SafeSetBlock(wgl , startpos , log.defaultBlockState().setValue(RotatedPillarBlock.AXIS , Direction.Axis.Y) , replaceable))
        {
            return false;
        }

        startpos = startpos.relative(fd , 1);

        // Place the air block
        if (!FeaturePlacementUtils.SafeSetBlock(wgl , startpos , Blocks.AIR.defaultBlockState() , replaceable))
        {
            return false;
        }

        startpos = startpos.relative(fd , 1);
        byte placed = 0; // Number of fallen logs placed.

        // Start position has been defined, we can now proceed with placement.

        BlockPos temp;
        BlockState computedstate = log.defaultBlockState().setValue(RotatedPillarBlock.AXIS , fd.getAxis());
        for (int I = 0; I < ts; I++)
        {
            temp = startpos.relative(fd , I);
            if (
                    !wgl.getBlockState(temp.below()).isAir() &&
                    FeaturePlacementUtils.SafeSetBlock(wgl, temp , computedstate , replaceable)
            )
            {
                placed++;
            }
        }

        Holder<PlacedFeature> patch;

        // Place the vegetation patch, if possible
        if ((patch = fpc.config().VegetationPatch).isBound() && placed >= (ts / 2))
        {
            patch.value().place(fpc.level() , fpc.chunkGenerator(), fpc.random() , startpos.relative(fd , ts / 2));
        }

        return placed > 1;
    }
}


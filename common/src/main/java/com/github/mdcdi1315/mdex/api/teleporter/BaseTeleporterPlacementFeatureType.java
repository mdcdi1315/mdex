package com.github.mdcdi1315.mdex.api.teleporter;

import com.github.mdcdi1315.DotNetLayer.System.Predicate;
import com.github.mdcdi1315.mdex.MDEXBalmLayer;
import com.github.mdcdi1315.mdex.block.ModBlocks;
import com.github.mdcdi1315.mdex.features.FeaturePlacementUtils;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public final class BaseTeleporterPlacementFeatureType
    extends Feature<BaseTeleporterPlacementFeatureConfiguration>
{
    public BaseTeleporterPlacementFeatureType(Codec<BaseTeleporterPlacementFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<BaseTeleporterPlacementFeatureConfiguration> fpc)
    {
        if (!fpc.config().IsCompiled())
        {
            MDEXBalmLayer.LOGGER.error("Cannot place the feature because it's configuration is invalid.");
            return false;
        }
        // The specified position is where the entity should be placed to.
        // If the place is darky, a light block state as specified by the block state provider will be placed around the teleporter block as well.

        // First check that we can place at the specified position.
        // Ignored if specified before generating.
        WorldGenLevel wgl = fpc.level();

        BlockPos pos = fpc.origin();

        /*
        MDEXBalmLayer.LOGGER.info("Ignore empty space check: {}" , fpc.config().IgnoreEmptySpaceCheck);
        if (!fpc.config().IgnoreEmptySpaceCheck)
        {
            for (int I = 0; I < 3; I++)
            {
                if (BlockUtils.ReferentIsSolidBlock(wgl.getBlockState(pos.above(I))))
                {
                    MDEXBalmLayer.LOGGER.error("MDEXTELEPORTER_EVENTS: Failed to assert the block at {}." , pos.above(I));
                    return false;
                }
            }
        } */

        // OK, it is clean enough to place the teleporter

        // Get the lower-bound position by moving one Z and one X backwards.
        // Start from the current Y level up to 3 blocks.

        Predicate<BlockState> replaceable = FeaturePlacementUtils.IsReplaceable(BlockTags.FEATURES_CANNOT_REPLACE);

        int ydownlayer = pos.getY() - 1;

        var basestoneprovider = fpc.config().Base_Plate_Provider;

        var rs = fpc.random();

        for (BlockPos temp : FeaturePlacementUtils.GetRectangularArea(pos.offset(-1 , -1, -1) , new BlockPos(3 , 4, 3)))
        {
            if (temp.getY() == ydownlayer) {
                if (!FeaturePlacementUtils.SafeSetBlock(wgl , temp , basestoneprovider.getState(rs , temp) , replaceable))
                {
                    MDEXBalmLayer.LOGGER.error("MDEXTELEPORTER_EVENTS: Failed to place a plate block at {}." , temp);
                    return false;
                }
            } else if (!FeaturePlacementUtils.SafeSetBlock(wgl , temp , Blocks.CAVE_AIR.defaultBlockState() , replaceable)) {
                MDEXBalmLayer.LOGGER.error("MDEXTELEPORTER_EVENTS: Failed to place an air block at {}." , temp);
                return false;
            }
        }

        if (!wgl.setBlock(pos , ModBlocks.TELEPORTER.defaultBlockState() , 2))
        {
            MDEXBalmLayer.LOGGER.error("MDEXTELEPORTER_EVENTS: Failed to place the teleporter down.");
            return false;
        }

        if (wgl.getMaxLocalRawBrightness(pos) < 4)
        {
            var lightblockprovider = fpc.config().Light_Block_Provider;
            for (BlockPos temp : new BlockPos[] {
                    pos.offset(-1 , 0, -1),
                    pos.offset(-1 , 0, 1),
                    pos.offset(1 , 0 , -1),
                    pos.offset(1 , 0 , 1),
            })
            {
                if (!wgl.setBlock(temp , lightblockprovider.getState(rs , temp) , 2))
                {
                    MDEXBalmLayer.LOGGER.error("MDEXTELEPORTER_EVENTS: Failed to place α light block at {}." , temp);
                    return false;
                }
            }
        }

        return true;
    }
}
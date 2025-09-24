package com.github.mdcdi1315.mdex.api.teleporter;

import com.github.mdcdi1315.DotNetLayer.System.Predicate;

import com.github.mdcdi1315.mdex.MDEXBalmLayer;
import com.github.mdcdi1315.mdex.block.ModBlocks;
import com.github.mdcdi1315.mdex.util.Extensions;
import com.github.mdcdi1315.mdex.block.BlockUtils;
import com.github.mdcdi1315.mdex.features.FeaturePlacementUtils;

import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.WorldGenLevel;
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

        // The specified position is where the teleporter block should be placed to.
        // If the place is darky, a light block state as specified by the block state provider will be placed around the teleporter block as well.

        Predicate<BlockState> replaceable = FeaturePlacementUtils.IsReplaceable(BlockTags.FEATURES_CANNOT_REPLACE);

        BlockPos pos = fpc.origin();

        var rs = fpc.random();

        int ydownlayer = pos.getY() - 1;

        // Get the lower-bound position by moving by requested Z and X backwards.
        // Start from the current Y level up to 3 blocks plus the desired size of the feature.

        int size = fpc.config().Size.sample(rs);

        var basestoneprovider = fpc.config().Base_Plate_Provider;

        WorldGenLevel wgl = fpc.level();

        for (BlockPos temp : FeaturePlacementUtils.GetRectangularArea(pos.offset(-size , -1, -size) , new BlockPos(2+size , 4, 2+size)))
        {
            if (temp.getY() == ydownlayer) {
                if (!FeaturePlacementUtils.SafeSetBlock(wgl , temp , basestoneprovider.GetBlockState(wgl, rs , temp) , replaceable))
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
                    pos.offset(-size , 0, -size),
                    pos.offset(-size , 0, size),
                    pos.offset(size , 0 , -size),
                    pos.offset(size , 0 , size),
            })
            {
                if (!wgl.setBlock(temp , lightblockprovider.GetBlockState(wgl, rs , temp) , 2))
                {
                    MDEXBalmLayer.LOGGER.error("MDEXTELEPORTER_EVENTS: Failed to place α light block at {}." , temp);
                    return false;
                }
            }
        }

        if (fpc.config().PlaceStarterChest)
        {
            var si = fpc.config().ChestPlacement;
            if (si.Probability > 0f && rs.nextFloat() < si.Probability) {
                BlockPos chestpos = pos.relative(Extensions.GetRandomDirectionExcludingUpDown(rs) , size == 1 ? 1 : size / 2);
                if (!wgl.setBlock(chestpos , si.ContainerState.GetBlockState(wgl, rs , chestpos) , 2)) {
                    MDEXBalmLayer.LOGGER.info("MDEXTELEPORTER_EVENTS: Failed to place the starter chest for this dimension.");
                }
                if (!BlockUtils.SetRandomizableContainerLootTable(wgl , rs , chestpos , si.LootTable)) {
                    MDEXBalmLayer.LOGGER.info("MDEXTELEPORTER_EVENTS: Failed to create the loot table for the starter chest. Removing it.");
                    wgl.setBlock(chestpos , Blocks.CAVE_AIR.defaultBlockState() , 2);
                }
            }
        }

        return true;
    }
}
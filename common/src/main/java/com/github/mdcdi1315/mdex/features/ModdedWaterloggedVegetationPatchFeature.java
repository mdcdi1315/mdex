package com.github.mdcdi1315.mdex.features;

import com.github.mdcdi1315.DotNetLayer.System.Predicate;
import com.github.mdcdi1315.mdex.features.config.ModdedVegetationPatchConfiguration;

import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.HashSet;
import java.util.Set;

public class ModdedWaterloggedVegetationPatchFeature
        extends ModdedVegetationPatchFeature
{
    public ModdedWaterloggedVegetationPatchFeature(Codec<ModdedVegetationPatchConfiguration> codec) {
        super(codec);
    }

    protected Set<BlockPos> placeGroundPatch(FeaturePlaceContext<ModdedVegetationPatchConfiguration> context, BlockPos pos, Predicate<BlockState> state, int xRadius, int zRadius)
    {
        var level = context.level();
        Set<BlockPos> set = new HashSet<>();
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

        for (BlockPos blockpos : super.placeGroundPatch(context, pos, state, xRadius, zRadius)) {
            if (!isExposed(level, blockpos, mutable)) {
                set.add(blockpos);
                // OPT: Instead of iterating the found elements again, execute what we want to do here.
                level.setBlock(blockpos, Blocks.WATER.defaultBlockState(), 2);
            }
        }

        return set;
    }

    private static boolean isExposed(BlockGetter level, BlockPos pos, BlockPos.MutableBlockPos mutablePos) {
        return isExposedDirection(level, pos, mutablePos, Direction.NORTH) ||
                isExposedDirection(level, pos, mutablePos, Direction.EAST) ||
                isExposedDirection(level, pos, mutablePos, Direction.SOUTH) ||
                isExposedDirection(level, pos, mutablePos, Direction.WEST) ||
                isExposedDirection(level, pos, mutablePos, Direction.DOWN);
    }

    private static boolean isExposedDirection(BlockGetter level, BlockPos pos, BlockPos.MutableBlockPos mutablePos, Direction direction) {
        mutablePos.setWithOffset(pos, direction);
        return !level.getBlockState(mutablePos).isFaceSturdy(level, mutablePos, direction.getOpposite());
    }

    protected boolean placeVegetation(FeaturePlaceContext<ModdedVegetationPatchConfiguration> context, BlockPos pos)
    {
        boolean placed = super.placeVegetation(context, pos.below());
        if (placed) {
            var level = context.level();
            BlockState blockstate = level.getBlockState(pos);
            if (blockstate.hasProperty(BlockStateProperties.WATERLOGGED) && !(Boolean)blockstate.getValue(BlockStateProperties.WATERLOGGED)) {
                level.setBlock(pos, blockstate.setValue(BlockStateProperties.WATERLOGGED, true), 2);
            }
        }
        return placed;
    }
}

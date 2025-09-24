package com.github.mdcdi1315.mdex.block.blockstateproviders;

import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.MaybeNull;
import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.DisallowNull;

import com.github.mdcdi1315.mdex.block.BlockUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Provides the context for running block state providers. <br />
 * Apart from providing the required data for running the context,
 * it defines also and some utility methods.
 * @param getter The {@link BlockGetter} instance to read block states from. Not all block state providers do need this. See their doc for more info.
 * @param source The {@link RandomSource} instance to use.
 * @param position The {@link BlockPos} that represents the requested position.
 */
public record BlockStateProviderContext(@MaybeNull BlockGetter getter , @DisallowNull RandomSource source , @DisallowNull BlockPos position)
{
    public BlockStateProviderContext(BlockGetter getter, RandomSource source , BlockPos position)
    {
        this.getter = getter;
        this.source = source;
        this.position = position;
    }

    public BlockStateProviderContext(RandomSource source , BlockPos position)
    {
        this(null , source , position);
    }

    public BlockState GetStateAtPosition() {
        return getter.getBlockState(position);
    }

    public boolean IsEmptyFluidAtPosition() {
        return BlockUtils.IsEmptyFluid(getter , position);
    }

    public boolean StateAtPositionMatchesWith(BlockState other) {
        return BlockUtils.BlockStatesMatch(GetStateAtPosition() , other);
    }
}

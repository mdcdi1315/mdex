package com.github.mdcdi1315.mdex.block.blockstateproviders;

import com.github.mdcdi1315.mdex.block.BlockUtils;
import com.github.mdcdi1315.mdex.util.CompilableBlockState;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.RotatedPillarBlock;

public final class RotatedBlockProvider
        extends AbstractBlockStateProvider
{
    public final CompilableBlockState Block;

    public RotatedBlockProvider(CompilableBlockState block) {
        Block = block;
    }

    @Override
    public BlockState GetBlockState(BlockStateProviderContext context) {
        // Use the desired block state that the user wants to, but set the axis property right after all the properties are defined in the provider itself
        return Block.BlockState.setValue(RotatedPillarBlock.AXIS, Direction.Axis.getRandom(context.source()));
    }

    @Override
    public AbstractBlockStateProviderType<?> GetType() {
        return RotatedBlockProviderType.INSTANCE;
    }

    @Override
    protected boolean CompileImplementation() {
        Block.Compile();
        boolean compiled = Block.IsCompiled();
        if (compiled) {
            BlockUtils.RequireBlockPropertyOrFail(Block.BlockState.getBlock() , RotatedPillarBlock.AXIS.getName());
        }
        return compiled;
    }
}

package com.github.mdcdi1315.mdex.block.blockstateproviders;

import com.github.mdcdi1315.DotNetLayer.System.ArgumentNullException;
import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.MaybeNull;

import com.github.mdcdi1315.mdex.util.CompilableBlockState;

import net.minecraft.world.level.block.state.BlockState;

public abstract class HasFallbackAbstractBlockStateProvider
    extends AbstractBlockStateProvider
{
    public CompilableBlockState FallbackState;

    public HasFallbackAbstractBlockStateProvider(CompilableBlockState state)
        throws ArgumentNullException
    {
        ArgumentNullException.ThrowIfNull(state, "state");
        FallbackState = state;
    }

    @MaybeNull
    protected abstract BlockState GetNormalBlockState(BlockStateProviderContext context);

    @Override
    public BlockState GetBlockState(BlockStateProviderContext context) {
        BlockState s = GetNormalBlockState(context);
        return s == null ? FallbackState.BlockState : s;
    }

    @Override
    protected boolean CompileImplementation() {
        FallbackState.Compile();
        return FallbackState.IsCompiled();
    }
}

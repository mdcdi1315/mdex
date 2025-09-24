package com.github.mdcdi1315.mdex.block.blockstateproviders;

import com.github.mdcdi1315.mdex.util.CompilableBlockState;

import net.minecraft.world.level.block.state.BlockState;

public final class SimpleStateProvider
    extends AbstractBlockStateProvider
{
    public CompilableBlockState State;

    public SimpleStateProvider(CompilableBlockState s) {
        State = s;
    }

    @Override
    public BlockState GetBlockState(BlockStateProviderContext context) {
        return State.BlockState;
    }

    @Override
    public AbstractBlockStateProviderType<?> GetType() {
        return SimpleStateProviderType.INSTANCE;
    }

    @Override
    protected boolean CompileImplementation() {
        State.Compile();
        boolean c = State.IsCompiled();
        if (!c) { State = null; }
        return c;
    }
}

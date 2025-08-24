package com.github.mdcdi1315.mdex.block.blockstateproviders;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import com.github.mdcdi1315.mdex.util.CompilableTargetBlockState;

public class SimpleStateProvider
        extends AbstractBlockStateProvider
{
    public final CompilableTargetBlockState state;

    protected SimpleStateProvider(CompilableTargetBlockState state) {
        this.state = state;
    }

    @Override
    protected AbstractBlockStateProviderType<?> type() {
        return CustomBlockStateProviderRegistrySubsystem.SIMPLE_STATE_PROVIDER;
    }

    public BlockState getState(RandomSource random, BlockPos pos) {
        return this.state.BlockState;
    }

    @Override
    public void Compile() {
        state.Compile();
    }

    @Override
    public boolean IsCompiled() {
        return state.IsCompiled();
    }
}

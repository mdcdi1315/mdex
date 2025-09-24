package com.github.mdcdi1315.mdex.block.blockstateproviders;

import com.github.mdcdi1315.mdex.util.Extensions;
import com.github.mdcdi1315.mdex.util.CompilableBlockState;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class NoiseStateProvider
    extends AbstractNoiseStateProvider
{
    protected List<CompilableBlockState> States;

    public NoiseStateProvider(NoiseStateProviderData data , List<CompilableBlockState> states) {
        super(data);
        States = states;
    }

    @Override
    public BlockState GetBlockState(BlockStateProviderContext context) {
        return this.GetRandomState(context.source(), States, context.position(), this.Data.scale());
    }

    protected BlockState GetRandomState_2(RandomSource rs, List<BlockState> possibleStates, BlockPos pos, double delta) {
        return this.GetRandomState_2(possibleStates, this.GetNoiseValue(rs , pos, delta));
    }

    protected BlockState GetRandomState_2(List<BlockState> possibleStates, double delta) {
        return possibleStates.get((int)(Extensions.Clamp(((double)1.0F + delta) / (double)2.0F, 0.0F, 0.9999) * possibleStates.size()));
    }

    protected BlockState GetRandomState(RandomSource rs, List<CompilableBlockState> possibleStates, BlockPos pos, double delta) {
        return this.GetRandomState(possibleStates, this.GetNoiseValue(rs , pos, delta));
    }

    protected BlockState GetRandomState(List<CompilableBlockState> possibleStates, double delta) {
        return possibleStates.get((int)(Extensions.Clamp(((double)1.0F + delta) / (double)2.0F, 0.0F, 0.9999) * possibleStates.size())).BlockState;
    }

    @Override
    public AbstractBlockStateProviderType<?> GetType() {
        return NoiseStateProviderType.INSTANCE;
    }

    @Override
    protected boolean CompileImplementation() {
        boolean c = Extensions.CompileAllOrFail(States);
        if (!c) { States = null; }
        return c;
    }
}

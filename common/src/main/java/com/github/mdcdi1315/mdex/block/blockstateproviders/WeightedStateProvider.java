package com.github.mdcdi1315.mdex.block.blockstateproviders;

import com.github.mdcdi1315.mdex.util.CompilableBlockState;

import com.github.mdcdi1315.DotNetLayer.System.InvalidOperationException;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.util.random.SimpleWeightedRandomList;

import java.util.Optional;

public final class WeightedStateProvider
    extends AbstractBlockStateProvider
{
    public SimpleWeightedRandomList<CompilableBlockState> States;

    public WeightedStateProvider(SimpleWeightedRandomList<CompilableBlockState> states) {
        States = states;
    }

    private static BlockState Mapper(CompilableBlockState s) {
        return s.BlockState;
    }

    @Override
    public BlockState GetBlockState(BlockStateProviderContext context) {
        Optional<CompilableBlockState> obs = States.getRandomValue(context.source());
        return obs.map(WeightedStateProvider::Mapper).orElseThrow(() -> new InvalidOperationException("Cannot find a block state to use!"));
    }

    @Override
    public AbstractBlockStateProviderType<?> GetType() {
        return WeightedStateProviderType.INSTANCE;
    }

    @Override
    protected boolean CompileImplementation()
    {
        for (var s : States.unwrap())
        {
            var cs = s.data();
            cs.Compile();
            if (!cs.IsCompiled()) {
                States = null;
                return false;
            }
        }
        return true;
    }
}

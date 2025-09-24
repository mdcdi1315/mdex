package com.github.mdcdi1315.mdex.block.blockstateproviders;

import com.github.mdcdi1315.mdex.util.Extensions;
import com.github.mdcdi1315.mdex.util.CompilableBlockState;
import com.github.mdcdi1315.mdex.util.SingleTargetBlockState;

import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public final class RuleTestBasedBlockStateProvider
    extends AbstractBlockStateProvider
{
    public List<SingleTargetBlockState> RuleTargets;
    public CompilableBlockState FallbackState;

    public RuleTestBasedBlockStateProvider(List<SingleTargetBlockState> targets , CompilableBlockState fallback)
    {
        RuleTargets = targets;
        FallbackState = fallback;
    }

    @Override
    public BlockState GetBlockState(BlockStateProviderContext context) {
        for (var t : RuleTargets)
        {
            if (t.Target.test(context.GetStateAtPosition() , context.source())) {
                return t.State.BlockState;
            }
        }
        return FallbackState.BlockState;
    }

    @Override
    public AbstractBlockStateProviderType<?> GetType() {
        return RuleTestBasedBlockStateProviderType.INSTANCE;
    }

    @Override
    protected boolean CompileImplementation() {
        if (Extensions.CompileAllOrFail(RuleTargets))
        {
            FallbackState.Compile();
            return FallbackState.IsCompiled();
        }
        return false;
    }
}

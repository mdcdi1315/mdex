package com.github.mdcdi1315.mdex.block.blockstateproviders;

import com.github.mdcdi1315.DotNetLayer.System.ArgumentNullException;

import com.github.mdcdi1315.mdex.dco_logic.DCOUtils;
import com.github.mdcdi1315.mdex.util.CompilableBlockState;
import com.github.mdcdi1315.mdex.util.SingleTargetBlockState;

import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public final class RuleTestBasedBlockStateProvider
    extends HasFallbackAbstractBlockStateProvider
{
    public List<SingleTargetBlockState> RuleTargets;

    public RuleTestBasedBlockStateProvider(List<SingleTargetBlockState> targets , CompilableBlockState fallback)
            throws ArgumentNullException
    {
        super(fallback);
        RuleTargets = targets;
    }

    @Override
    public BlockState GetNormalBlockState(BlockStateProviderContext context)
    {
        for (var t : RuleTargets)
        {
            if (t.Target.test(context.GetStateAtPosition() , context.source())) {
                return t.State.BlockState;
            }
        }
        return null;
    }

    @Override
    public AbstractBlockStateProviderType<?> GetType() {
        return RuleTestBasedBlockStateProviderType.INSTANCE;
    }

    private void DisposeFields()
    {
        RuleTargets = null;
        FallbackState = null;
    }

    @Override
    protected boolean CompileImplementation()
    {
        if (DCOUtils.CompileAllOrFail(RuleTargets)) {
            if (super.CompileImplementation()) {
                return true;
            } else {
                DisposeFields();
                return false;
            }
        } else {
            DisposeFields();
            return false;
        }
    }
}

package com.github.mdcdi1315.mdex.structures;

import com.github.mdcdi1315.basemodslib.utils.Extensions;

import com.github.mdcdi1315.mdex.block.BlockUtils;
import com.github.mdcdi1315.mdex.dco_logic.DCOUtils;
import com.github.mdcdi1315.mdex.util.CompilableBlockState;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public final class RandomBlockStatesMatchRuleTest
    extends AbstractModdedRuleTest
{
    public final float probability;
    public List<CompilableBlockState> RandomStates;

    public RandomBlockStatesMatchRuleTest(List<CompilableBlockState> randomstates , float probability)
    {
        this.probability = probability;
        RandomStates = randomstates;
        Compile();
    }

    @Override
    protected boolean CompileRuleTestData()
    {
        if (DCOUtils.CompileAllOrFail(RandomStates)) {
            return true;
        } else {
            RandomStates = null;
            return false;
        }
    }

    @Override
    protected boolean Test(BlockState blockState, RandomSource randomSource)
    {
        if (RandomStates == null || RandomStates.isEmpty()) {
            return false;
        } else {
            return randomSource.nextFloat() < probability && BlockUtils.BlockStatesMatch(Extensions.SelectRandomFromListUnsafe(RandomStates, randomSource).BlockState , blockState);
        }
    }

    @Override
    protected AbstractModdedRuleTestType<RandomBlockStatesMatchRuleTest> GetType() {
        return RandomBlockStatesMatchRuleTestType.INSTANCE;
    }
}

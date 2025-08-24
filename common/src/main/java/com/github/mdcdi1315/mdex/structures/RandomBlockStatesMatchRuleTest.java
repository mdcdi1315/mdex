package com.github.mdcdi1315.mdex.structures;

import com.github.mdcdi1315.mdex.block.BlockUtils;
import com.github.mdcdi1315.mdex.features.FeaturePlacementUtils;
import com.github.mdcdi1315.mdex.util.CompilableTargetBlockState;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTestType;

import java.util.List;

public final class RandomBlockStatesMatchRuleTest
    extends AbstractModdedRuleTest
{
    public final float probability;
    public List<CompilableTargetBlockState> RandomStates;

    public RandomBlockStatesMatchRuleTest(List<CompilableTargetBlockState> randomstates , float probability)
    {
        this.probability = probability;
        RandomStates = randomstates;
        Compile();
    }

    @Override
    protected boolean CompileRuleTestData()
    {
        for (var i : RandomStates)
        {
            i.Compile();
            if (!i.IsCompiled()) {
                RandomStates = null;
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean test(BlockState blockState, RandomSource randomSource)
    {
        if (RandomStates.isEmpty()) {
            return false;
        }
        return randomSource.nextFloat() < probability && BlockUtils.BlockStatesMatch(FeaturePlacementUtils.SampleFromRandomSource(RandomStates , randomSource).BlockState , blockState);
    }

    @Override
    protected RuleTestType<?> getType() {
        return RandomBlockStatesMatchRuleTestType.INSTANCE;
    }
}

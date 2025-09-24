package com.github.mdcdi1315.mdex.structures;

import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.DisallowNull;
import com.github.mdcdi1315.mdex.util.Compilable;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTestType;

public abstract class AbstractModdedRuleTest
    extends RuleTest
    implements Compilable
{
    private boolean compiled;

    public AbstractModdedRuleTest() {
        compiled = false;
    }

    protected abstract boolean CompileRuleTestData();

    protected abstract AbstractModdedRuleTestType<?> GetType();

    public final void Compile()
    {
        compiled = CompileRuleTestData();
    }

    protected abstract boolean Test(@DisallowNull BlockState state , @DisallowNull RandomSource random);

    @Override
    public final boolean test(BlockState blockState, RandomSource randomSource) {
        return compiled && Test(blockState, randomSource);
    }

    public final boolean IsCompiled()
    {
        return compiled;
    }

    public final RuleTestType<?> getType() { return GetType(); }
}
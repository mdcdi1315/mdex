package com.github.mdcdi1315.mdex.structures;

import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.DisallowNull;

import com.github.mdcdi1315.mdex.MDEXBalmLayer;
import com.github.mdcdi1315.mdex.util.Compilable;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTestType;

public abstract class AbstractModdedRuleTest
    extends RuleTest
    implements Compilable
{
    private byte state;
    private static final byte
            STATE_COMPILED = 1 << 0 ,
            STATE_IS_INVALID = 1 << 1 ,
            STATE_MASK = STATE_COMPILED | STATE_IS_INVALID;

    public AbstractModdedRuleTest() {
        state = 0;
    }

    protected abstract boolean CompileRuleTestData();

    protected abstract AbstractModdedRuleTestType<?> GetType();

    public final void Compile()
    {
        if ((state & STATE_MASK) != 0) { return; }
        try {
            if (CompileRuleTestData()) {
                state |= STATE_COMPILED;
            }
        } catch (Exception e) {
            MDEXBalmLayer.LOGGER.error("A rule test could not be compiled." , e);
            state |= STATE_IS_INVALID;
        }
    }

    protected abstract boolean Test(@DisallowNull BlockState state , @DisallowNull RandomSource random);

    @Override
    public final boolean test(BlockState blockState, RandomSource randomSource) {
        Compile();
        return (state & STATE_COMPILED) != 0 && Test(blockState, randomSource);
    }

    public final boolean IsCompiled()
    {
        return (state & STATE_COMPILED) != 0;
    }

    public final RuleTestType<?> getType() { return GetType(); }
}
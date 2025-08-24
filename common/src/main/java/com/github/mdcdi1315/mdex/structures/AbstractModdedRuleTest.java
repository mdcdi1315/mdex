package com.github.mdcdi1315.mdex.structures;

import com.github.mdcdi1315.mdex.util.Compilable;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;

public abstract class AbstractModdedRuleTest
    extends RuleTest
    implements Compilable
{
    private boolean compiled;

    public AbstractModdedRuleTest() {
        compiled = false;
    }

    protected abstract boolean CompileRuleTestData();

    public final void Compile()
    {
        if (CompileRuleTestData())
        {
            compiled = true;
        }
    }

    public final boolean IsCompiled()
    {
        return compiled;
    }
}
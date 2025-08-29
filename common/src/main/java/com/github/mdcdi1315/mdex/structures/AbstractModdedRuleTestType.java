package com.github.mdcdi1315.mdex.structures;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTestType;

public abstract class AbstractModdedRuleTestType<T extends AbstractModdedRuleTest>
    implements RuleTestType<T>
{
    private final MapCodec<T> cdc;

    protected AbstractModdedRuleTestType() {
        cdc = GetCodecInstance();
    }

    protected abstract MapCodec<T> GetCodecInstance();

    public final MapCodec<T> codec() {
        return cdc;
    }
}

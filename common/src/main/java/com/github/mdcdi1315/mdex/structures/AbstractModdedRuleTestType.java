package com.github.mdcdi1315.mdex.structures;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTestType;

public abstract class AbstractModdedRuleTestType<T extends AbstractModdedRuleTest>
    implements RuleTestType<T>
{
    private final Codec<T> cdc;

    protected AbstractModdedRuleTestType() {
        cdc = GetCodecInstance();
    }

    protected abstract Codec<T> GetCodecInstance();

    public final MapCodec<T> codec() {
        return MapCodec.assumeMapUnsafe(cdc);
    }
}

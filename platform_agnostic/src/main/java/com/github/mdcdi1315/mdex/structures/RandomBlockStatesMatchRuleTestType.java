package com.github.mdcdi1315.mdex.structures;

import com.mojang.serialization.Codec;

import com.github.mdcdi1315.basemodslib.codecs.CodecUtils;

import com.github.mdcdi1315.mdex.util.CompilableBlockState;

public final class RandomBlockStatesMatchRuleTestType
    extends AbstractModdedRuleTestType<RandomBlockStatesMatchRuleTest>
{
    public static final RandomBlockStatesMatchRuleTestType INSTANCE = new RandomBlockStatesMatchRuleTestType();

    @Override
    protected Codec<RandomBlockStatesMatchRuleTest> GetCodecInstance()
    {
        return CodecUtils.CreateCodecDirect(
                CompilableBlockState.GetCodec().listOf().fieldOf("random_states").forGetter((RandomBlockStatesMatchRuleTest r) -> r.RandomStates),
                CodecUtils.FLOAT_PROBABILITY.fieldOf("probability").forGetter((RandomBlockStatesMatchRuleTest r) -> r.probability),
                RandomBlockStatesMatchRuleTest::new
        );
    }
}

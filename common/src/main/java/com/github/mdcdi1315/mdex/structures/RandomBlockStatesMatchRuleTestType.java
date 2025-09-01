package com.github.mdcdi1315.mdex.structures;

import com.mojang.serialization.MapCodec;
import com.github.mdcdi1315.mdex.codecs.CodecUtils;
import com.github.mdcdi1315.mdex.util.CompilableTargetBlockState;

public final class RandomBlockStatesMatchRuleTestType
    extends AbstractModdedRuleTestType<RandomBlockStatesMatchRuleTest>
{
    public static RandomBlockStatesMatchRuleTestType INSTANCE = new RandomBlockStatesMatchRuleTestType();

    @Override
    protected MapCodec<RandomBlockStatesMatchRuleTest> GetCodecInstance()
    {
        return CodecUtils.CreateMapCodecDirect(
                CompilableTargetBlockState.GetCodec().listOf().fieldOf("random_states").forGetter((RandomBlockStatesMatchRuleTest r) -> r.RandomStates),
                CodecUtils.FLOAT_PROBABILITY.fieldOf("probability").forGetter((RandomBlockStatesMatchRuleTest r) -> r.probability),
                RandomBlockStatesMatchRuleTest::new
        );
    }
}

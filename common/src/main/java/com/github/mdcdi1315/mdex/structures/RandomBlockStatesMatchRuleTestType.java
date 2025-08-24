package com.github.mdcdi1315.mdex.structures;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.ListCodec;
import com.github.mdcdi1315.mdex.codecs.CodecUtils;
import com.github.mdcdi1315.mdex.util.CompilableTargetBlockState;

public final class RandomBlockStatesMatchRuleTestType
    extends AbstractModdedRuleTestType<RandomBlockStatesMatchRuleTest>
{
    public static RandomBlockStatesMatchRuleTestType INSTANCE = new RandomBlockStatesMatchRuleTestType();

    @Override
    protected Codec<RandomBlockStatesMatchRuleTest> GetCodecInstance()
    {
        return CodecUtils.CreateCodecDirect(
                new ListCodec<>(CompilableTargetBlockState.GetCodec()).fieldOf("random_states").forGetter((RandomBlockStatesMatchRuleTest r) -> r.RandomStates),
                Codec.floatRange(0f , 1f).fieldOf("probability").forGetter((RandomBlockStatesMatchRuleTest r) -> r.probability),
                RandomBlockStatesMatchRuleTest::new
        );
    }
}

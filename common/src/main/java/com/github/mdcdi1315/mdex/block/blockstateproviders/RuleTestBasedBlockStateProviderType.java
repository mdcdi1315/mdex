package com.github.mdcdi1315.mdex.block.blockstateproviders;

import com.github.mdcdi1315.mdex.codecs.CodecUtils;
import com.github.mdcdi1315.mdex.util.CompilableBlockState;
import com.github.mdcdi1315.mdex.util.SingleTargetBlockState;

import com.mojang.serialization.Codec;

public final class RuleTestBasedBlockStateProviderType
    extends AbstractBlockStateProviderType<RuleTestBasedBlockStateProvider>
{
    public static final RuleTestBasedBlockStateProviderType INSTANCE = new RuleTestBasedBlockStateProviderType();

    @Override
    protected Codec<RuleTestBasedBlockStateProvider> GetCodecInstance() {
        return CodecUtils.CreateCodecDirect(
                SingleTargetBlockState.GetCodec().listOf().fieldOf("targets").forGetter((s) -> s.RuleTargets),
                CompilableBlockState.GetCodec().fieldOf("fallback_state").forGetter((s) -> s.FallbackState),
                RuleTestBasedBlockStateProvider::new
        );
    }
}

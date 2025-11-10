package com.github.mdcdi1315.mdex.block.blockstateproviders;

import com.github.mdcdi1315.basemodslib.codecs.CodecUtils;
import com.github.mdcdi1315.mdex.util.CompilableBlockState;
import com.github.mdcdi1315.mdex.util.IntegerInclusiveRange;

import com.mojang.serialization.MapCodec;

import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

public final class DualNoiseProviderType
    extends AbstractBlockStateProviderType<DualNoiseProvider>
{
    public static final DualNoiseProviderType INSTANCE = new DualNoiseProviderType();

    @Override
    protected MapCodec<DualNoiseProvider> GetCodecInstance() {
        return CodecUtils.CreateMapCodecDirect(
                DualNoiseProvider.GetBaseCodec(),
                IntegerInclusiveRange.EnforceRangeIntoRange(1 , 64).fieldOf("variety").forGetter((p) -> p.variety),
                NormalNoise.NoiseParameters.CODEC.fieldOf("slow_noise").forGetter((p) -> p.slowNoiseParameters),
                ExtraCodecs.POSITIVE_FLOAT.fieldOf("slow_scale").forGetter((p) -> p.slowScale),
                CompilableBlockState.GetCodec().listOf().fieldOf("states").forGetter((p) -> p.States),
                DualNoiseProvider::new
        );
    }
}

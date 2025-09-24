package com.github.mdcdi1315.mdex.block.blockstateproviders;

import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.MaybeNull;
import com.github.mdcdi1315.mdex.codecs.CodecUtils;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;

import net.minecraft.core.Holder;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.minecraft.world.level.levelgen.XoroshiroRandomSource;

public record NoiseStateProviderData(long seed , Holder<NormalNoise.NoiseParameters> parameters , float scale)
{
    private static MapCodec<NoiseStateProviderData> codec;

    public static MapCodec<NoiseStateProviderData> GetCodec()
    {
        if (codec == null)
        {
            codec = CodecUtils.CreateMapCodecDirect(
                    Codec.LONG.fieldOf("seed").forGetter(NoiseStateProviderData::seed),
                    NormalNoise.NoiseParameters.CODEC.fieldOf("noise").forGetter(NoiseStateProviderData::parameters),
                    ExtraCodecs.POSITIVE_FLOAT.fieldOf("scale").forGetter(NoiseStateProviderData::scale),
                    NoiseStateProviderData::new
            );
        }
        return codec;
    }

    public NormalNoise CreateNoise(@MaybeNull RandomSource source) {
        return NormalNoise.create(source == null ? new XoroshiroRandomSource(seed) : source , parameters.value());
    }
}

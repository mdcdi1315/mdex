package com.github.mdcdi1315.mdex.features.orevein;

import com.github.mdcdi1315.mdex.codecs.CodecUtils;
import com.github.mdcdi1315.mdex.util.Compilable;
import com.github.mdcdi1315.mdex.util.SingleBlockState;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.ListCodec;

import java.util.List;

public final class RareBlockPlacementSettings
    implements Compilable
{
    public List<SingleBlockState> RareTargetStates;
    public float NoiseDensityThreshold;
    private boolean compiled;

    public RareBlockPlacementSettings(List<SingleBlockState> rare , float noiseDensityThreshold)
    {
        compiled = false;
        RareTargetStates = rare;
        NoiseDensityThreshold = noiseDensityThreshold;
    }

    public static Codec<RareBlockPlacementSettings> GetCodec()
    {
        return CodecUtils.CreateCodecDirect(
                new ListCodec<>(SingleBlockState.GetCodec()).fieldOf("targets").forGetter((RareBlockPlacementSettings r) -> r.RareTargetStates),
                Codec.floatRange(0f , 6f).fieldOf("noise_density_threshold").forGetter((RareBlockPlacementSettings r) -> r.NoiseDensityThreshold),
                RareBlockPlacementSettings::new
        );
    }

    @Override
    public void Compile() {
        for (var i : RareTargetStates)
        {
            i.Compile();
            if (!i.IsCompiled()) { return; }
        }
        compiled = true;
    }

    @Override
    public boolean IsCompiled() {
        return compiled;
    }
}

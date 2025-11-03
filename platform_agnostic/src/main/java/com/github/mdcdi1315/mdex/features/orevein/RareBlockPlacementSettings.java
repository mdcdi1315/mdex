package com.github.mdcdi1315.mdex.features.orevein;

import com.github.mdcdi1315.basemodslib.codecs.CodecUtils;

import com.github.mdcdi1315.mdex.dco_logic.Compilable;
import com.github.mdcdi1315.mdex.util.SingleTargetBlockState;

import com.mojang.serialization.Codec;

import java.util.List;

public final class RareBlockPlacementSettings
    implements Compilable
{
    public List<SingleTargetBlockState> RareTargetStates;
    public float NoiseDensityThreshold;
    private boolean compiled;

    public RareBlockPlacementSettings(List<SingleTargetBlockState> rare , float noiseDensityThreshold)
    {
        compiled = false;
        RareTargetStates = rare;
        NoiseDensityThreshold = noiseDensityThreshold;
    }

    public static Codec<RareBlockPlacementSettings> GetCodec()
    {
        return CodecUtils.CreateCodecDirect(
                SingleTargetBlockState.GetListCodec().fieldOf("targets").forGetter((RareBlockPlacementSettings r) -> r.RareTargetStates),
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

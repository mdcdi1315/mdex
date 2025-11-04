package com.github.mdcdi1315.mdex.features.config;

import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.NotNull;

import com.github.mdcdi1315.basemodslib.codecs.CodecUtils;
import com.github.mdcdi1315.basemodslib.codecs.StrictListCodec;

import com.github.mdcdi1315.mdex.features.createlayeredore.LayerPattern;

import com.mojang.serialization.MapCodec;

import net.minecraft.util.RandomSource;

import java.util.List;
import java.util.ArrayList;

public final class CreateLayeredOreFeatureConfigurationDetails
    implements IModdedFeatureConfigurationDetails
{
    public final byte Size;
    public List<LayerPattern> patterns;
    public final float DiscardChanceOnAirExposure;

    public static MapCodec<CreateLayeredOreFeatureConfigurationDetails> GetCodec()
    {
        return CodecUtils.CreateMapCodecDirect(
                new StrictListCodec<>(LayerPattern.GetCodec()).fieldOf("layer_patterns").forGetter((lo) -> lo.patterns),
                CodecUtils.ByteRange(0 , 48).fieldOf("size").forGetter((lo) -> lo.Size),
                CodecUtils.FLOAT_PROBABILITY.fieldOf("discard_chance_on_air_exposure").forGetter((lo) -> lo.DiscardChanceOnAirExposure),
                CreateLayeredOreFeatureConfigurationDetails::new
        );
    }

    public CreateLayeredOreFeatureConfigurationDetails(List<LayerPattern> patterns, byte size, float discard_chance)
    {
        Size = size;
        this.patterns = patterns;
        DiscardChanceOnAirExposure = discard_chance;
        Compile();
    }

    @Override
    public void Compile()
    {
        ArrayList<LayerPattern> ps = new ArrayList<>(patterns);
        LayerPattern lp;
        for (int I = 0; I < ps.size(); I++)
        {
            lp = ps.get(I);
            lp.Compile();
            if (!lp.IsCompiled()) {
                /*
                Compilation should fail in layer patterns for primarily two reasons:

                -> ALL the layers contained by the patterns have failed to provide existing block states
                -> A fatal exception has been occurred.

                If this is the case, the pattern will be removed at run-time.
                If we have left with 0 layer patterns, we have failed, and compilation must fail.
                 */
                ps.remove(I--);
            }
        }
        if (ps.isEmpty()) {
            // All layer patterns failed, at least one must exist, thus compilation has failed.
            throw new FeatureCompilationFailureException();
        } else {
            // On success, trim down the size of the patterns and set that as a new value to the patterns field.
            ps.trimToSize();
            patterns = ps;
        }
    }

    @NotNull
    public LayerPattern GetRandomPattern(RandomSource random) {
        return patterns.get(random.nextInt(patterns.size()));
    }
}

package com.github.mdcdi1315.mdex.features.config;

import com.github.mdcdi1315.mdex.codecs.CodecUtils;
import com.github.mdcdi1315.mdex.util.InvalidFeatureConfigurationException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.ListCodec;
import net.minecraft.core.HolderSet;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import com.github.mdcdi1315.mdex.features.floatingisland.CompilableIslandLayer;

import java.util.List;

public final class SimpleFloatingIslandConfiguration
    extends ModdedFeatureConfiguration
{
    public HolderSet<PlacedFeature> FeaturesToGenerateOnTop;
    public List<CompilableIslandLayer> Layers;
    public IntProvider MaxDistanceFromGround;

    public SimpleFloatingIslandConfiguration(List<String> modids, List<CompilableIslandLayer> layers , IntProvider distance, HolderSet<PlacedFeature> featuresOnTop)
    {
        super(modids);
        Layers = layers;
        FeaturesToGenerateOnTop = featuresOnTop;
        MaxDistanceFromGround = distance;
        Compile();
    }

    public static Codec<SimpleFloatingIslandConfiguration> GetCodec()
    {
        return CodecUtils.CreateCodecDirect(
                GetBaseCodec(),
                new ListCodec<>(CompilableIslandLayer.GetCodec()).fieldOf("layers").forGetter((p) -> p.Layers),
                IntProvider.codec(1 , 8).optionalFieldOf("max_distance_from_ground" , ConstantInt.of(4)).forGetter((p) -> p.MaxDistanceFromGround),
                PlacedFeature.LIST_CODEC.optionalFieldOf("additional_features_on_top" , HolderSet.direct()).forGetter((p) -> p.FeaturesToGenerateOnTop),
                SimpleFloatingIslandConfiguration::new
        );
    }

    @Override
    protected void compileConfigData()
    {
        int size = Layers.size();
        if (size > 1)
        {
            // Last layer must always have smaller size than it's previous one.
            for (int I = size - 1; I > 0; I--)
            {
                if ((Layers.get(I).Size >= Layers.get(I-1).Size))
                {
                    throw new InvalidFeatureConfigurationException("Size of the previous layer must be greater than the size of the next layer.");
                }
            }
            for (var lr : Layers)
            {
                lr.Compile();
            }
        } else if (size == 1) {
            Layers.get(0).Compile();
        }
        for (var g : FeaturesToGenerateOnTop)
        {
            if (g.value().feature().value().config() instanceof ModdedFeatureConfiguration mfc)
            {
                mfc.Compile();
                if (mfc.IsCompiled() == false)
                {
                    setConfigAsInvalid();
                    return;
                }
            }
        }
    }

    @Override
    protected void invalidateUntransformedFields() {

    }
}

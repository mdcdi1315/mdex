package com.github.mdcdi1315.mdex.features.config;

import com.github.mdcdi1315.basemodslib.codecs.CodecUtils;

import com.github.mdcdi1315.mdex.features.floatingisland.AdvancedCompilableIslandLayer;

import net.minecraft.core.HolderSet;
import com.mojang.serialization.Codec;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.List;

public final class AdvancedFloatingIslandConfigurationDetails
    implements IModdedFeatureConfigurationDetails
{
    public HolderSet<PlacedFeature> FeaturesToGenerateOnTop;
    public List<AdvancedCompilableIslandLayer> Layers;
    public IntProvider MaxDistanceFromGround;

    public static Codec<AdvancedFloatingIslandConfigurationDetails> GetCodec()
    {
        return CodecUtils.CreateCodecDirect(
                AdvancedCompilableIslandLayer.GetCodec().listOf().fieldOf("layers").forGetter((p) -> p.Layers),
                IntProvider.codec(1 , 8).optionalFieldOf("max_distance_from_ground" , ConstantInt.of(4)).forGetter((p) -> p.MaxDistanceFromGround),
                PlacedFeature.LIST_CODEC.optionalFieldOf("additional_features_on_top" , HolderSet.direct()).forGetter((p) -> p.FeaturesToGenerateOnTop),
                AdvancedFloatingIslandConfigurationDetails::new
        );
    }

    public AdvancedFloatingIslandConfigurationDetails(List<AdvancedCompilableIslandLayer> layers , IntProvider distance, HolderSet<PlacedFeature> featuresOnTop)
    {
        Layers = layers;
        FeaturesToGenerateOnTop = featuresOnTop;
        MaxDistanceFromGround = distance;
        Compile();
    }

    @Override
    public void Compile()
    {
        for (var l : Layers)
        {
            l.Compile();
            if (l.IsCompiled() == false) {
                throw new FeatureCompilationFailureException();
            }
        }
    }

}

package com.github.mdcdi1315.mdex.features;

import com.github.mdcdi1315.mdex.features.config.ModdedFeatureConfiguration;
import com.github.mdcdi1315.mdex.features.config.NoiseGenerationBasedOreFeatureConfigurationDetails;

import net.minecraft.core.BlockPos;
import com.mojang.serialization.Codec;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public final class NoiseGenerationBasedOreFeature
    extends ModdedFeature<ModdedFeatureConfiguration<NoiseGenerationBasedOreFeatureConfigurationDetails>>
{
    public NoiseGenerationBasedOreFeature(Codec<ModdedFeatureConfiguration<NoiseGenerationBasedOreFeatureConfigurationDetails>> codec) { super(codec); }

    @Override
    protected boolean PlaceModdedFeature(FeaturePlaceContext<ModdedFeatureConfiguration<NoiseGenerationBasedOreFeatureConfigurationDetails>> fpc)
    {
        RandomSource rs = fpc.random();
        WorldGenLevel wgl = fpc.level();
        NoiseGenerationBasedOreFeatureConfigurationDetails details = fpc.config().Details;
        NormalNoise n = NormalNoise.create(rs , details.NoiseParameters.value());

        // Create a rectangular area for the noise, let it be as dictated by the specified size.
        // Y scales are also controlled by the same scheme.
        int fs = details.Size.sample(rs);
        boolean generated = false;
        float discardchance = details.DiscardChanceOnAirExposure;

        for (BlockPos temp : FeaturePlacementUtils.GetRectangularArea(fpc.origin().offset(-(fs / 2), 0, -(fs / 2)) , new BlockPos(fs , fpc.config().Details.Y_Scale.sample(rs) , fs)))
        {
            if (n.getValue(temp.getX() , temp.getY() , temp.getZ()) > 0d)
            {
                BlockState current = wgl.getBlockState(temp);
                for (var t : details.TargetStates)
                {
                    if (ModdedOreFeature.CanPlaceOre(current , wgl::getBlockState , rs , discardchance , t , temp))
                    {
                        if (wgl.setBlock(temp, t.State.BlockState, 2)) {
                            generated = true;
                        }
                        break;
                    }
                }
            }
        }

        return generated;
    }
}

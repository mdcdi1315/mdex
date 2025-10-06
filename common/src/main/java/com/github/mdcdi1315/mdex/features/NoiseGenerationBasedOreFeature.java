package com.github.mdcdi1315.mdex.features;

import net.minecraft.core.BlockPos;
import com.mojang.serialization.Codec;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

import com.github.mdcdi1315.mdex.features.config.NoiseGenerationBasedOreFeatureConfiguration;

public final class NoiseGenerationBasedOreFeature
    extends ModdedFeature<NoiseGenerationBasedOreFeatureConfiguration>
{
    public NoiseGenerationBasedOreFeature(Codec<NoiseGenerationBasedOreFeatureConfiguration> codec) { super(codec); }

    @Override
    protected boolean placeModdedFeature(FeaturePlaceContext<NoiseGenerationBasedOreFeatureConfiguration> fpc)
    {
        RandomSource rs = fpc.random();
        WorldGenLevel wgl = fpc.level();
        NormalNoise n = NormalNoise.create(rs , fpc.config().NoiseParameters.value());

        // Create a rectangular area for the noise, let it be as dictated by the specified size.
        // Y scales are also controlled by the same scheme.
        int fs = fpc.config().Size.sample(rs);
        boolean generated = false;
        float discardchance = fpc.config().DiscardChanceOnAirExposure;

        for (BlockPos temp : FeaturePlacementUtils.GetRectangularArea(fpc.origin().offset(-fs, 0, -fs) , new BlockPos(fs , fpc.config().Y_Scale.sample(rs) , fs)))
        {
            if (n.getValue(temp.getX() , temp.getY() , temp.getZ()) > 0d)
            {
                BlockState current = wgl.getBlockState(temp);
                for (var t : fpc.config().TargetStates)
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

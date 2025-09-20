package com.github.mdcdi1315.mdex.features;

import com.github.mdcdi1315.mdex.util.SingleTargetBlockState;
import com.github.mdcdi1315.mdex.features.config.ModdedOreVeinFeatureConfiguration;

import net.minecraft.core.BlockPos;
import com.mojang.serialization.Codec;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

import java.util.List;

public final class ModdedOreVeinFeature
    extends ModdedFeature<ModdedOreVeinFeatureConfiguration>
{
    public ModdedOreVeinFeature(Codec<ModdedOreVeinFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    protected boolean placeModdedFeature(FeaturePlaceContext<ModdedOreVeinFeatureConfiguration> fpc)
    {
        RandomSource rs = fpc.random();
        WorldGenLevel wgl = fpc.level();
        NormalNoise n = NormalNoise.create(rs , fpc.config().Parameters.value());

        // Create a rectangular area for the noise, let it be as dictated by the specified size.
        // Y scales are also controlled by the same scheme.
        int fs = fpc.config().Size.sample(rs);
        short generated = 0;
        float discardchance = fpc.config().DiscardChanceOnAirExposure ,
                rareplacement = fpc.config().RarePlacementSettings.NoiseDensityThreshold;
        double density;

        for (BlockPos temp : FeaturePlacementUtils.GetRectangularArea(fpc.origin() , new BlockPos(fs , fpc.config().Y_Scale.sample(rs) , fs)))
        {
            BlockState current = wgl.getBlockState(temp);
            density = n.getValue(temp.getX() , temp.getY() , temp.getZ());
            for (var t : (density >= rareplacement) ? fpc.config().RarePlacementSettings.RareTargetStates : (density > 0d) ? fpc.config().TargetStates : List.<SingleTargetBlockState>of())
            {
                if (ModdedOreFeature.CanPlaceOre(current , wgl::getBlockState , rs , discardchance , t , temp))
                {
                    if (wgl.setBlock(temp, t.State.BlockState, 2)) {
                        generated++;
                    }
                    break;
                }
            }
            // A simple check to not generate more than this limit - the area is full of ores!!
            if (generated == 4096) { break; }
        }

        return generated > 10;
    }
}

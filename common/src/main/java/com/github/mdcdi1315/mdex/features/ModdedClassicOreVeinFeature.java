package com.github.mdcdi1315.mdex.features;

import com.github.mdcdi1315.mdex.util.SingleTargetBlockState;
import com.github.mdcdi1315.mdex.features.config.ModdedClassicOreVeinFeatureConfiguration;

import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

import java.util.List;

public final class ModdedClassicOreVeinFeature
    extends ModdedFeature<ModdedClassicOreVeinFeatureConfiguration>
{
    public ModdedClassicOreVeinFeature(Codec<ModdedClassicOreVeinFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    protected boolean placeModdedFeature(FeaturePlaceContext<ModdedClassicOreVeinFeatureConfiguration> fpc) {
        RandomSource rs = fpc.random();
        WorldGenLevel wgl = fpc.level();
        NormalNoise n = NormalNoise.create(rs , fpc.config().Parameters.value());

        // Create a rectangular area for the noise, let it be as dictated by the specified size.
        // Y scales are also controlled by the same scheme.
        int fs = fpc.config().Size.sample(rs);
        boolean atleastone = false;
        float discardchance = fpc.config().DiscardChanceOnAirExposure ,
                rareplacement = fpc.config().RarePlacementSettings.NoiseDensityThreshold,
                stoneplacement = fpc.config().StonePlacementSettings.NoiseDensityThreshold,
                stoneplacementp = fpc.config().StonePlacementSettings.PlacementProbability;
        BlockState stonestate = fpc.config().StonePlacementSettings.StoneState.BlockState, current = null;
        double density;

        List<SingleTargetBlockState> states = null;

        for (BlockPos temp : FeaturePlacementUtils.GetRectangularArea(fpc.origin().offset(-fs , 0 , -fs), new BlockPos(fs , fpc.config().Y_Scale.sample(rs) , fs)))
        {
            density = n.getValue(temp.getX() , temp.getY() , temp.getZ());
            if (density > rareplacement) {
                current = wgl.getBlockState(temp);
                states = fpc.config().RarePlacementSettings.RareTargetStates;
            } else if (density > stoneplacement && rs.nextFloat() < stoneplacementp) {
                wgl.setBlock(temp , stonestate , 2);
            } else if (density > 0d) {
                current = wgl.getBlockState(temp);
                states = fpc.config().TargetStates;
            }
            if (states != null)
            {
                for (var t : states)
                {
                    if (ModdedOreFeature.CanPlaceOre(current , wgl::getBlockState , rs , discardchance , t , temp))
                    {
                        if (wgl.setBlock(temp, t.State.BlockState, 2)) { atleastone = true; }
                        break;
                    }
                }
                states = null;
                current = null; // This field will also not be null upon entering this if branch.
            }
        }

        return atleastone;
    }
}

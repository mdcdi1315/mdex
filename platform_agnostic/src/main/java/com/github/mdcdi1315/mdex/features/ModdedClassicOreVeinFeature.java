package com.github.mdcdi1315.mdex.features;

import com.github.mdcdi1315.mdex.block.BlockUtils;
import com.github.mdcdi1315.mdex.util.SingleTargetBlockState;
import com.github.mdcdi1315.mdex.features.config.ModdedFeatureConfiguration;
import com.github.mdcdi1315.mdex.features.config.ModdedClassicOreVeinFeatureConfigurationDetails;

import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

import java.util.List;

public final class ModdedClassicOreVeinFeature
    extends ModdedFeature<ModdedFeatureConfiguration<ModdedClassicOreVeinFeatureConfigurationDetails>>
{
    public ModdedClassicOreVeinFeature(Codec<ModdedFeatureConfiguration<ModdedClassicOreVeinFeatureConfigurationDetails>> codec) {
        super(codec);
    }

    @Override
    protected boolean PlaceModdedFeature(FeaturePlaceContext<ModdedFeatureConfiguration<ModdedClassicOreVeinFeatureConfigurationDetails>> fpc) {
        RandomSource rs = fpc.random();
        WorldGenLevel wgl = fpc.level();
        ModdedClassicOreVeinFeatureConfigurationDetails details = fpc.config().Details;
        NormalNoise n = NormalNoise.create(rs , details.Parameters.value());

        // Create a rectangular area for the noise, let it be as dictated by the specified size.
        // Y scales are also controlled by the same scheme.
        int fs = details.Size.sample(rs);
        boolean atleastone = false;
        float discardchance = details.DiscardChanceOnAirExposure ,
                rareplacement = details.RarePlacementSettings.NoiseDensityThreshold,
                stoneplacement = details.StonePlacementSettings.NoiseDensityThreshold,
                stoneplacementp = details.StonePlacementSettings.PlacementProbability;
        BlockState stonestate = details.StonePlacementSettings.StoneState.BlockState, current;
        double density;

        List<SingleTargetBlockState> states = null;

        for (BlockPos temp : FeaturePlacementUtils.GetRectangularArea(fpc.origin().offset(-(fs / 2) , 0 , -(fs / 2)), new BlockPos(fs , details.Y_Scale.sample(rs) , fs)))
        {
            density = n.getValue(temp.getX() , temp.getY() , temp.getZ());
            current = wgl.getBlockState(temp);
            if (density > rareplacement) {
                states = details.RarePlacementSettings.RareTargetStates;
            } else if (density > stoneplacement && BlockUtils.ReferentIsSolidBlockUnsafe(current) && rs.nextFloat() < stoneplacementp) {
                wgl.setBlock(temp , stonestate , 2);
            } else if (density > 0d) {
                states = details.TargetStates;
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
            }
        }

        return atleastone;
    }
}

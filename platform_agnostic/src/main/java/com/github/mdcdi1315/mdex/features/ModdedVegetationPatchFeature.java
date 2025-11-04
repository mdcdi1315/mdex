package com.github.mdcdi1315.mdex.features;

import com.github.mdcdi1315.mdex.block.BlockUtils;
import com.github.mdcdi1315.DotNetLayer.System.Predicate;
import com.github.mdcdi1315.mdex.features.config.ModdedFeatureConfiguration;
import com.github.mdcdi1315.mdex.features.config.ModdedVegetationPatchConfigurationDetails;

import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

import java.util.Set;
import java.util.HashSet;

public class ModdedVegetationPatchFeature
    extends ModdedFeature<ModdedFeatureConfiguration<ModdedVegetationPatchConfigurationDetails>>
{
    public ModdedVegetationPatchFeature(Codec<ModdedFeatureConfiguration<ModdedVegetationPatchConfigurationDetails>> codec) {
        super(codec);
    }

    @Override
    protected boolean PlaceModdedFeature(FeaturePlaceContext<ModdedFeatureConfiguration<ModdedVegetationPatchConfigurationDetails>> context)
    {
        ModdedVegetationPatchConfigurationDetails vegetationpatchconfiguration = context.config().Details;
        RandomSource randomsource = context.random();
        int i = vegetationpatchconfiguration.xzRadius.sample(randomsource) + 1;
        int j = vegetationpatchconfiguration.xzRadius.sample(randomsource) + 1;
        Set<BlockPos> set = this.PlaceGroundPatch(context, context.origin(), (state) -> state.is(vegetationpatchconfiguration.replaceable), i, j);
        this.DistributeVegetation(context, set, i, j);
        return !set.isEmpty();
    }

    protected Set<BlockPos> PlaceGroundPatch(FeaturePlaceContext<ModdedFeatureConfiguration<ModdedVegetationPatchConfigurationDetails>> context, BlockPos pos, Predicate<BlockState> state, int xRadius, int zRadius)
    {
        BlockPos.MutableBlockPos mutablepos = pos.mutable();
        BlockPos.MutableBlockPos mutablepos1 = mutablepos.mutable();
        var config = context.config().Details;
        var random = context.random();
        var level = context.level();
        Direction direction = config.surface.getDirection();
        Direction opposite = direction.getOpposite();
        Set<BlockPos> set = new HashSet<>();

        for (int i = -xRadius; i <= xRadius; ++i)
        {
            boolean flag = i == -xRadius || i == xRadius;

            for (int j = -zRadius; j <= zRadius; ++j)
            {
                boolean flag1 = j == -zRadius || j == zRadius;
                if (!(flag && flag1) && (!(flag || flag1) || config.extraEdgeColumnChance != 0.0F && !(random.nextFloat() > config.extraEdgeColumnChance))) {
                    mutablepos.setWithOffset(pos, i, 0, j);

                    // First check for vertical range, then for whether the desired state exists.
                    for (int k = 0; k < config.verticalRange && level.isStateAtPosition(mutablepos, BlockBehaviour.BlockStateBase::isAir); ++k) {
                        mutablepos.move(direction);
                    }

                    for (int i1 = 0; i1 < config.verticalRange && level.isStateAtPosition(mutablepos, BlockUtils::ReferentIsSolidBlock); ++i1) {
                        mutablepos.move(opposite);
                    }

                    mutablepos1.setWithOffset(mutablepos, direction);
                    if (level.isEmptyBlock(mutablepos) && level.getBlockState(mutablepos1).isFaceSturdy(level, mutablepos1, opposite)) {
                        BlockPos blockpos = mutablepos1.immutable();
                        if (this.PlaceGround(context, state, mutablepos1, config.depth.sample(random) + (config.extraBottomBlockChance > 0.0F && random.nextFloat() < config.extraBottomBlockChance ? 1 : 0))) {
                            set.add(blockpos);
                        }
                    }
                }
            }
        }

        return set;
    }

    protected void DistributeVegetation(FeaturePlaceContext<ModdedFeatureConfiguration<ModdedVegetationPatchConfigurationDetails>> context, Set<BlockPos> possiblePositions, int xRadius, int zRadius) {
        var random = context.random();
        float chance = context.config().Details.vegetationChance;
        if (chance > 0.0f) {
            for (BlockPos blockpos : possiblePositions) {
                if (random.nextFloat() < chance) {
                    this.PlaceVegetation(context, blockpos);
                }
            }
        }
    }

    protected boolean PlaceVegetation(FeaturePlaceContext<ModdedFeatureConfiguration<ModdedVegetationPatchConfigurationDetails>> context, BlockPos pos) {
        var config = context.config().Details;
        return (config.vegetationFeature.value()).place(context.level(), context.chunkGenerator(), context.random(), pos.relative(config.surface.getDirection().getOpposite()));
    }

    protected boolean PlaceGround(FeaturePlaceContext<ModdedFeatureConfiguration<ModdedVegetationPatchConfigurationDetails>> context, Predicate<BlockState> replaceableblocks, BlockPos.MutableBlockPos mutablePos, int maxDistance)
    {
        var level = context.level();
        var ground_state = context.config().Details.groundState;
        var surfacedirection = context.config().Details.surface.getDirection();

        for (int dist = 0; dist < maxDistance; ++dist)
        {
            BlockState blockstate = ground_state.GetBlockState(level, context.random(), mutablePos);
            BlockState blockstate1 = level.getBlockState(mutablePos);
            if (!blockstate.is(blockstate1.getBlock())) {
                if (!replaceableblocks.predicate(blockstate1)) {
                    return dist != 0;
                }

                level.setBlock(mutablePos, blockstate, 2);
                mutablePos.move(surfacedirection);
            }
        }

        return true;
    }
}
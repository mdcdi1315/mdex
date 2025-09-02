package com.github.mdcdi1315.mdex.features;

import com.github.mdcdi1315.mdex.block.BlockUtils;
import com.github.mdcdi1315.DotNetLayer.System.Predicate;
import com.github.mdcdi1315.mdex.features.config.ModdedVegetationPatchConfiguration;

import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

import java.util.Set;
import java.util.HashSet;

public class ModdedVegetationPatchFeature
    extends ModdedFeature<ModdedVegetationPatchConfiguration>
{
    public ModdedVegetationPatchFeature(Codec<ModdedVegetationPatchConfiguration> codec) {
        super(codec);
    }

    public boolean placeModdedFeature(FeaturePlaceContext<ModdedVegetationPatchConfiguration> context) {
        WorldGenLevel worldgenlevel = context.level();
        ModdedVegetationPatchConfiguration vegetationpatchconfiguration = context.config();
        RandomSource randomsource = context.random();
        BlockPos blockpos = context.origin();
        Predicate<BlockState> predicate = (p_204782_) -> p_204782_.is(vegetationpatchconfiguration.replaceable);
        int i = vegetationpatchconfiguration.xzRadius.sample(randomsource) + 1;
        int j = vegetationpatchconfiguration.xzRadius.sample(randomsource) + 1;
        Set<BlockPos> set = this.placeGroundPatch(worldgenlevel, vegetationpatchconfiguration, randomsource, blockpos, predicate, i, j);
        this.distributeVegetation(context, worldgenlevel, randomsource, set, i, j);
        return !set.isEmpty();
    }

    protected Set<BlockPos> placeGroundPatch(WorldGenLevel level, ModdedVegetationPatchConfiguration config, RandomSource random, BlockPos pos, Predicate<BlockState> state, int xRadius, int zRadius) {
        BlockPos.MutableBlockPos mutablepos = pos.mutable();
        BlockPos.MutableBlockPos mutablepos1 = mutablepos.mutable();
        Direction direction = config.surface.getDirection();
        Direction direction1 = direction.getOpposite();
        Set<BlockPos> set = new HashSet<>();

        for (int i = -xRadius; i <= xRadius; ++i)
        {
            boolean flag = i == -xRadius || i == xRadius;

            for (int j = -zRadius; j <= zRadius; ++j)
            {
                boolean flag1 = j == -zRadius || j == zRadius;
                if (!(flag && flag1) && (!(flag || flag1) || config.extraEdgeColumnChance != 0.0F && !(random.nextFloat() > config.extraEdgeColumnChance))) {
                    mutablepos.setWithOffset(pos, i, 0, j);

                    for (int k = 0; level.isStateAtPosition(mutablepos, BlockBehaviour.BlockStateBase::isAir) && k < config.verticalRange; ++k) {
                        mutablepos.move(direction);
                    }

                    for (int i1 = 0; level.isStateAtPosition(mutablepos, BlockUtils::ReferentIsSolidBlock) && i1 < config.verticalRange; ++i1) {
                        mutablepos.move(direction1);
                    }

                    mutablepos1.setWithOffset(mutablepos, config.surface.getDirection());
                    BlockState blockstate = level.getBlockState(mutablepos1);
                    if (level.isEmptyBlock(mutablepos) && blockstate.isFaceSturdy(level, mutablepos1, config.surface.getDirection().getOpposite())) {
                        int l = config.depth.sample(random) + (config.extraBottomBlockChance > 0.0F && random.nextFloat() < config.extraBottomBlockChance ? 1 : 0);
                        BlockPos blockpos = mutablepos1.immutable();
                        if (this.placeGround(level, config, state, random, mutablepos1, l)) {
                            set.add(blockpos);
                        }
                    }
                }
            }
        }

        return set;
    }

    protected void distributeVegetation(FeaturePlaceContext<ModdedVegetationPatchConfiguration> context, WorldGenLevel level, RandomSource random, Set<BlockPos> possiblePositions, int xRadius, int zRadius) {
        var cfg = context.config();
        for(BlockPos blockpos : possiblePositions) {
            if (cfg.vegetationChance > 0.0F && random.nextFloat() < cfg.vegetationChance) {
                this.placeVegetation(level, cfg, context.chunkGenerator(), random, blockpos);
            }
        }
    }

    protected boolean placeVegetation(WorldGenLevel level, ModdedVegetationPatchConfiguration config, ChunkGenerator chunkGenerator, RandomSource random, BlockPos pos) {
        return (config.vegetationFeature.value()).place(level, chunkGenerator, random, pos.relative(config.surface.getDirection().getOpposite()));
    }

    protected boolean placeGround(WorldGenLevel level, ModdedVegetationPatchConfiguration config, Predicate<BlockState> replaceableblocks, RandomSource random, BlockPos.MutableBlockPos mutablePos, int maxDistance) {
        for(int i = 0; i < maxDistance; ++i) {
            BlockState blockstate = config.groundState.getState(random, mutablePos);
            BlockState blockstate1 = level.getBlockState(mutablePos);
            if (!blockstate.is(blockstate1.getBlock())) {
                if (!replaceableblocks.predicate(blockstate1)) {
                    return i != 0;
                }

                level.setBlock(mutablePos, blockstate, 2);
                mutablePos.move(config.surface.getDirection());
            }
        }

        return true;
    }
}
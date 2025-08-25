package com.github.mdcdi1315.mdex.features;

import com.github.mdcdi1315.mdex.features.config.ModdedGeodeConfiguration;
import com.github.mdcdi1315.mdex.features.geode.GeodeBlockSettings;
import com.github.mdcdi1315.mdex.features.geode.GeodeCrackSettings;
import com.github.mdcdi1315.mdex.features.geode.GeodeLayerSettings;
import com.github.mdcdi1315.mdex.util.CompilableTargetBlockState;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BuddingAmethystBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.minecraft.world.level.material.FluidState;

import java.util.List;

public final class ModdedGeodeFeature
    extends ModdedFeature<ModdedGeodeConfiguration>
{
    public ModdedGeodeFeature(Codec<ModdedGeodeConfiguration> codec) {
        super(codec);
    }

    @Override
    protected boolean placeModdedFeature(FeaturePlaceContext<ModdedGeodeConfiguration> fpc)
    {
        ModdedGeodeConfiguration geodeconfiguration = fpc.config();
        RandomSource randomsource = fpc.random();
        BlockPos blockpos = fpc.origin();
        WorldGenLevel worldgenlevel = fpc.level();
        int i = geodeconfiguration.minGenOffset;
        int j = geodeconfiguration.maxGenOffset;
        List<Pair<BlockPos, Integer>> list = Lists.newLinkedList();
        List<BlockPos> list1 = Lists.newLinkedList();
        GeodeLayerSettings geodelayersettings = geodeconfiguration.geodeLayerSettings;
        GeodeBlockSettings geodeblocksettings = geodeconfiguration.geodeBlockSettings;
        GeodeCrackSettings geodecracksettings = geodeconfiguration.geodeCrackSettings;
        double d1 = (double)1.0F / Math.sqrt(geodelayersettings.filling);
        int k = geodeconfiguration.distributionPoints.sample(randomsource);
        double d0 = (double)k / (double)geodeconfiguration.outerWallDistance.getMaxValue();
        double d2 = (double)1.0F / Math.sqrt(geodelayersettings.innerLayer + d0);
        double d3 = (double)1.0F / Math.sqrt(geodelayersettings.middleLayer + d0);
        double d4 = (double)1.0F / Math.sqrt(geodelayersettings.outerLayer + d0);
        double d5 = (double)1.0F / Math.sqrt(geodecracksettings.baseCrackSize + randomsource.nextDouble() / (double)2.0F + (k > 3 ? d0 : 0.0D));
        boolean flag = (double)randomsource.nextFloat() < geodecracksettings.generateCrackChance;
        int l = 0;

        for (int i1 = 0; i1 < k; ++i1)
        {
            int j1 = geodeconfiguration.outerWallDistance.sample(randomsource);
            int k1 = geodeconfiguration.outerWallDistance.sample(randomsource);
            int l1 = geodeconfiguration.outerWallDistance.sample(randomsource);
            BlockPos blockpos1 = blockpos.offset(j1, k1, l1);
            BlockState blockstate = worldgenlevel.getBlockState(blockpos1);
            if (blockstate.isAir() ||
                    blockstate.is(BlockTags.GEODE_INVALID_BLOCKS) ||
                    blockstate.is(geodeblocksettings.invalidBlocks))
            {
                ++l;
                if (l > geodeconfiguration.invalidBlocksThreshold) {
                    return false;
                }
            }

            list.add(Pair.of(blockpos1, geodeconfiguration.pointOffset.sample(randomsource)));
        }

        if (flag) {
            int i2 = randomsource.nextInt(4);
            int j2 = k * 2 + 1;
            switch (i2)
            {
                case 0:
                    list1.add(blockpos.offset(j2, 7, 0));
                    list1.add(blockpos.offset(j2, 5, 0));
                    list1.add(blockpos.offset(j2, 1, 0));
                    break;
                case 1:
                    list1.add(blockpos.offset(0, 7, j2));
                    list1.add(blockpos.offset(0, 5, j2));
                    list1.add(blockpos.offset(0, 1, j2));
                    break;
                case 2:
                    list1.add(blockpos.offset(j2, 7, j2));
                    list1.add(blockpos.offset(j2, 5, j2));
                    list1.add(blockpos.offset(j2, 1, j2));
                    break;
                default:
                    list1.add(blockpos.offset(0, 7, 0));
                    list1.add(blockpos.offset(0, 5, 0));
                    list1.add(blockpos.offset(0, 1, 0));
                    break;
            }
        }

        List<BlockPos> list2 = Lists.newArrayList();
        var predicate = FeaturePlacementUtils.IsReplaceable(geodeconfiguration.geodeBlockSettings.cannotReplace);
        NormalNoise normalnoise = NormalNoise.create(worldgenlevel.getRandom(), -4, 1.0D);

        for (BlockPos blockpos3 : BlockPos.betweenClosed(blockpos.offset(i, i, i), blockpos.offset(j, j, j)))
        {
            double d8 = normalnoise.getValue(blockpos3.getX(), blockpos3.getY(), blockpos3.getZ()) * geodeconfiguration.noiseMultiplier;
            double d6 = 0.0D;
            double d7 = 0.0D;

            for (Pair<BlockPos, Integer> pair : list)
            {
                d6 += Mth.invSqrt(blockpos3.distSqr(pair.getFirst()) + (double)pair.getSecond()) + d8;
            }

            for (BlockPos blockpos6 : list1)
            {
                d7 += Mth.invSqrt(blockpos3.distSqr(blockpos6) + (double)geodecracksettings.crackPointOffset) + d8;
            }

            if (d6 >= d4)
            {
                if (flag && d7 >= d5 && d6 < d1) {
                    FeaturePlacementUtils.SafeSetBlock(worldgenlevel, blockpos3, Blocks.AIR.defaultBlockState(), predicate);

                    for (Direction direction1 : Direction.values()) {
                        BlockPos blockpos2 = blockpos3.relative(direction1);
                        FluidState fluidstate = worldgenlevel.getFluidState(blockpos2);
                        if (!fluidstate.isEmpty()) {
                            worldgenlevel.scheduleTick(blockpos2, fluidstate.getType(), 0);
                        }
                    }
                } else if (d6 >= d1) {
                    FeaturePlacementUtils.SafeSetBlock(worldgenlevel, blockpos3, geodeblocksettings.fillingProvider.getState(randomsource, blockpos3), predicate);
                } else if (d6 >= d2) {
                    boolean flag1 = (double)randomsource.nextFloat() < geodeconfiguration.useAlternateLayer0Chance;
                    if (flag1) {
                        FeaturePlacementUtils.SafeSetBlock(worldgenlevel, blockpos3, geodeblocksettings.alternateInnerLayerProvider.getState(randomsource, blockpos3), predicate);
                    } else {
                        FeaturePlacementUtils.SafeSetBlock(worldgenlevel, blockpos3, geodeblocksettings.innerLayerProvider.getState(randomsource, blockpos3), predicate);
                    }

                    if ((!geodeconfiguration.placementsRequireLayer0Alternate || flag1) && (double)randomsource.nextFloat() < geodeconfiguration.usePotentialPlacementsChance) {
                        list2.add(blockpos3.immutable());
                    }
                } else if (d6 >= d3) {
                    FeaturePlacementUtils.SafeSetBlock(worldgenlevel, blockpos3, geodeblocksettings.middleLayerProvider.getState(randomsource, blockpos3), predicate);
                } else if (d6 >= d4) {
                    FeaturePlacementUtils.SafeSetBlock(worldgenlevel, blockpos3, geodeblocksettings.outerLayerProvider.getState(randomsource, blockpos3), predicate);
                }
            }
        }

        List<CompilableTargetBlockState> list3 = geodeblocksettings.innerPlacements;

        for (BlockPos blockpos4 : list2) {
            BlockState blockstate1 = Util.getRandom(list3, randomsource).BlockState;

            for (Direction direction : Direction.values())
            {
                if (blockstate1.hasProperty(BlockStateProperties.FACING)) {
                    blockstate1 = blockstate1.setValue(BlockStateProperties.FACING, direction);
                }

                BlockPos blockpos5 = blockpos4.relative(direction);
                BlockState blockstate2 = worldgenlevel.getBlockState(blockpos5);
                if (blockstate1.hasProperty(BlockStateProperties.WATERLOGGED)) {
                    blockstate1 = blockstate1.setValue(BlockStateProperties.WATERLOGGED, blockstate2.getFluidState().isSource());
                }

                if (BuddingAmethystBlock.canClusterGrowAtState(blockstate2)) {
                    FeaturePlacementUtils.SafeSetBlock(worldgenlevel, blockpos5, blockstate1, predicate);
                    break;
                }
            }
        }

        return true;
    }
}

package com.github.mdcdi1315.mdex.features;

// Portions of code has been copied from Minecraft's 1.20.1 monster room feature.

import com.github.mdcdi1315.mdex.MDEXBalmLayer;
import com.github.mdcdi1315.mdex.block.BlockUtils;
import com.github.mdcdi1315.DotNetLayer.System.Predicate;
import com.github.mdcdi1315.mdex.util.WeightedBlockEntry;
import com.github.mdcdi1315.mdex.features.config.CustomizableMonsterRoomConfiguration;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import com.mojang.serialization.Codec;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;

public final class CustomizableMonsterRoomFeature
    extends ModdedFeature<CustomizableMonsterRoomConfiguration>
{
    public CustomizableMonsterRoomFeature(Codec<CustomizableMonsterRoomConfiguration> cfg) {
        super(cfg);
    }

    private void placeRewardChests(FeaturePlaceContext<CustomizableMonsterRoomConfiguration> fpc , BlockPos blockpos , Predicate<BlockState> predicate , int j , int k1)
    {
        WorldGenLevel worldgenlevel = fpc.level();
        RandomSource randomsource = fpc.random();
        int chestplacercount = fpc.config().ChestConfiguration.Count.sample(randomsource);
        ResourceLocation loottable = fpc.config().ChestConfiguration.LootTable;

        for (int l3 = 0; l3 < chestplacercount; ++l3)
        {
            BlockPos blockpos2 = new BlockPos(
                    blockpos.getX() + randomsource.nextInt(j * 2 + 1) - j,
                    blockpos.getY(),
                    blockpos.getZ() + randomsource.nextInt(k1 * 2 + 1) - k1
            );
            if (worldgenlevel.isEmptyBlock(blockpos2))
            {
                int j3 = 0;

                for (Direction direction : Direction.Plane.HORIZONTAL) {
                    if (BlockUtils.ReferentIsSolidBlock(worldgenlevel.getBlockState(blockpos2.relative(direction)))) {
                        ++j3;
                    }
                }

                if (j3 == 1) {
                    FeaturePlacementUtils.SafeSetBlock(worldgenlevel, blockpos2, StructurePiece.reorient(worldgenlevel, blockpos2, Blocks.CHEST.defaultBlockState()), predicate);
                    RandomizableContainerBlockEntity.setLootTable(worldgenlevel, randomsource, blockpos2, loottable);
                    break;
                }
            }
        }
    }

    private void createSpawner(FeaturePlaceContext<CustomizableMonsterRoomConfiguration> fpc , BlockPos blockpos , Predicate<BlockState> predicate)
    {
        WorldGenLevel wgl = fpc.level();
        RandomSource rs = fpc.random();
        if (FeaturePlacementUtils.SafeSetBlock(wgl, blockpos, fpc.config().SpawnerBlock, predicate))
        {
            FeaturePlacementUtils.MakeTriggeredSpawns(wgl, blockpos , rs , fpc.config().AdditionalEntities , 12);

            BlockEntity blockentity = wgl.getBlockEntity(blockpos);

            if (blockentity instanceof SpawnerBlockEntity sbe) {
                var entry = FeaturePlacementUtils.SampleWeightedFromRandomSource(fpc.config().SpawnerEntityCandidates , rs);
                if (entry != null) {
                    sbe.setEntityId(entry.Entity , rs);
                } else {
                    MDEXBalmLayer.LOGGER.warn("WARN: Cannot set the entity for the mob spawner entity at ({}, {}, {}) because the list lookup returned an empty entry. Setting the spawner to have the 'zombie' mob.", blockpos.getX(), blockpos.getY(), blockpos.getZ());
                    sbe.setEntityId(EntityType.ZOMBIE , rs);
                }
            } else {
                MDEXBalmLayer.LOGGER.error("ERROR: The mob spawner entity at ({}, {}, {}) does not derive from SpawnerBlockEntity, thus no mobs will be applied in the spawner object.", blockpos.getX(), blockpos.getY(), blockpos.getZ());
            }
        }
    }

    @Override
    protected boolean placeModdedFeature(FeaturePlaceContext<CustomizableMonsterRoomConfiguration> fpc)
    {
        Predicate<BlockState> predicate = FeaturePlacementUtils.IsReplaceable(BlockTags.FEATURES_CANNOT_REPLACE);
        BlockPos blockpos = fpc.origin();
        RandomSource randomsource = fpc.random();
        WorldGenLevel worldgenlevel = fpc.level();

        BlockState air_block = Blocks.CAVE_AIR.defaultBlockState();

        int j = randomsource.nextInt(2) + 2;
        int k = -j - 1;
        int l = j + 1;
        int k1 = randomsource.nextInt(2) + 2;
        int l1 = -k1 - 1;
        int i2 = k1 + 1;
        int j2 = 0;

        for (int k2 = k; k2 <= l; ++k2)
        {
            for (int l2 = -1; l2 < 5; ++l2)
            {
                for (int i3 = l1; i3 <= i2; ++i3)
                {
                    BlockPos blockpos1 = blockpos.offset(k2, l2, i3);
                    boolean flag = worldgenlevel.getBlockState(blockpos1).isAir();
                    if (l2 == -1 && flag) {
                        return false;
                    }

                    if (l2 == 4 && flag) {
                        return false;
                    }

                    if ((k2 == k || k2 == l || i3 == l1 || i3 == i2) && l2 == 0 && flag && worldgenlevel.getBlockState(blockpos1.above()).isAir()) {
                        ++j2;
                    }
                }
            }
        }

        if (j2 < 1 || j2 > 5) { return false; }

        float probability = fpc.config().RareStonePlacementProbability;
        WeightedRandomList<WeightedBlockEntry> entries = WeightedRandomList.create(fpc.config().RareStoneBlocks);

        for (int k3 = k; k3 <= l; ++k3)
        {
            for (int i4 = 3; i4 >= -1; --i4)
            {
                for (int k4 = l1; k4 <= i2; ++k4)
                {
                    BlockPos blockpos3 = blockpos.offset(k3, i4, k4);
                    BlockState blockstate = worldgenlevel.getBlockState(blockpos3);
                    if (k3 != k && i4 != -1 && k4 != l1 && k3 != l && i4 != 4 && k4 != i2)
                    {
                        if (!blockstate.is(Blocks.CHEST) && !blockstate.is(fpc.config().SpawnerBlock.getBlock()))
                        {
                            FeaturePlacementUtils.SafeSetBlock(worldgenlevel, blockpos3, air_block, predicate);
                        }
                    } else if (blockpos3.getY() >= worldgenlevel.getMinBuildHeight() && BlockUtils.ReferentIsAirBlock(worldgenlevel.getBlockState(blockpos3.below()))) {
                        worldgenlevel.setBlock(blockpos3, air_block, 2);
                    } else if (BlockUtils.ReferentIsSolidBlock(blockstate) && !blockstate.is(Blocks.CHEST)) {
                        if (randomsource.nextFloat() < probability) {
                            entries.getRandom(randomsource).ifPresent(weightedBlockEntry -> FeaturePlacementUtils.SafeSetBlock(
                                    worldgenlevel, blockpos3, weightedBlockEntry.Block.defaultBlockState(), predicate
                            ));
                        } else {
                            FeaturePlacementUtils.SafeSetBlock(worldgenlevel, blockpos3, fpc.config().BaseStoneBlock, predicate);
                        }
                    }
                }
            }
        }

        placeRewardChests(fpc , blockpos , predicate , j , k1);

        createSpawner(fpc , blockpos , predicate);

        return true;
    }
}

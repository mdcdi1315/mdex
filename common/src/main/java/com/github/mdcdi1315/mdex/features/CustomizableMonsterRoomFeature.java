package com.github.mdcdi1315.mdex.features;

// Portions of code has been copied from Minecraft's 1.20.1 monster room feature.

import com.github.mdcdi1315.mdex.MDEXBalmLayer;
import com.github.mdcdi1315.mdex.block.BlockUtils;
import com.github.mdcdi1315.DotNetLayer.System.Predicate;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public final class CustomizableMonsterRoomFeature
    extends ModdedFeature<CustomizableMonsterRoomConfiguration>
{
    public CustomizableMonsterRoomFeature(Codec<CustomizableMonsterRoomConfiguration> cfg) {
        super(cfg);
    }

    private static void PlaceRewardChests(FeaturePlaceContext<CustomizableMonsterRoomConfiguration> fpc , BlockPos blockpos , Predicate<BlockState> predicate , int j , int k1)
    {
        WorldGenLevel worldgenlevel = fpc.level();
        RandomSource randomsource = fpc.random();
        byte attempts = (byte) fpc.config().ChestConfiguration.Count().sample(randomsource); // This value will be up to 32 , so we can safely cast to a byte value
        ResourceLocation loottable = fpc.config().ChestConfiguration.LootTable();

        for (byte attempt = 0; attempt < attempts; ++attempt)
        {
            BlockPos blockpos2 = new BlockPos(
                    blockpos.getX() + randomsource.nextInt(j * 2 + 1) - j,
                    blockpos.getY(),
                    blockpos.getZ() + randomsource.nextInt(k1 * 2 + 1) - k1
            );
            if (worldgenlevel.isEmptyBlock(blockpos2))
            {
                byte j3 = 0;

                for (Direction direction : Direction.Plane.HORIZONTAL) {
                    if (BlockUtils.ReferentIsSolidBlock(worldgenlevel.getBlockState(blockpos2.relative(direction)))) {
                        j3++;
                    }
                }

                if (j3 == 1) {
                    FeaturePlacementUtils.SafeSetBlock(worldgenlevel, blockpos2, StructurePiece.reorient(worldgenlevel, blockpos2, Blocks.CHEST.defaultBlockState()), predicate);
                    BlockUtils.SetRandomizableContainerLootTable(worldgenlevel, randomsource, blockpos2, loottable);
                    break;
                }
            }
        }
    }

    private static void CreateSpawner(FeaturePlaceContext<CustomizableMonsterRoomConfiguration> fpc , BlockPos blockpos , Predicate<BlockState> predicate)
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

        byte size = 0;

        for (int xofs = k; xofs <= l; ++xofs)
        {
            for (short yofs = -1; yofs < 5; ++yofs)
            {
                for (int zofs = l1; zofs <= i2; ++zofs)
                {
                    BlockPos blockpos1 = blockpos.offset(xofs, yofs, zofs);

                    if (worldgenlevel.getBlockState(blockpos1).isAir())
                    {
                        switch (yofs)
                        {
                            case 4:
                            case -1:
                                return false;
                            case 0:
                                if ((xofs == k || xofs == l || zofs == l1 || zofs == i2) && worldgenlevel.getBlockState(blockpos1.above()).isAir()) {
                                    if (++size > 5) {
                                        return false;
                                    }
                                }
                                break;
                        }
                    }
                }
            }
        }

        if (size < 1) { return false; }

        for (int k3 = k; k3 <= l; ++k3)
        {
            for (short i4 = 3; i4 > -2; --i4)
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
                    } else if (blockpos3.getY() >= worldgenlevel.getMinY() && BlockUtils.ReferentIsAirBlock(worldgenlevel.getBlockState(blockpos3.below()))) {
                        worldgenlevel.setBlock(blockpos3, air_block, 2);
                    } else if (BlockUtils.ReferentIsSolidBlock(blockstate) && !blockstate.is(Blocks.CHEST)) {
                        FeaturePlacementUtils.SafeSetBlock(worldgenlevel , blockpos3 , fpc.config().StoneBlockProvider.getState(randomsource , blockpos3) , predicate);
                    }
                }
            }
        }

        PlaceRewardChests(fpc , blockpos , predicate , j , k1);

        CreateSpawner(fpc , blockpos , predicate);

        return true;
    }
}

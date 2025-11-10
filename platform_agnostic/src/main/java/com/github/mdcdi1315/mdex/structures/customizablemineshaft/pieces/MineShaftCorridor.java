package com.github.mdcdi1315.mdex.structures.customizablemineshaft.pieces;

import com.github.mdcdi1315.mdex.block.BlockUtils;
import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.MaybeNull;
import com.github.mdcdi1315.mdex.structures.customizablemineshaft.CustomizableMineshaftPiecesSettings;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.entity.vehicle.MinecartChest;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;

public final class MineShaftCorridor
        extends AbstractMineshaftPiece
{
    private byte state;
    private static final byte STATE_HAS_RAILS = 1 << 0 ,
      STATE_IS_SPIDER_CORRIDOR = 1 << 1,
      STATE_HAS_PLACED_SPIDER = 1 << 2;
    private final int numSections;

    private void SetState(byte s)
    {
        state |= s;
    }

    private boolean HasStateFlag(byte b)
    {
        return (state & b) == b;
    }

    private boolean HasNotStateFlag(byte b)
    {
        return (state & b) != b;
    }

    public MineShaftCorridor(CompoundTag tag) {
        super(MineShaftCorridorType.INSTANCE , tag);
        this.state = tag.getByte("state");
        this.numSections = tag.getInt("Num");
    }

    protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
        super.addAdditionalSaveData(context, tag);
        tag.putByte("state" , state);
        tag.putInt("Num", this.numSections);
    }

    public MineShaftCorridor(int genDepth, RandomSource random, BoundingBox boundingBox, Direction orientation, CustomizableMineshaftPiecesSettings settings) {
        super(settings, MineShaftCorridorType.INSTANCE, genDepth, boundingBox);
        this.setOrientation(orientation);
        if (random.nextInt(3) == 0) {
            SetState(STATE_HAS_RAILS);
        } else if (random.nextInt(23) == 0) {
            SetState(STATE_IS_SPIDER_CORRIDOR);
        }
        Direction ort = this.getOrientation();
        if (ort != null && ort.getAxis() == Direction.Axis.Z) {
            this.numSections = boundingBox.getZSpan() / MineshaftPieces.DEFAULT_SHAFT_LENGTH;
        } else {
            this.numSections = boundingBox.getXSpan() / MineshaftPieces.DEFAULT_SHAFT_LENGTH;
        }
    }

    @MaybeNull
    public static BoundingBox findCorridorSize(StructurePieceAccessor pieces, RandomSource random, int x, int y, int z, Direction direction) {
        for (int i = random.nextInt(3) + 2; i > 0; --i)
        {
            int j = i * MineshaftPieces.DEFAULT_SHAFT_LENGTH;
            BoundingBox boundingbox = switch (direction) {
                case SOUTH -> new BoundingBox(0, 0, 0, 2, 2, j - 1);
                case WEST -> new BoundingBox(-(j - 1), 0, 0, 0, 2, 2);
                case EAST -> new BoundingBox(0, 0, 0, j - 1, 2, 2);
                default -> new BoundingBox(0, 0, -(j - 1), 2, 2, 0);
            };

            boundingbox = boundingbox.moved(x , y , z);

            if (pieces.findCollisionPiece(boundingbox) == null) {
                return boundingbox;
            }
        }

        return null;
    }

    public void addChildren(StructurePiece piece, StructurePieceAccessor pieces, RandomSource random) {
        int depth = this.getGenDepth();
        int j = random.nextInt(4);
        Direction direction = this.getOrientation();
        if (direction != null) {
            switch (direction) {
                case SOUTH:
                    if (j < 2) {
                        MineshaftPieces.GenerateAndAddPiece(piece, pieces, random, this.boundingBox.minX(), this.boundingBox.minY() - 1 + random.nextInt(3), this.boundingBox.maxZ() + 1, direction, depth);
                    } else if (j == 2) {
                        MineshaftPieces.GenerateAndAddPiece(piece, pieces, random, this.boundingBox.minX() - 1, this.boundingBox.minY() - 1 + random.nextInt(3), this.boundingBox.maxZ() - 3, Direction.WEST, depth);
                    } else {
                        MineshaftPieces.GenerateAndAddPiece(piece, pieces, random, this.boundingBox.maxX() + 1, this.boundingBox.minY() - 1 + random.nextInt(3), this.boundingBox.maxZ() - 3, Direction.EAST, depth);
                    }
                    break;
                case WEST:
                    if (j < 2) {
                        MineshaftPieces.GenerateAndAddPiece(piece, pieces, random, this.boundingBox.minX() - 1, this.boundingBox.minY() - 1 + random.nextInt(3), this.boundingBox.minZ(), direction, depth);
                    } else if (j == 2) {
                        MineshaftPieces.GenerateAndAddPiece(piece, pieces, random, this.boundingBox.minX(), this.boundingBox.minY() - 1 + random.nextInt(3), this.boundingBox.minZ() - 1, Direction.NORTH, depth);
                    } else {
                        MineshaftPieces.GenerateAndAddPiece(piece, pieces, random, this.boundingBox.minX(), this.boundingBox.minY() - 1 + random.nextInt(3), this.boundingBox.maxZ() + 1, Direction.SOUTH, depth);
                    }
                    break;
                case EAST:
                    if (j < 2) {
                        MineshaftPieces.GenerateAndAddPiece(piece, pieces, random, this.boundingBox.maxX() + 1, this.boundingBox.minY() - 1 + random.nextInt(3), this.boundingBox.minZ(), direction, depth);
                    } else if (j == 2) {
                        MineshaftPieces.GenerateAndAddPiece(piece, pieces, random, this.boundingBox.maxX() - 3, this.boundingBox.minY() - 1 + random.nextInt(3), this.boundingBox.minZ() - 1, Direction.NORTH, depth);
                    } else {
                        MineshaftPieces.GenerateAndAddPiece(piece, pieces, random, this.boundingBox.maxX() - 3, this.boundingBox.minY() - 1 + random.nextInt(3), this.boundingBox.maxZ() + 1, Direction.SOUTH, depth);
                    }
                    break;
                case NORTH:
                default:
                    if (j < 2) {
                        MineshaftPieces.GenerateAndAddPiece(piece, pieces, random, this.boundingBox.minX(), this.boundingBox.minY() - 1 + random.nextInt(3), this.boundingBox.minZ() - 1, direction, depth);
                    } else if (j == 2) {
                        MineshaftPieces.GenerateAndAddPiece(piece, pieces, random, this.boundingBox.minX() - 1, this.boundingBox.minY() - 1 + random.nextInt(3), this.boundingBox.minZ(), Direction.WEST, depth);
                    } else {
                        MineshaftPieces.GenerateAndAddPiece(piece, pieces, random, this.boundingBox.maxX() + 1, this.boundingBox.minY() - 1 + random.nextInt(3), this.boundingBox.minZ(), Direction.EAST, depth);
                    }
                    break;
            }
        }

        if (depth < MineshaftPieces.MAX_DEPTH) {
            if (direction != Direction.NORTH && direction != Direction.SOUTH) {
                for (int i1 = this.boundingBox.minX() + 3; i1 + 3 <= this.boundingBox.maxX(); i1 += MineshaftPieces.DEFAULT_SHAFT_LENGTH)
                {
                    switch (random.nextInt(5))
                    {
                        case 0:
                            MineshaftPieces.GenerateAndAddPiece(piece, pieces, random, i1, this.boundingBox.minY(), this.boundingBox.minZ() - 1, Direction.NORTH, depth + 1);
                            break;
                        case 1:
                            MineshaftPieces.GenerateAndAddPiece(piece, pieces, random, i1, this.boundingBox.minY(), this.boundingBox.maxZ() + 1, Direction.SOUTH, depth + 1);
                            break;
                    }
                }
            } else {
                for (int k = this.boundingBox.minZ() + 3; k + 3 <= this.boundingBox.maxZ(); k += MineshaftPieces.DEFAULT_SHAFT_LENGTH) {
                    switch (random.nextInt(5))
                    {
                        case 0:
                            MineshaftPieces.GenerateAndAddPiece(piece, pieces, random, this.boundingBox.minX() - 1, this.boundingBox.minY(), k, Direction.WEST, depth + 1);
                            break;
                        case 1:
                            MineshaftPieces.GenerateAndAddPiece(piece, pieces, random, this.boundingBox.maxX() + 1, this.boundingBox.minY(), k, Direction.EAST, depth + 1);
                            break;
                    }
                }
            }
        }
    }

    private boolean createChest(WorldGenLevel level, BoundingBox box, RandomSource random, int x, int y, int z, ResourceLocation lootTable)
    {
        BlockPos blockpos = this.getWorldPos(x, y, z);
        if (box.isInside(blockpos) && BlockUtils.BlockIsSolidAndAboveIsAir(level , blockpos.below())) {
            this.placeBlock(level, Blocks.RAIL.defaultBlockState().setValue(RailBlock.SHAPE, random.nextBoolean() ? RailShape.NORTH_SOUTH : RailShape.EAST_WEST), x, y, z, box);
            MinecartChest minecartchest = new MinecartChest(level.getLevel(), blockpos.getX() + 0.5d, blockpos.getY() + 0.5d, blockpos.getZ() + 0.5d);
            minecartchest.setLootTable(ResourceKey.create(Registries.LOOT_TABLE, lootTable), random.nextLong());
            level.addFreshEntity(minecartchest);
            return true;
        } else {
            return false;
        }
    }

    public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox box, ChunkPos chunkPos, BlockPos pos)
    {
        if (!IsInInvalidLocation(level, box))
        {
            boolean spider_corridor = HasStateFlag(STATE_IS_SPIDER_CORRIDOR);
            int i1 = this.numSections * MineshaftPieces.DEFAULT_SHAFT_LENGTH - 1;
            this.generateBox(level, box, 0, 0, 0, 2, 1, i1, CAVE_AIR, CAVE_AIR, false);
            this.generateMaybeBox(level, box, random, 0.8F, 0, 2, 0, 2, 2, i1, CAVE_AIR, CAVE_AIR, false, false);
            if (spider_corridor) {
                this.generateMaybeBox(level, box, random, 0.6F, 0, 0, 0, 2, 1, i1, Blocks.COBWEB.defaultBlockState(), CAVE_AIR, false, true);
            }

            for (int section = 0; section < this.numSections; ++section)
            {
                int k1 = 2 + section * 5;
                this.placeSupport(level, box, 0, 0, k1, 2, 2, random);
                this.maybePlaceCobWeb(level, box, random, 0.1F, 0, 2, k1 - 1);
                this.maybePlaceCobWeb(level, box, random, 0.1F, 2, 2, k1 - 1);
                this.maybePlaceCobWeb(level, box, random, 0.1F, 0, 2, k1 + 1);
                this.maybePlaceCobWeb(level, box, random, 0.1F, 2, 2, k1 + 1);
                this.maybePlaceCobWeb(level, box, random, 0.05F, 0, 2, k1 - 2);
                this.maybePlaceCobWeb(level, box, random, 0.05F, 2, 2, k1 - 2);
                this.maybePlaceCobWeb(level, box, random, 0.05F, 0, 2, k1 + 2);
                this.maybePlaceCobWeb(level, box, random, 0.05F, 2, 2, k1 + 2);
                // If settings.MinecartPlacementProbability is 0.01f, this is like the older code
                // that did: random.nextInt(100) == 0.
                // That is, a 1/100 probability. (Oh my!)
                if (random.nextFloat() < settings.MinecartPlacementProbability) {
                    this.createChest(level, box, random, 2, 0, k1 - 1, settings.MinecartsLootTable);
                }

                if (random.nextFloat() < settings.MinecartPlacementProbability) {
                    this.createChest(level, box, random, 0, 0, k1 + 1, settings.MinecartsLootTable);
                }

                if (spider_corridor && HasNotStateFlag(STATE_HAS_PLACED_SPIDER)) {
                    int z = k1 - 1 + random.nextInt(3);
                    BlockPos blockpos = this.getWorldPos(1, 0, z);
                    if (box.isInside(blockpos) && this.isInterior(level, 1, 0, z, box)) {
                        SetState(STATE_HAS_PLACED_SPIDER);
                        level.setBlock(blockpos, Blocks.SPAWNER.defaultBlockState(), 2);
                        BlockEntity blockentity = level.getBlockEntity(blockpos);
                        if (blockentity instanceof SpawnerBlockEntity sbe) {
                            sbe.setEntityId(EntityType.CAVE_SPIDER, random);
                        }
                    }
                }
            }

            for (byte j2 = 0; j2 < 3; ++j2)
            {
                for (int l2 = 0; l2 <= i1; ++l2)
                {
                    this.SetPlanksBlock(level, box, j2, -1, l2);
                }
            }

            this.placeDoubleLowerOrUpperSupport(level, box, 0, -1, 2);
            if (this.numSections > 1) {
                this.placeDoubleLowerOrUpperSupport(level, box, 0, -1, i1 - 2);
            }

            if (HasStateFlag(STATE_HAS_RAILS))
            {
                BlockState blockstate1 = Blocks.RAIL.defaultBlockState().setValue(RailBlock.SHAPE, RailShape.NORTH_SOUTH);

                for (int j3 = 0; j3 <= i1; ++j3) {
                    BlockState blockstate2 = this.getBlock(level, 1, -1, j3, box);
                    if (BlockUtils.ReferentIsSolidBlock(blockstate2) && blockstate2.isSolidRender(level, this.getWorldPos(1, -1, j3))) {
                        this.maybeGenerateBlock(level, box, random, this.isInterior(level, 1, 0, j3, box) ? 0.7F : 0.9F, 1, 0, j3, blockstate1);
                    }
                }
            }
        }

    }

    private void placeDoubleLowerOrUpperSupport(LevelAccessor level, BoundingBox box, int x, int y, int z)
    {
        BlockState blockstate = settings.WoodState;
        BlockState blockstate1 = settings.PlanksState;
        if (this.getBlock(level, x, y, z, box).is(blockstate1.getBlock())) {
            this.fillPillarDownOrChainUp(level, blockstate, x, y, z, box);
        }

        if (this.getBlock(level, x + 2, y, z, box).is(blockstate1.getBlock())) {
            this.fillPillarDownOrChainUp(level, blockstate, x + 2, y, z, box);
        }

    }

    protected void fillColumnDown(WorldGenLevel level, BlockState state, int x, int y, int z, BoundingBox box) {
        BlockPos.MutableBlockPos blockpos$mutableblockpos = this.getWorldPos(x, y, z);
        if (box.isInside(blockpos$mutableblockpos)) {
            int i = blockpos$mutableblockpos.getY();

            while (this.isReplaceableByStructures(level.getBlockState(blockpos$mutableblockpos)) && blockpos$mutableblockpos.getY() > level.getMinBuildHeight() + 1) {
                blockpos$mutableblockpos.move(Direction.DOWN);
            }

            if (this.canPlaceColumnOnTopOf(level, blockpos$mutableblockpos, level.getBlockState(blockpos$mutableblockpos))) {
                while (blockpos$mutableblockpos.getY() < i) {
                    blockpos$mutableblockpos.move(Direction.UP);
                    level.setBlock(blockpos$mutableblockpos, state, 2);
                }
            }
        }

    }

    private void fillPillarDownOrChainUp(LevelAccessor level, BlockState state, int x, int y, int z, BoundingBox box) {
        BlockPos.MutableBlockPos blockpos$mutableblockpos = this.getWorldPos(x, y, z);
        if (box.isInside(blockpos$mutableblockpos)) {
            int i = blockpos$mutableblockpos.getY(), j = 1;
            boolean flag = true;

            for (boolean flag1 = true; flag || flag1; ++j) {
                if (flag) {
                    blockpos$mutableblockpos.setY(i - j);
                    BlockState blockstate = level.getBlockState(blockpos$mutableblockpos);
                    boolean flag2 = this.isReplaceableByStructures(blockstate) && !blockstate.is(Blocks.LAVA);
                    if (!flag2 && this.canPlaceColumnOnTopOf(level, blockpos$mutableblockpos, blockstate)) {
                        fillColumnBetween(level, state, blockpos$mutableblockpos, i - j + 1, i);
                        return;
                    }

                    flag = j < (MineshaftPieces.MAX_PILLAR_HEIGHT + 1) && flag2 && blockpos$mutableblockpos.getY() > level.getMinBuildHeight() + 1;
                }

                if (flag1) {
                    blockpos$mutableblockpos.setY(i + j);
                    BlockState blockstate1 = level.getBlockState(blockpos$mutableblockpos);
                    boolean flag3 = this.isReplaceableByStructures(blockstate1);
                    if (!flag3 && this.canHangChainBelow(level, blockpos$mutableblockpos, blockstate1)) {
                        level.setBlock(blockpos$mutableblockpos.setY(i + 1), settings.FenceState, 2);
                        fillColumnBetween(level, Blocks.CHAIN.defaultBlockState(), blockpos$mutableblockpos, i + 2, i + j);
                        return;
                    }

                    flag1 = j < (MineshaftPieces.MAX_CHAIN_HEIGHT + 1) && flag3 && blockpos$mutableblockpos.getY() < level.getMaxBuildHeight() - 1;
                }
            }
        }

    }

    private static void fillColumnBetween(LevelWriter level, BlockState state, BlockPos.MutableBlockPos pos, int minY, int maxY) {
        for (int i = minY; i < maxY; ++i) {
            level.setBlock(pos.setY(i), state, 2);
        }

    }

    private boolean canPlaceColumnOnTopOf(LevelReader level, BlockPos pos, BlockState state) {
        return state.isFaceSturdy(level, pos, Direction.UP);
    }

    private boolean canHangChainBelow(LevelReader level, BlockPos pos, BlockState state) {
        return Block.canSupportCenter(level, pos, Direction.DOWN) && !(state.getBlock() instanceof FallingBlock);
    }

    private void placeSupport(WorldGenLevel level, BoundingBox box, int minX, int minY, int z, int maxY, int maxX, RandomSource random) {
        if (IsSupportingBox(level, box, minX, maxX, maxY, z))
        {
            BlockState blockstate = settings.PlanksState;
            BlockState blockstate1 = settings.FenceState;
            this.generateBox(level, box, minX, minY, z, minX, maxY - 1, z, blockstate1.setValue(FenceBlock.WEST, true), CAVE_AIR, false);
            this.generateBox(level, box, maxX, minY, z, maxX, maxY - 1, z, blockstate1.setValue(FenceBlock.EAST, true), CAVE_AIR, false);
            if (random.nextInt(4) == 0) {
                this.generateBox(level, box, minX, maxY, z, minX, maxY, z, blockstate, CAVE_AIR, false);
                this.generateBox(level, box, maxX, maxY, z, maxX, maxY, z, blockstate, CAVE_AIR, false);
            } else {
                this.generateBox(level, box, minX, maxY, z, maxX, maxY, z, blockstate, CAVE_AIR, false);
                this.maybeGenerateBlock(level, box, random, 0.05F, minX + 1, maxY, z - 1, settings.TorchState.setValue(WallTorchBlock.FACING, Direction.SOUTH));
                this.maybeGenerateBlock(level, box, random, 0.05F, minX + 1, maxY, z + 1, settings.TorchState.setValue(WallTorchBlock.FACING, Direction.NORTH));
            }
        }

    }

    private void maybePlaceCobWeb(WorldGenLevel level, BoundingBox box, RandomSource random, float chance, int x, int y, int z) {
        if (this.isInterior(level, x, y, z, box) && random.nextFloat() < chance && this.hasSturdyNeighbours(level, box, x, y, z, (byte)2)) {
            this.placeBlock(level, Blocks.COBWEB.defaultBlockState(), x, y, z, box);
        }
    }

    private boolean hasSturdyNeighbours(WorldGenLevel level, BoundingBox box, int x, int y, int z, byte required) {
        BlockPos.MutableBlockPos blockpos$mutableblockpos = this.getWorldPos(x, y, z);
        byte i = 0;

        for (Direction direction : Direction.values()) {
            blockpos$mutableblockpos.move(direction);
            if (box.isInside(blockpos$mutableblockpos) && level.getBlockState(blockpos$mutableblockpos).isFaceSturdy(level, blockpos$mutableblockpos, direction.getOpposite())) {
                if (++i >= required) {
                    return true;
                }
            }

            blockpos$mutableblockpos.move(direction.getOpposite());
        }

        return false;
    }
}
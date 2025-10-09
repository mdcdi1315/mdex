package com.github.mdcdi1315.mdex.structures.customizablemineshaft.pieces;

import com.github.mdcdi1315.mdex.block.BlockUtils;
import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.NotNull;
import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.MaybeNull;
import com.github.mdcdi1315.mdex.structures.customizablemineshaft.CustomizableMineshaftPiecesSettings;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.*;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;

public final class MineShaftCrossing
        extends AbstractMineshaftPiece
{
    private final Direction direction;
    private final boolean isTwoFloored;

    public MineShaftCrossing(CompoundTag tag) {
        super(MineShaftCrossingType.INSTANCE, tag);
        this.isTwoFloored = tag.getBooleanOr("tf" , false);
        this.direction =  tag.read("D", Direction.LEGACY_ID_CODEC_2D).orElse(Direction.SOUTH);
    }

    protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
        super.addAdditionalSaveData(context, tag);
        tag.putBoolean("tf", this.isTwoFloored);
        tag.putInt("D", this.direction.get2DDataValue());
    }

    public MineShaftCrossing(int genDepth, BoundingBox boundingBox, @MaybeNull Direction direction, CustomizableMineshaftPiecesSettings settings) {
        super(settings, MineShaftCrossingType.INSTANCE, genDepth, boundingBox);
        this.direction = direction;
        this.isTwoFloored = boundingBox.getYSpan() > 3;
    }

    @MaybeNull
    public static BoundingBox findCrossing(StructurePieceAccessor pieces, RandomSource random, int x, int y, int z, Direction direction)
    {
        int i = (random.nextInt(4) == 0) ? 6 : 2;

        BoundingBox boundingbox = switch (direction) {
            case SOUTH -> new BoundingBox(-1, 0, 0, 3, i, 4);
            case WEST -> new BoundingBox(-4, 0, -1, 0, i, 3);
            case EAST -> new BoundingBox(0, 0, -1, 4, i, 3);
            default -> new BoundingBox(-1, 0, -4, 3, i, 0);
        };

        boundingbox = boundingbox.moved(x , y , z);

        // It is faster to check for null rather for the opposite.
        return pieces.findCollisionPiece(boundingbox) == null ? boundingbox : null;
    }

    public void addChildren(StructurePiece piece, StructurePieceAccessor pieces, RandomSource random) {
        int i = this.getGenDepth();
        switch (this.direction)
        {
            case SOUTH:
                MineshaftPieces.GenerateAndAddPiece(piece, pieces, random, this.boundingBox.minX() + 1, this.boundingBox.minY(), this.boundingBox.maxZ() + 1, Direction.SOUTH, i);
                MineshaftPieces.GenerateAndAddPiece(piece, pieces, random, this.boundingBox.minX() - 1, this.boundingBox.minY(), this.boundingBox.minZ() + 1, Direction.WEST, i);
                MineshaftPieces.GenerateAndAddPiece(piece, pieces, random, this.boundingBox.maxX() + 1, this.boundingBox.minY(), this.boundingBox.minZ() + 1, Direction.EAST, i);
                break;
            case WEST:
                MineshaftPieces.GenerateAndAddPiece(piece, pieces, random, this.boundingBox.minX() + 1, this.boundingBox.minY(), this.boundingBox.minZ() - 1, Direction.NORTH, i);
                MineshaftPieces.GenerateAndAddPiece(piece, pieces, random, this.boundingBox.minX() + 1, this.boundingBox.minY(), this.boundingBox.maxZ() + 1, Direction.SOUTH, i);
                MineshaftPieces.GenerateAndAddPiece(piece, pieces, random, this.boundingBox.minX() - 1, this.boundingBox.minY(), this.boundingBox.minZ() + 1, Direction.WEST, i);
                break;
            case EAST:
                MineshaftPieces.GenerateAndAddPiece(piece, pieces, random, this.boundingBox.minX() + 1, this.boundingBox.minY(), this.boundingBox.minZ() - 1, Direction.NORTH, i);
                MineshaftPieces.GenerateAndAddPiece(piece, pieces, random, this.boundingBox.minX() + 1, this.boundingBox.minY(), this.boundingBox.maxZ() + 1, Direction.SOUTH, i);
                MineshaftPieces.GenerateAndAddPiece(piece, pieces, random, this.boundingBox.maxX() + 1, this.boundingBox.minY(), this.boundingBox.minZ() + 1, Direction.EAST, i);
                break;
            case NORTH:
            default:
                MineshaftPieces.GenerateAndAddPiece(piece, pieces, random, this.boundingBox.minX() + 1, this.boundingBox.minY(), this.boundingBox.minZ() - 1, Direction.NORTH, i);
                MineshaftPieces.GenerateAndAddPiece(piece, pieces, random, this.boundingBox.minX() - 1, this.boundingBox.minY(), this.boundingBox.minZ() + 1, Direction.WEST, i);
                MineshaftPieces.GenerateAndAddPiece(piece, pieces, random, this.boundingBox.maxX() + 1, this.boundingBox.minY(), this.boundingBox.minZ() + 1, Direction.EAST, i);
                break;
        }

        if (this.isTwoFloored) {
            if (random.nextBoolean()) {
                MineshaftPieces.GenerateAndAddPiece(piece, pieces, random, this.boundingBox.minX() + 1, this.boundingBox.minY() + MineshaftPieces.DEFAULT_SHAFT_HEIGHT + 1, this.boundingBox.minZ() - 1, Direction.NORTH, i);
            }

            if (random.nextBoolean()) {
                MineshaftPieces.GenerateAndAddPiece(piece, pieces, random, this.boundingBox.minX() - 1, this.boundingBox.minY() + MineshaftPieces.DEFAULT_SHAFT_HEIGHT + 1, this.boundingBox.minZ() + 1, Direction.WEST, i);
            }

            if (random.nextBoolean()) {
                MineshaftPieces.GenerateAndAddPiece(piece, pieces, random, this.boundingBox.maxX() + 1, this.boundingBox.minY() + MineshaftPieces.DEFAULT_SHAFT_HEIGHT + 1, this.boundingBox.minZ() + 1, Direction.EAST, i);
            }

            if (random.nextBoolean()) {
                MineshaftPieces.GenerateAndAddPiece(piece, pieces, random, this.boundingBox.minX() + 1, this.boundingBox.minY() + MineshaftPieces.DEFAULT_SHAFT_HEIGHT + 1, this.boundingBox.maxZ() + 1, Direction.SOUTH, i);
            }
        }

    }

    public void postProcess(@NotNull WorldGenLevel level, @NotNull StructureManager structureManager, @NotNull ChunkGenerator generator, @NotNull RandomSource random, @NotNull BoundingBox box, @NotNull ChunkPos chunkPos, @NotNull BlockPos pos) {
        if (!IsInInvalidLocation(level, box)) {
            if (this.isTwoFloored) {
                this.generateBox(level, box, this.boundingBox.minX() + 1, this.boundingBox.minY(), this.boundingBox.minZ(), this.boundingBox.maxX() - 1, this.boundingBox.minY() + 3 - 1, this.boundingBox.maxZ(), CAVE_AIR, CAVE_AIR, false);
                this.generateBox(level, box, this.boundingBox.minX(), this.boundingBox.minY(), this.boundingBox.minZ() + 1, this.boundingBox.maxX(), this.boundingBox.minY() + 3 - 1, this.boundingBox.maxZ() - 1, CAVE_AIR, CAVE_AIR, false);
                this.generateBox(level, box, this.boundingBox.minX() + 1, this.boundingBox.maxY() - 2, this.boundingBox.minZ(), this.boundingBox.maxX() - 1, this.boundingBox.maxY(), this.boundingBox.maxZ(), CAVE_AIR, CAVE_AIR, false);
                this.generateBox(level, box, this.boundingBox.minX(), this.boundingBox.maxY() - 2, this.boundingBox.minZ() + 1, this.boundingBox.maxX(), this.boundingBox.maxY(), this.boundingBox.maxZ() - 1, CAVE_AIR, CAVE_AIR, false);
                this.generateBox(level, box, this.boundingBox.minX() + 1, this.boundingBox.minY() + 3, this.boundingBox.minZ() + 1, this.boundingBox.maxX() - 1, this.boundingBox.minY() + 3, this.boundingBox.maxZ() - 1, CAVE_AIR, CAVE_AIR, false);
            } else {
                this.generateBox(level, box, this.boundingBox.minX() + 1, this.boundingBox.minY(), this.boundingBox.minZ(), this.boundingBox.maxX() - 1, this.boundingBox.maxY(), this.boundingBox.maxZ(), CAVE_AIR, CAVE_AIR, false);
                this.generateBox(level, box, this.boundingBox.minX(), this.boundingBox.minY(), this.boundingBox.minZ() + 1, this.boundingBox.maxX(), this.boundingBox.maxY(), this.boundingBox.maxZ() - 1, CAVE_AIR, CAVE_AIR, false);
            }

            this.placeSupportPillar(level, box, this.boundingBox.minX() + 1, this.boundingBox.minY(), this.boundingBox.minZ() + 1, this.boundingBox.maxY());
            this.placeSupportPillar(level, box, this.boundingBox.minX() + 1, this.boundingBox.minY(), this.boundingBox.maxZ() - 1, this.boundingBox.maxY());
            this.placeSupportPillar(level, box, this.boundingBox.maxX() - 1, this.boundingBox.minY(), this.boundingBox.minZ() + 1, this.boundingBox.maxY());
            this.placeSupportPillar(level, box, this.boundingBox.maxX() - 1, this.boundingBox.minY(), this.boundingBox.maxZ() - 1, this.boundingBox.maxY());
            int i = this.boundingBox.minY() - 1;

            for (int j = this.boundingBox.minX(); j <= this.boundingBox.maxX(); ++j) {
                for (int k = this.boundingBox.minZ(); k <= this.boundingBox.maxZ(); ++k) {
                    SetPlanksBlock(level, box, j, i, k);
                }
            }
        }

    }

    private void placeSupportPillar(WorldGenLevel level, BoundingBox box, int x, int y, int z, int maxY) {
        if (BlockUtils.ReferentIsSolidBlockUnsafe(this.getBlock(level, x, maxY + 1, z, box))) {
            this.generateBox(level, box, x, y, z, x, maxY, z, settings.PlanksState, CAVE_AIR, false);
        }
    }
}

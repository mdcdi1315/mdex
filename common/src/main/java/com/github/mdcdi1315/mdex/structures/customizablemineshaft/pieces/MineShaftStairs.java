package com.github.mdcdi1315.mdex.structures.customizablemineshaft.pieces;

import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.MaybeNull;
import com.github.mdcdi1315.mdex.structures.customizablemineshaft.CustomizableMineshaftPiecesSettings;

import net.minecraft.world.level.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;

@SuppressWarnings("all")
public final class MineShaftStairs
        extends AbstractMineshaftPiece
{
    public MineShaftStairs(int genDepth, BoundingBox boundingBox, Direction orientation , CustomizableMineshaftPiecesSettings s) {
        super(s, MineShaftStairsType.INSTANCE, genDepth, boundingBox);
        this.setOrientation(orientation);
    }

    public MineShaftStairs(CompoundTag tag) {
            super(MineShaftStairsType.INSTANCE, tag);
        }

    @MaybeNull
    public static BoundingBox findStairs(StructurePieceAccessor pieces, RandomSource random, int x, int y, int z, Direction direction) {
        BoundingBox boundingbox = switch (direction) {
            case SOUTH -> new BoundingBox(0, -5, 0, 2, 2, 8);
            case WEST -> new BoundingBox(-8, -5, 0, 0, 2, 2);
            case EAST -> new BoundingBox(0, -5, 0, 8, 2, 2);
            default -> new BoundingBox(0, -5, -8, 2, 2, 0);
        };

        boundingbox = boundingbox.moved(x , y , z);

        return pieces.findCollisionPiece(boundingbox) != null ? null : boundingbox;
    }

    public void addChildren(StructurePiece piece, StructurePieceAccessor pieces, RandomSource random) {
        int i = this.getGenDepth();
        Direction direction = this.getOrientation();
        if (direction != null) {
            switch (direction) {
                case SOUTH:
                    MineshaftPieces.GenerateAndAddPiece(piece, pieces, random, this.boundingBox.minX(), this.boundingBox.minY(), this.boundingBox.maxZ() + 1, Direction.SOUTH, i);
                    break;
                case WEST:
                    MineshaftPieces.GenerateAndAddPiece(piece, pieces, random, this.boundingBox.minX() - 1, this.boundingBox.minY(), this.boundingBox.minZ(), Direction.WEST, i);
                    break;
                case EAST:
                    MineshaftPieces.GenerateAndAddPiece(piece, pieces, random, this.boundingBox.maxX() + 1, this.boundingBox.minY(), this.boundingBox.minZ(), Direction.EAST, i);
                    break;
                case NORTH:
                default:
                    MineshaftPieces.GenerateAndAddPiece(piece, pieces, random, this.boundingBox.minX(), this.boundingBox.minY(), this.boundingBox.minZ() - 1, Direction.NORTH, i);
                    break;
            }
        }

    }

    public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox box, ChunkPos chunkPos, BlockPos pos) {
        if (!IsInInvalidLocation(level, box)) {
            this.generateBox(level, box, 0, 5, 0, 2, 7, 1, CAVE_AIR, CAVE_AIR, false);
            this.generateBox(level, box, 0, 0, 7, 2, 2, 8, CAVE_AIR, CAVE_AIR, false);

            for (byte i = 0; i < 5; ++i) {
                this.generateBox(level, box, 0, 5 - i - (i < 4 ? 1 : 0), 2 + i, 2, 7 - i, 2 + i, CAVE_AIR, CAVE_AIR, false);
            }
        }
    }
}

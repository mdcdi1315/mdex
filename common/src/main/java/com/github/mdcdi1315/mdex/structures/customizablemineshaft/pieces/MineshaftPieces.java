package com.github.mdcdi1315.mdex.structures.customizablemineshaft.pieces;

import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.MaybeNull;

import com.github.mdcdi1315.mdex.structures.customizablemineshaft.CustomizableMineshaftPiecesSettings;

import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;

public final class MineshaftPieces
{
    private MineshaftPieces() {}

    public static final int DEFAULT_SHAFT_WIDTH = 3;
    public static final int DEFAULT_SHAFT_HEIGHT = 3;
    public static final int DEFAULT_SHAFT_LENGTH = 5;
    public static final int MAX_PILLAR_HEIGHT = 20;
    public static final int MAX_CHAIN_HEIGHT = 50;
    public static final int MAX_DEPTH = 8;
    public static final int MAGIC_START_Y = 50;

    public static AbstractMineshaftPiece CreateRandomShaftPiece(
            StructurePieceAccessor pieces,
            RandomSource random,
            int x,
            int y,
            int z,
            @MaybeNull Direction orientation,
            int genDepth ,
            CustomizableMineshaftPiecesSettings settings
    ) {
        BoundingBox boundingbox;
        int i = random.nextInt(100);
        if (i > 79) {
            boundingbox = MineShaftCrossing.findCrossing(pieces, random, x, y, z, orientation);
            if (boundingbox != null) {
                return new MineShaftCrossing(genDepth, boundingbox, orientation, settings);
            }
        } else if (i > 69) {
            boundingbox = MineShaftStairs.findStairs(pieces, random, x, y, z, orientation);
            if (boundingbox != null) {
                return new MineShaftStairs(genDepth, boundingbox, orientation , settings);
            }
        } else {
            boundingbox = MineShaftCorridor.findCorridorSize(pieces, random, x, y, z, orientation);
            if (boundingbox != null) {
                return new MineShaftCorridor(genDepth, random, boundingbox, orientation, settings);
            }
        }

        return null;
    }

    public static AbstractMineshaftPiece GenerateAndAddPiece(
            StructurePiece piece,
            StructurePieceAccessor pieces,
            RandomSource random,
            int x,
            int y,
            int z,
            Direction direction,
            int genDepth
    ) {
        if (genDepth > MAX_DEPTH) {
            return null;
        } else if (Math.abs(x - piece.getBoundingBox().minX()) < 81 && Math.abs(z - piece.getBoundingBox().minZ()) < 81) {
            AbstractMineshaftPiece p = CreateRandomShaftPiece(pieces, random, x, y, z, direction, genDepth + 1, ((AbstractMineshaftPiece)piece).settings);
            if (p != null) {
                pieces.addPiece(p);
                p.addChildren(piece, pieces, random);
            }

            return p;
        } else {
            return null;
        }
    }
}

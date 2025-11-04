package com.github.mdcdi1315.mdex.structures;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;

public abstract class AbstractStructurePiece
    extends StructurePiece
{
    protected AbstractStructurePiece(AbstractStructurePieceType type, int genDepth, BoundingBox boundingBox) {
        super(type, genDepth, boundingBox);
    }

    public AbstractStructurePiece(AbstractStructurePieceType type, CompoundTag tag) {
        super(type, tag);
    }
}

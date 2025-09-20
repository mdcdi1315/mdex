package com.github.mdcdi1315.mdex.structures.customizablemineshaft.pieces;

import com.github.mdcdi1315.mdex.structures.AbstractStructurePieceType;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;

public final class MineShaftCorridorType
    extends AbstractStructurePieceType
{
    public static final MineShaftCorridorType INSTANCE = new MineShaftCorridorType();

    private MineShaftCorridorType() {
        super("mineshaft_corridor");
    }

    @Override
    protected StructurePiece LoadImpl(StructurePieceSerializationContext sc, CompoundTag ct) {
        return new MineShaftCorridor(ct);
    }
}

package com.github.mdcdi1315.mdex.structures.customizablemineshaft.pieces;

import com.github.mdcdi1315.mdex.structures.AbstractStructurePieceType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;

public final class MineShaftStairsType
    extends AbstractStructurePieceType
{
    public static final MineShaftStairsType INSTANCE = new MineShaftStairsType();

    private MineShaftStairsType()
    {
        super("mineshaft_stairs");
    }

    @Override
    protected StructurePiece LoadImpl(StructurePieceSerializationContext sc, CompoundTag ct) {
        return new MineShaftStairs(ct);
    }
}

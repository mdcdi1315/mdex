package com.github.mdcdi1315.mdex.structures;

import com.github.mdcdi1315.DotNetLayer.System.ArgumentNullException;
import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.MaybeNull;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;

public abstract class AbstractStructurePieceType
    implements StructurePieceType
{
    private String name;

    public AbstractStructurePieceType(String name)
    {
        ArgumentNullException.ThrowIfNull(name , "name");
        this.name = name;
    }

    protected abstract StructurePiece LoadImpl(StructurePieceSerializationContext sc, CompoundTag ct);

    @Override
    public final StructurePiece load(StructurePieceSerializationContext sc, CompoundTag ct) {
        return LoadImpl(sc , ct);
    }

    @MaybeNull
    public String GetNameAndClear() {
        String s = name;
        name = null;
        return s;
    }
}

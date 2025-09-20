package com.github.mdcdi1315.mdex.structures.customizablemineshaft;

import com.github.mdcdi1315.mdex.structures.AbstractStructure;
import com.github.mdcdi1315.mdex.structures.AbstractStructureType;
import com.github.mdcdi1315.mdex.structures.customizablemineshaft.pieces.MineShaftRoom;

import com.mojang.datafixers.util.Either;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

import java.util.List;
import java.util.Optional;

public final class CustomizableMineshaftStructure
        extends AbstractStructure
{
    public CustomizableMineshaftStructureSettings Settings;

    public CustomizableMineshaftStructure(
            List<String> modids ,
            StructureSettings settings,
            CustomizableMineshaftStructureSettings mineshaftsettings
    ) {
        super(modids , settings);
        Settings = mineshaftsettings;
        Compile();
    }

    @Override
    protected void CompileInstanceData() {
        Settings.Compile();
        if (!Settings.IsCompiled()) {
            SetInstanceAsInvalid();
        }
    }

    public Optional<Structure.GenerationStub> FindGenerationPoint(GenerationContext context) {
        context.random().nextDouble();
        ChunkPos chunkpos = context.chunkPos();
        BlockPos blockpos = new BlockPos(chunkpos.getMiddleBlockX(), 50, chunkpos.getMinBlockZ());
        StructurePiecesBuilder structurepiecesbuilder = new StructurePiecesBuilder();
        int i = this.generatePiecesAndAdjust(structurepiecesbuilder, context);
        return Optional.of(new Structure.GenerationStub(blockpos.offset(0, i, 0), Either.right(structurepiecesbuilder)));
    }

    private static void offsetPiecesVertically(StructurePiecesBuilder b, int offset)
    {
        for (StructurePiece structurepiece : b.build().pieces()) {
            structurepiece.move(0, offset, 0);
        }
    }

    private int moveBelowSeaLevel(StructurePiecesBuilder b, int seaLevel, int minY, RandomSource random, int p_226969_) {
        int i = seaLevel - p_226969_;
        BoundingBox boundingbox = b.getBoundingBox();
        int j = boundingbox.getYSpan() + minY + 1;
        if (j < i) {
            j += random.nextInt(i - j);
        }

        int k = j - boundingbox.maxY();
        offsetPiecesVertically(b, k);
        return k;
    }


    private int generatePiecesAndAdjust(StructurePiecesBuilder builder, GenerationContext context)
    {
        ChunkPos chunkpos = context.chunkPos();
        WorldgenRandom worldgenrandom = context.random();
        ChunkGenerator chunkgenerator = context.chunkGenerator();
        MineShaftRoom mineshaftpieces$mineshaftroom = new MineShaftRoom(0, worldgenrandom, chunkpos.getBlockX(2), chunkpos.getBlockZ(2), Settings);
        builder.addPiece(mineshaftpieces$mineshaftroom);
        mineshaftpieces$mineshaftroom.addChildren(mineshaftpieces$mineshaftroom, builder, worldgenrandom);
        return moveBelowSeaLevel(builder, chunkgenerator.getSeaLevel(), chunkgenerator.getMinY(), worldgenrandom, 10);
    }

    @Override
    protected AbstractStructureType<?> GetStructureType() {
        return CustomizableMineshaftStructureType.INSTANCE;
    }
}

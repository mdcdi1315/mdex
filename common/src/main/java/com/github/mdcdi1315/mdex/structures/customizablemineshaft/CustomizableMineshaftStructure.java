package com.github.mdcdi1315.mdex.structures.customizablemineshaft;

import com.github.mdcdi1315.mdex.util.Extensions;
import com.github.mdcdi1315.mdex.structures.AbstractStructure;
import com.github.mdcdi1315.mdex.structures.AbstractStructureType;
import com.github.mdcdi1315.mdex.structures.customizablemineshaft.pieces.MineShaftRoom;

import com.mojang.datafixers.util.Either;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
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

    public Optional<Structure.GenerationStub> FindGenerationPoint(GenerationContext context)
    {
        var random = context.random();
        // About the older technique context.random().nextDouble();
        // Possibly it was perpetually used to initialize the random number generator appropriately
        // I am doing the same below and instead of generating a random double I generate 10 int values.
        // Also see below, I am using the random source to roll a random value from the height provider
        Extensions.InitializeRandomSource(random);
        ChunkPos chunkpos = context.chunkPos();
        // Selected generation height.
        // The structure will finally select a value below than this height
        int sh = Settings.Starting_Y_Level.sample(random , new WorldGenerationContext(context.chunkGenerator(), context.heightAccessor()));
        BlockPos blockpos = new BlockPos(chunkpos.getMiddleBlockX(), sh, chunkpos.getMinBlockZ());
        StructurePiecesBuilder structurepiecesbuilder = new StructurePiecesBuilder();
        int i = this.generatePiecesAndAdjust(structurepiecesbuilder, sh , context);
        return Optional.of(new Structure.GenerationStub(blockpos.offset(0, i, 0), Either.right(structurepiecesbuilder)));
    }

    private static int ShiftPiecesBelowLevel(StructurePiecesBuilder b, int desiredheightlevel, int minY, RandomSource random)
    {
        int i = desiredheightlevel - 10;
        BoundingBox boundingbox = b.getBoundingBox();
        int j = boundingbox.getYSpan() + minY + 1;
        if (j < i) {
            j += random.nextInt(i - j);
        }

        int offset = j - boundingbox.maxY();
        for (var sp : b.build().pieces()) {
            sp.move(0, offset, 0);
        }
        return offset;
    }

    private int generatePiecesAndAdjust(StructurePiecesBuilder builder , int start_y, GenerationContext context)
    {
        ChunkPos chunkpos = context.chunkPos();
        RandomSource random = context.random();
        ChunkGenerator chunkgenerator = context.chunkGenerator();
        MineShaftRoom room = new MineShaftRoom(0, random, chunkpos.getBlockX(2), start_y, chunkpos.getBlockZ(2), Settings.CreateSettingsObject());
        builder.addPiece(room);
        room.addChildren(room, builder, random);
        // Make min_y instead + 10 to avoid unfaithful issues occurring with the mineshafts spawning in lava levels and makes the planks to be burnt
        return ShiftPiecesBelowLevel(builder, start_y, chunkgenerator.getMinY() + 10, random);
    }

    @Override
    protected AbstractStructureType<?> GetStructureType() {
        return CustomizableMineshaftStructureType.INSTANCE;
    }
}

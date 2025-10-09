package com.github.mdcdi1315.mdex.structures.customizablemineshaft.pieces;

import com.github.mdcdi1315.mdex.MDEXBalmLayer;
import com.github.mdcdi1315.mdex.structures.customizablemineshaft.CustomizableMineshaftPiecesSettings;

import com.google.common.collect.Lists;

import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;

import java.util.List;

@SuppressWarnings("all")
public final class MineShaftRoom
        extends AbstractMineshaftPiece
{
    private final List<BoundingBox> childEntranceBoxes;

    public MineShaftRoom(int genDepth, RandomSource random, int x, int y, int z, CustomizableMineshaftPiecesSettings s) {
        super(s, MineShaftRoomType.INSTANCE, genDepth, new BoundingBox(x, y, z, x + 7 + random.nextInt(6),  y + 5 + random.nextInt(6), z + 7 + random.nextInt(6)));
        childEntranceBoxes = Lists.newLinkedList();
    }

    public MineShaftRoom(CompoundTag tag) {
        super(MineShaftRoomType.INSTANCE, tag);
        childEntranceBoxes = Lists.newLinkedList();
        BoundingBox.CODEC.listOf()
                .parse(NbtOps.INSTANCE, tag.getList("Entrances", Tag.TAG_INT_ARRAY))
                .resultOrPartial(MDEXBalmLayer.LOGGER::error).ifPresent(this.childEntranceBoxes::addAll);
    }

    public void addChildren(StructurePiece piece, StructurePieceAccessor pieces, RandomSource random)
    {
        int i = this.getGenDepth();
        int j = this.boundingBox.getYSpan() - 3 - 1;
        int k;
        if (j < 1) {
            j = 1;
        }

        StructurePiece p;
        BoundingBox boundingbox;

        for (k = 0; k < this.boundingBox.getXSpan(); k += 4) {
            k += random.nextInt(this.boundingBox.getXSpan());
            if (k + 3 > this.boundingBox.getXSpan()) {
                break;
            }

            p = MineshaftPieces.GenerateAndAddPiece(piece, pieces, random, this.boundingBox.minX() + k, this.boundingBox.minY() + random.nextInt(j) + 1, this.boundingBox.minZ() - 1, Direction.NORTH, i);
            if (p != null) {
                boundingbox = p.getBoundingBox();
                this.childEntranceBoxes.add(new BoundingBox(boundingbox.minX(), boundingbox.minY(), this.boundingBox.minZ(), boundingbox.maxX(), boundingbox.maxY(), this.boundingBox.minZ() + 1));
            }
        }

        for (k = 0; k < this.boundingBox.getXSpan(); k += 4) {
            k += random.nextInt(this.boundingBox.getXSpan());
            if (k + 3 > this.boundingBox.getXSpan()) {
                break;
            }

            p = MineshaftPieces.GenerateAndAddPiece(piece, pieces, random, this.boundingBox.minX() + k, this.boundingBox.minY() + random.nextInt(j) + 1, this.boundingBox.maxZ() + 1, Direction.SOUTH, i);
            if (p != null) {
                boundingbox = p.getBoundingBox();
                this.childEntranceBoxes.add(new BoundingBox(boundingbox.minX(), boundingbox.minY(), this.boundingBox.maxZ() - 1, boundingbox.maxX(), boundingbox.maxY(), this.boundingBox.maxZ()));
            }
        }

        for (k = 0; k < this.boundingBox.getZSpan(); k += 4)
        {
            k += random.nextInt(this.boundingBox.getZSpan());
            if (k + 3 > this.boundingBox.getZSpan()) {
                break;
            }

            p = MineshaftPieces.GenerateAndAddPiece(piece, pieces, random, this.boundingBox.minX() - 1, this.boundingBox.minY() + random.nextInt(j) + 1, this.boundingBox.minZ() + k, Direction.WEST, i);
            if (p != null) {
                boundingbox = p.getBoundingBox();
                this.childEntranceBoxes.add(new BoundingBox(this.boundingBox.minX(), boundingbox.minY(), boundingbox.minZ(), this.boundingBox.minX() + 1, boundingbox.maxY(), boundingbox.maxZ()));
            }
        }

        for (k = 0; k < this.boundingBox.getZSpan(); k += 4)
        {
            k += random.nextInt(this.boundingBox.getZSpan());
            if (k + 3 > this.boundingBox.getZSpan()) {
                break;
            }

            p = MineshaftPieces.GenerateAndAddPiece(piece, pieces, random, this.boundingBox.maxX() + 1, this.boundingBox.minY() + random.nextInt(j) + 1, this.boundingBox.minZ() + k, Direction.EAST, i);
            if (p != null) {
                boundingbox = p.getBoundingBox();
                this.childEntranceBoxes.add(new BoundingBox(this.boundingBox.maxX() - 1, boundingbox.minY(), boundingbox.minZ(), this.boundingBox.maxX(), boundingbox.maxY(), boundingbox.maxZ()));
            }
        }

    }

    public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox box, ChunkPos chunkPos, BlockPos pos) {
        if (!IsInInvalidLocation(level, box)) {
            this.generateBox(level, box, this.boundingBox.minX(), this.boundingBox.minY() + 1, this.boundingBox.minZ(), this.boundingBox.maxX(), Math.min(this.boundingBox.minY() + 3, this.boundingBox.maxY()), this.boundingBox.maxZ(), CAVE_AIR, CAVE_AIR, false);

            for(BoundingBox boundingbox : this.childEntranceBoxes) {
                this.generateBox(level, box, boundingbox.minX(), boundingbox.maxY() - 2, boundingbox.minZ(), boundingbox.maxX(), boundingbox.maxY(), boundingbox.maxZ(), CAVE_AIR, CAVE_AIR, false);
            }

            this.generateUpperHalfSphere(level, box, this.boundingBox.minX(), this.boundingBox.minY() + 4, this.boundingBox.minZ(), this.boundingBox.maxX(), this.boundingBox.maxY(), this.boundingBox.maxZ(), CAVE_AIR, false);
        }

    }

    public void move(int x, int y, int z) {
        super.move(x, y, z);

        this.childEntranceBoxes.replaceAll(box -> box.moved(x, y, z));
    }

    protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag)
    {
        super.addAdditionalSaveData(context, tag);
        BoundingBox.CODEC.listOf()
                .encodeStart(NbtOps.INSTANCE, this.childEntranceBoxes)
                .resultOrPartial(MDEXBalmLayer.LOGGER::error).ifPresent((p) -> tag.put("Entrances", p));
    }
}
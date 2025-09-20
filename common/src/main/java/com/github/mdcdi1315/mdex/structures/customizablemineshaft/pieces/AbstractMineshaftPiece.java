package com.github.mdcdi1315.mdex.structures.customizablemineshaft.pieces;

import com.github.mdcdi1315.mdex.block.BlockUtils;
import com.github.mdcdi1315.mdex.util.MDEXException;
import com.github.mdcdi1315.mdex.structures.AbstractStructurePiece;
import com.github.mdcdi1315.mdex.structures.AbstractStructurePieceType;
import com.github.mdcdi1315.mdex.structures.customizablemineshaft.CustomizableMineshaftStructureSettings;

import com.mojang.serialization.DataResult;

import net.minecraft.nbt.Tag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BiomeTags;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;

public abstract class AbstractMineshaftPiece
    extends AbstractStructurePiece
{
    protected final CustomizableMineshaftStructureSettings settings;

    protected AbstractMineshaftPiece(CustomizableMineshaftStructureSettings s, AbstractStructurePieceType type, int genDepth, BoundingBox boundingBox) {
        super(type, genDepth, boundingBox);
        settings = s;
    }

    public AbstractMineshaftPiece(AbstractStructurePieceType type, CompoundTag tag) {
        super(type, tag);
        if (tag.contains("SETTINGS" , Tag.TAG_COMPOUND)) {
            settings = CustomizableMineshaftStructureSettings.GetCodec().codec().decode(NbtOps.INSTANCE , tag.getCompound("SETTINGS")).getOrThrow(false , (String s) -> {}).getFirst();
            settings.Compile();
            if (!settings.IsCompiled()) {
                throw new MDEXException("Cannot re-compile back the given structure settings! Possibly a data corruption?");
            }
        } else {
            settings = null;
        }
    }

    protected boolean canBeReplaced(LevelReader level, int x, int y, int z, BoundingBox box) {
        BlockState blockstate = this.getBlock(level, x, y, z, box);
        return !blockstate.is(settings.PlanksState.BlockState.getBlock()) && !blockstate.is(settings.WoodState.BlockState.getBlock()) && !blockstate.is(settings.FenceState.BlockState.getBlock()) && !blockstate.is(Blocks.CHAIN);
    }

    protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag)
    {
        DataResult<Tag> t = CustomizableMineshaftStructureSettings.GetCodec().encode(settings , NbtOps.INSTANCE , NbtOps.INSTANCE.mapBuilder()).build(tag);
        try {
            tag.put("SETTINGS", t.getOrThrow(false, (String s) -> {
            }));
        } catch (Exception e) {
            throw new MDEXException("Internal error occured while trying to save mineshaft data: " + e.getMessage());
        }
    }

    protected boolean IsSupportingBox(BlockGetter level, BoundingBox box, int xStart, int xEnd, int y, int z)
    {
        for (int i = xStart; i <= xEnd; ++i) {
            if (this.getBlock(level, i, y + 1, z, box).isAir()) {
                return false;
            }
        }

        return true;
    }

    protected boolean IsInInvalidLocation(LevelAccessor level, BoundingBox boundingBox)
    {
        int i = Math.max(this.boundingBox.minX() - 1, boundingBox.minX());
        int j = Math.max(this.boundingBox.minY() - 1, boundingBox.minY());
        int k = Math.max(this.boundingBox.minZ() - 1, boundingBox.minZ());
        int l = Math.min(this.boundingBox.maxX() + 1, boundingBox.maxX());
        int i1 = Math.min(this.boundingBox.maxY() + 1, boundingBox.maxY());
        int j1 = Math.min(this.boundingBox.maxZ() + 1, boundingBox.maxZ());
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos((i + l) / 2, (j + i1) / 2, (k + j1) / 2);
        if (level.getBiome(blockpos$mutableblockpos).is(BiomeTags.MINESHAFT_BLOCKING)) {
            return true;
        } else {
            for (int k1 = i; k1 <= l; ++k1)
            {
                for (int l1 = k; l1 <= j1; ++l1)
                {
                    if (BlockUtils.HasAnyFluid(level , blockpos$mutableblockpos.set(k1, j, l1))) { return true; }

                    if (BlockUtils.HasAnyFluid(level , blockpos$mutableblockpos.set(k1, i1, l1))) { return true; }
                }
            }

            for (int i2 = i; i2 <= l; ++i2)
            {
                for (int k2 = j; k2 <= i1; ++k2)
                {
                    if (BlockUtils.HasAnyFluid(level , blockpos$mutableblockpos.set(i2, k2, k))) { return true; }

                    if (BlockUtils.HasAnyFluid(level, blockpos$mutableblockpos.set(i2, k2, j1))) { return true; }
                }
            }

            for (int j2 = k; j2 <= j1; ++j2)
            {
                for (int l2 = j; l2 <= i1; ++l2)
                {
                    if (BlockUtils.HasAnyFluid(level, blockpos$mutableblockpos.set(i, l2, j2))) { return true; }

                    if (BlockUtils.HasAnyFluid(level , blockpos$mutableblockpos.set(l, l2, j2))) { return true; }
                }
            }

            return false;
        }
    }

    protected final void SetPlanksBlock(WorldGenLevel level, BoundingBox box, int x, int y, int z)
    {
        if (this.isInterior(level, x, y, z, box))
        {
            BlockPos blockpos = this.getWorldPos(x, y, z);
            BlockState blockstate = level.getBlockState(blockpos);
            if (!blockstate.isFaceSturdy(level, blockpos, Direction.UP)) {
                level.setBlock(blockpos, settings.PlanksState.BlockState, 2);
            }
        }
    }
}

package com.github.mdcdi1315.mdex.api.areafinding;

import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.MaybeNull;
import com.github.mdcdi1315.DotNetLayer.System.Predicate;
import com.github.mdcdi1315.DotNetLayer.System.ArgumentNullException;

import com.github.mdcdi1315.mdex.util.SectionAreaEnumerator;

import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunkSection;

public final class ChunkAreaFinder
{
    private final ChunkAccess access;

    public ChunkAreaFinder(ChunkAccess access)
    {
        ArgumentNullException.ThrowIfNull(access, "access");
        this.access = access;
    }

    private static final class FindAreaCxtImpl
        implements FindAreaContext
    {
        private final ChunkAccess access;

        private SectionPos cursor_pos;
        private BlockState cursor_block_state;

        private int current_section_index;
        private LevelChunkSection current_section;

        public FindAreaCxtImpl(ChunkAccess access)
        {
            this.access = access;
            current_section_index = this.access.getSectionIndexFromSectionY(this.access.getMinSectionY());
            current_section = this.access.getSection(current_section_index);
        }

        public void UpdateCursor(SectionPos pos)
        {
            int y = (cursor_pos = pos).getY(), index = access.getSectionIndex(y);
            if (current_section_index != index) {
                current_section = access.getSection(current_section_index = index);
            }
            cursor_block_state = current_section.getBlockState(pos.getX(), y & SectionPos.SECTION_MASK, pos.getZ());
        }

        @Override
        public SectionPos GetCursorPosition() { return cursor_pos; }

        @Override
        public BlockState GetCursorBlockState() { return cursor_block_state; }

        @Override
        @MaybeNull
        public BlockState GetOtherBlockState(SectionPos pos)
        {
            int y = pos.getY();
            if (y > access.getMaxY() || y < access.getMinY()) {
                return null;
            } else {
                int index = access.getSectionIndex(y);
                return ((current_section_index == index) ? current_section : access.getSection(index)).getBlockState(pos.getX() & SectionPos.SECTION_MASK, y & SectionPos.SECTION_MASK, pos.getZ() & SectionPos.SECTION_MASK);
            }
        }
    }

    public BlockPos FindArea(Predicate<FindAreaContext> predicate)
    {
        FindAreaCxtImpl data = new FindAreaCxtImpl(access);
        for (SectionPos sp : new SectionAreaEnumerator(access))
        {
            data.UpdateCursor(sp);
            if (predicate.predicate(data)) {
                return SectionAreaEnumerator.ToDevirtualizedPosition(sp, access.getPos());
            }
        }
        return null;
    }

    public BlockPos FindArea(Predicate<FindAreaContext> predicate, SectionPos start_pos)
    {
        FindAreaCxtImpl data = new FindAreaCxtImpl(access);
        for (SectionPos sp : new SectionAreaEnumerator(access, start_pos))
        {
            data.UpdateCursor(sp);
            if (predicate.predicate(data)) {
                return SectionAreaEnumerator.ToDevirtualizedPosition(sp, access.getPos());
            }
        }
        return null;
    }

    public BlockPos FindArea(Predicate<FindAreaContext> predicate, BlockPos devirtualized_start_pos)
    {
        FindAreaCxtImpl data = new FindAreaCxtImpl(access);
        for (SectionPos sp : new SectionAreaEnumerator(access, SectionAreaEnumerator.ToVirtualizedPosition(devirtualized_start_pos, access.getPos())))
        {
            data.UpdateCursor(sp);
            if (predicate.predicate(data)) {
                return SectionAreaEnumerator.ToDevirtualizedPosition(sp, access.getPos());
            }
        }
        return null;
    }
}

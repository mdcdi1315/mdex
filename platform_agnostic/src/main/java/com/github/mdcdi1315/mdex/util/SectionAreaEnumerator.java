package com.github.mdcdi1315.mdex.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkAccess;

import java.util.Iterator;

public final class SectionAreaEnumerator
    implements Iterator<SectionPos>, Iterable<SectionPos>
{
    private final int width, height;
    private int x, y, z;

    public SectionAreaEnumerator(int width, int height)
    {
        this.width = width;
        this.height = height;
        x = 0;
        y = 0;
        z = -1;
    }

    public SectionAreaEnumerator(ChunkAccess chunk)
    {
        this.width = SectionPos.SECTION_SIZE;
        int min_height = chunk.getMinY();
        this.height = chunk.getMaxY() + min_height;
        x = 0;
        y = min_height; // May be negative, but we are not interested in that.
        z = -1; // Of our importance is the z value to be -1 so that in the first iteration becomes 0.
    }

    public SectionAreaEnumerator(ChunkAccess chunk, SectionPos start_pos)
    {
        this.width = SectionPos.SECTION_SIZE;
        int min_height = chunk.getMinY();
        this.height = chunk.getMaxY() + min_height;
        x = start_pos.getX();
        y = start_pos.getY();
        if (y < min_height) { y = min_height; }
        z = start_pos.getZ() - 1;
    }

    public SectionAreaEnumerator() { this(SectionPos.SECTION_SIZE, SectionPos.SECTION_SIZE); }

    public boolean hasNext()
    {
        if (++z < width) {
            return true;
        } else if (++x < width) {
            z = 0;
            return true;
        } else if (++y < height) {
            z = 0;
            x = 0;
            return true;
        } else {
            return false;
        }
    }

    public SectionPos next() { return SectionPos.of(x, y, z); }

    @Override
    public Iterator<SectionPos> iterator() { return this; }

    public static BlockPos ToDevirtualizedPosition(SectionPos pos, ChunkPos chunk)
    {
        return new BlockPos(
                chunk.getMinBlockX() + pos.getX(),
                 pos.getY(),
                chunk.getMinBlockZ() + pos.getZ()
        );
    }

    public static SectionPos ToVirtualizedPosition(BlockPos pos, ChunkPos chunk)
    {
        return SectionPos.of(
                pos.getX() - chunk.getMinBlockX(),
                pos.getY(),
                pos.getZ() - chunk.getMinBlockZ()
        );
    }
}

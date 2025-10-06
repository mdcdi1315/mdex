package com.github.mdcdi1315.mdex.util;

import net.minecraft.core.BlockPos;

import java.util.Iterator;

final class RectAreaEnumerator
        implements Iterator<BlockPos>
{
    private final BlockPos start;
    private final int mx , my , mz;
    private int cx, cy, cz;

    public RectAreaEnumerator(BlockPos start, BlockPos offset)
    {
        this.start = start;
        mx = offset.getX();
        my = offset.getY();
        mz = offset.getZ();
        cx = 0;
        cy = 0;
        // This happens due to the fact that the first block must always be the start point, and because
        // hasNext will be called, this will be ++'ed so it will become 0 , 0 , 0 (As it is expected).
        cz = -1;
    }

    @Override
    public boolean hasNext()
    {
        if (++cz < mz) {
            return true;
        } else if (++cx < mx) {
            cz = 0;
            return true;
        } else if (++cy < my) {
            cz = 0;
            cx = 0;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public BlockPos next() {
        return start.offset(cx , cy , cz);
    }
}
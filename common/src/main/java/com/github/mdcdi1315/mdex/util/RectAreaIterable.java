package com.github.mdcdi1315.mdex.util;

import com.github.mdcdi1315.DotNetLayer.System.ArgumentNullException;
import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public final class RectAreaIterable
        implements Iterable<BlockPos>
{
    private final BlockPos start , offset;

    public RectAreaIterable(BlockPos start, BlockPos offset)
            throws ArgumentNullException
    {
        ArgumentNullException.ThrowIfNull(start);
        ArgumentNullException.ThrowIfNull(offset);
        this.start = start;
        this.offset = offset;
    }

    @Override
    public @NotNull Iterator<BlockPos> iterator()
    {
        return new RectAreaEnumerator(start , offset);
    }
}


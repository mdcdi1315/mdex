package com.github.mdcdi1315.mdex.util;

import com.github.mdcdi1315.DotNetLayer.System.Runtime.CompilerServices.Extension;

import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;

@Extension
public final class DirectionExtensions
{
    private DirectionExtensions() {}

    @Extension
    public static Direction GetRandomDirectionExcludingUpDown(RandomSource rs)
    {
        Direction ret;
        do {
            ret = Direction.getRandom(rs);
        } while (ret == Direction.UP || ret == Direction.DOWN);
        return ret;
    }
}

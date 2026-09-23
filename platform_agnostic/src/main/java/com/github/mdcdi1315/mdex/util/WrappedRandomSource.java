package com.github.mdcdi1315.mdex.util;

import com.github.mdcdi1315.DotNetLayer.System.ArgumentNullException;

import com.github.mdcdi1315.basemodslib.utils.random.IRandomSource;

import net.minecraft.util.RandomSource;

public final class WrappedRandomSource
    implements IRandomSource
{
    private final RandomSource random;

    public WrappedRandomSource(RandomSource random)
            throws ArgumentNullException
    {
        ArgumentNullException.ThrowIfNull(random, "random");
        this.random = random;
    }

    @Override
    public long NextLong() { return random.nextLong(); }

    public RandomSource GetOriginal() { return this.random; }

    @Override
    public long GetSeed() { return 0L; }
}

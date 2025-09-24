package com.github.mdcdi1315.mdex.block.blockstateproviders;

import com.github.mdcdi1315.DotNetLayer.System.ArgumentNullException;
import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.NotNull;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;

import java.util.Iterator;

public final class BlockStatesIterable<TPos extends BlockPos>
    implements Iterable<BlockStateIterationResult<TPos>>
{
    private AbstractBlockStateProvider provider;
    private BlockGetter getter;
    private RandomSource source;
    private Iterator<TPos> iterator;

    public BlockStatesIterable(AbstractBlockStateProvider thisprovider , BlockGetter getter , RandomSource source , Iterator<TPos> iterator)
            throws ArgumentNullException
    {
        ArgumentNullException.ThrowIfNull(thisprovider , "thisprovider");
        ArgumentNullException.ThrowIfNull(getter , "getter");
        ArgumentNullException.ThrowIfNull(source , "source");
        ArgumentNullException.ThrowIfNull(iterator , "iterator");
        provider = thisprovider;
        this.getter = getter;
        this.source = source;
        this.iterator = iterator;
    }

    @Override
    public @NotNull Iterator<BlockStateIterationResult<TPos>> iterator() {
        return new BlockStatesIterator<>(provider, getter , source , iterator);
    }
}

package com.github.mdcdi1315.mdex.block.blockstateproviders;

import com.github.mdcdi1315.DotNetLayer.System.ArgumentNullException;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;

import java.util.Iterator;

public final class BlockStatesIterator<TPos extends BlockPos>
    implements Iterator<BlockStateIterationResult<TPos>>
{
    private BlockGetter getter;
    private RandomSource source;
    private Iterator<TPos> iterator;
    private AbstractBlockStateProvider provider;

    public BlockStatesIterator(
            AbstractBlockStateProvider provider,
            BlockGetter getter,
            RandomSource source,
            Iterator<TPos> iterator
    ) throws ArgumentNullException
    {
        ArgumentNullException.ThrowIfNull(provider);
        ArgumentNullException.ThrowIfNull(getter);
        ArgumentNullException.ThrowIfNull(source);
        ArgumentNullException.ThrowIfNull(iterator);
        this.provider = provider;
        this.iterator = iterator;
        this.getter = getter;
        this.source = source;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public BlockStateIterationResult<TPos> next() {
        TPos p = iterator.next();
        return new BlockStateIterationResult<>(provider.GetBlockState(new BlockStateProviderContext(getter , source , p)) , p);
    }
}

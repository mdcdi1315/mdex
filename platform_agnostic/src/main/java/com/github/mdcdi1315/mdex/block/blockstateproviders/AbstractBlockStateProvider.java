package com.github.mdcdi1315.mdex.block.blockstateproviders;

import com.github.mdcdi1315.DotNetLayer.System.ArgumentNullException;
import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.MaybeNull;

import com.github.mdcdi1315.basemodslib.codecs.DelayLoadedCodec;
import com.github.mdcdi1315.mdex.dco_logic.Compilable;

import net.minecraft.core.BlockPos;
import com.mojang.serialization.Codec;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Iterator;

public abstract class AbstractBlockStateProvider
    implements Compilable
{
    private boolean compiled;

    public static Codec<AbstractBlockStateProvider> CODEC = new DelayLoadedCodec<>(CustomBlockStateProviderRegistrySubsystem::CodecGetter).dispatch(AbstractBlockStateProvider::GetType , AbstractBlockStateProviderType::Codec);;

    public AbstractBlockStateProvider()
    {
        compiled = false;
    }

    public abstract BlockState GetBlockState(BlockStateProviderContext context);

    public final BlockState GetBlockState(RandomSource source , BlockPos position) {
        return GetBlockState(new BlockStateProviderContext(null , source , position));
    }

    public final BlockState GetBlockState(@MaybeNull BlockGetter getter , RandomSource source , BlockPos position) {
        return GetBlockState(new BlockStateProviderContext(getter , source , position));
    }

    public final <TPos extends BlockPos> Iterable<BlockStateIterationResult<TPos>> GetBlockStates(BlockGetter getter, RandomSource source, Iterable<TPos> iterable)
            throws ArgumentNullException
    {
        ArgumentNullException.ThrowIfNull(iterable , "iterable");
        return new BlockStatesIterable<>(this , getter , source , iterable.iterator());
    }

    public final <TPos extends BlockPos> Iterable<BlockStateIterationResult<TPos>> GetBlockStates(BlockGetter getter, RandomSource source, Iterator<TPos> iterator)
            throws ArgumentNullException
    {
        return new BlockStatesIterable<>(this , getter , source , iterator);
    }

    public abstract AbstractBlockStateProviderType<?> GetType();

    /**
     * Should be overridden by extending block state providers to provide compilation data.
     */
    protected abstract boolean CompileImplementation();

    public final void Compile()
    {
        try {
            compiled = CompileImplementation();
        } catch (Exception e) {
            compiled = false;
            throw e;
        }
    }

    @Override
    public final boolean IsCompiled() {
        return compiled;
    }
}

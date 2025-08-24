package com.github.mdcdi1315.mdex.block.blockstateproviders;

import net.minecraft.core.BlockPos;
import com.mojang.serialization.Codec;
import net.minecraft.util.RandomSource;
import com.github.mdcdi1315.mdex.util.Compilable;
import net.minecraft.world.level.block.state.BlockState;

public abstract class AbstractBlockStateProvider
    implements Compilable
{
    /**
     * See the {@link CustomBlockStateProviderRegistrySubsystem} class for how this field is defined.
     */
    public static Codec<AbstractBlockStateProvider> CODEC;

    protected abstract AbstractBlockStateProviderType<?> type();

    public abstract BlockState getState(RandomSource rs, BlockPos pos);
}

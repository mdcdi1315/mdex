package com.github.mdcdi1315.mdex.block.blockstateproviders;

import com.mojang.serialization.MapCodec;
import net.minecraft.util.random.SimpleWeightedRandomList;
import com.github.mdcdi1315.mdex.util.CompilableBlockState;

public final class WeightedStateProviderType
    extends AbstractBlockStateProviderType<WeightedStateProvider>
{
    private final MapCodec<WeightedStateProvider> codec;

    public WeightedStateProviderType()
    {
        codec = SimpleWeightedRandomList.wrappedCodec(CompilableBlockState.GetCodec())
                .comapFlatMap(WeightedStateProvider::create, (p) -> p.weightedList)
                .fieldOf("entries");
    }

    @Override
    public MapCodec<WeightedStateProvider> Codec() {
        return codec;
    }
}

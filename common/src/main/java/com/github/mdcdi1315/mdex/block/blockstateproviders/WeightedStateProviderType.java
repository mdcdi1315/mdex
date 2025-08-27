package com.github.mdcdi1315.mdex.block.blockstateproviders;

import com.github.mdcdi1315.mdex.util.CompilableTargetBlockState;
import com.mojang.serialization.MapCodec;
import net.minecraft.util.random.SimpleWeightedRandomList;

public final class WeightedStateProviderType
    extends AbstractBlockStateProviderType<WeightedStateProvider>
{
    private final MapCodec<WeightedStateProvider> codec;

    public WeightedStateProviderType()
    {
        codec = SimpleWeightedRandomList.wrappedCodec(CompilableTargetBlockState.GetCodec())
                .comapFlatMap(WeightedStateProvider::create, (p) -> p.weightedList)
                .fieldOf("entries");
    }

    @Override
    public MapCodec<WeightedStateProvider> Codec() {
        return codec;
    }
}

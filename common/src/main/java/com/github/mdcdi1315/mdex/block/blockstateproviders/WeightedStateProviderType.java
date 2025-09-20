package com.github.mdcdi1315.mdex.block.blockstateproviders;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.DataResult;

import com.github.mdcdi1315.mdex.util.CompilableBlockState;
import com.github.mdcdi1315.mdex.util.weight.SimpleWeightedEntryList;

public final class WeightedStateProviderType
    extends AbstractBlockStateProviderType<WeightedStateProvider>
{
    private final MapCodec<WeightedStateProvider> codec;

    public WeightedStateProviderType()
    {
        codec = SimpleWeightedEntryList.CreateSimpleWeightedEntryList(CompilableBlockState.GetMapCodec())
                .fieldOf("entries").flatXmap(
                        WeightedStateProviderType::Create,
                        WeightedStateProviderType::Decompose
                );
    }

    private static DataResult<WeightedStateProvider> Create(SimpleWeightedEntryList<CompilableBlockState> list)
    {
        if (list == null) {
            return DataResult.error(() -> "Specified list is null.");
        }
        return DataResult.success(new WeightedStateProvider(list));
    }

    private static DataResult<SimpleWeightedEntryList<CompilableBlockState>> Decompose(WeightedStateProvider wsp)
    {
        if (wsp == null) {
            return DataResult.error(() -> "Specified weighted state provider is null.");
        }
        return DataResult.success(wsp.weightedList);
    }

    @Override
    public MapCodec<WeightedStateProvider> Codec() {
        return codec;
    }
}

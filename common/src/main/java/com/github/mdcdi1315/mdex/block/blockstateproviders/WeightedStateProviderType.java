package com.github.mdcdi1315.mdex.block.blockstateproviders;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.DataResult;

import com.github.mdcdi1315.mdex.util.CompilableTargetBlockState;
import com.github.mdcdi1315.mdex.util.weight.SimpleWeightedEntryList;

public final class WeightedStateProviderType
    extends AbstractBlockStateProviderType<WeightedStateProvider>
{
    private final MapCodec<WeightedStateProvider> codec;

    public WeightedStateProviderType()
    {
        codec = SimpleWeightedEntryList.CreateSimpleWeightedEntryList(CompilableTargetBlockState.GetCodec())
                .fieldOf("entries").flatXmap(
                        WeightedStateProviderType::Create,
                        WeightedStateProviderType::Decompose
                );
    }

    private static DataResult<WeightedStateProvider> Create(SimpleWeightedEntryList<CompilableTargetBlockState> list)
    {
        if (list == null) {
            return DataResult.error(() -> "Specified list is null.");
        }
        return DataResult.success(new WeightedStateProvider(list));
    }

    private static DataResult<SimpleWeightedEntryList<CompilableTargetBlockState>> Decompose(WeightedStateProvider wsp)
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

package com.github.mdcdi1315.mdex.block.blockstateproviders;

import com.github.mdcdi1315.basemodslib.utils.random.weighted.WeightedList;
import com.github.mdcdi1315.basemodslib.utils.random.weighted.WeightedListCodec;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.DataResult;

public final class WeightedStateProviderType
    extends AbstractBlockStateProviderType<WeightedStateProvider>
{
    public static final WeightedStateProviderType INSTANCE = new WeightedStateProviderType();

    private static DataResult<WeightedList<WeightedStateProvider.CompilableWeightedEntry>> Decompose(WeightedStateProvider wsp)
    {
        if (wsp == null) {
            return DataResult.error(() -> "Specified weighted state provider is null.");
        }
        return DataResult.success(wsp.States);
    }

    private static DataResult<WeightedStateProvider> Create(WeightedList<WeightedStateProvider.CompilableWeightedEntry> list)
    {
        if (list == null) {
            return DataResult.error(() -> "Specified list is null.");
        }
        return DataResult.success(new WeightedStateProvider(list));
    }

    @Override
    protected MapCodec<WeightedStateProvider> GetCodecInstance()
    {
        return new WeightedListCodec<>(WeightedStateProvider.CompilableWeightedEntry.GetCodec())
                .fieldOf("entries").flatXmap(
                    WeightedStateProviderType::Create,
                    WeightedStateProviderType::Decompose
        );
    }
}

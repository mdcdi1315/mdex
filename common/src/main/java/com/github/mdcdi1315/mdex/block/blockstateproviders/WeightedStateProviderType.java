package com.github.mdcdi1315.mdex.block.blockstateproviders;

import com.github.mdcdi1315.mdex.util.CompilableBlockState;
import com.github.mdcdi1315.mdex.util.weight.SimpleWeightedEntryList;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.DataResult;

public final class WeightedStateProviderType
    extends AbstractBlockStateProviderType<WeightedStateProvider>
{
    public static final WeightedStateProviderType INSTANCE = new WeightedStateProviderType();

    private static DataResult<SimpleWeightedEntryList<CompilableBlockState>> Decompose(WeightedStateProvider wsp)
    {
        if (wsp == null) {
            return DataResult.error(() -> "Specified weighted state provider is null.");
        }
        return DataResult.success(wsp.States);
    }

    private static DataResult<WeightedStateProvider> Create(SimpleWeightedEntryList<CompilableBlockState> list)
    {
        if (list == null) {
            return DataResult.error(() -> "Specified list is null.");
        }
        return DataResult.success(new WeightedStateProvider(list));
    }

    @Override
    protected MapCodec<WeightedStateProvider> GetCodecInstance() {
        return  SimpleWeightedEntryList.CreateSimpleWeightedEntryList(CompilableBlockState.GetMapCodec())
                .fieldOf("entries").flatXmap(
                        WeightedStateProviderType::Create,
                        WeightedStateProviderType::Decompose
                );
    }
}

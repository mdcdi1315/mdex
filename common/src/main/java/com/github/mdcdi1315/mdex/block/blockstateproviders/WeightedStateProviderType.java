package com.github.mdcdi1315.mdex.block.blockstateproviders;

import com.github.mdcdi1315.mdex.util.CompilableBlockState;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.DataResult;

import net.minecraft.util.random.SimpleWeightedRandomList;

public final class WeightedStateProviderType
    extends AbstractBlockStateProviderType<WeightedStateProvider>
{
    public static final WeightedStateProviderType INSTANCE = new WeightedStateProviderType();

    private static DataResult<WeightedStateProvider> Create(SimpleWeightedRandomList<CompilableBlockState> weightedList)
    {
        return weightedList.isEmpty() ?
                DataResult.error(() -> "Supplied an WeightedStateProvider which does not have any valid states.") :
                DataResult.success(new WeightedStateProvider(weightedList));
    }

    @Override
    protected MapCodec<WeightedStateProvider> GetCodecInstance() {
        return SimpleWeightedRandomList.wrappedCodec(CompilableBlockState.GetCodec())
                .comapFlatMap(WeightedStateProviderType::Create, (p) -> p.States)
                .fieldOf("entries");
    }
}

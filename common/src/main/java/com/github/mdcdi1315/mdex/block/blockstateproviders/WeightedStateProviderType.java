package com.github.mdcdi1315.mdex.block.blockstateproviders;

import com.mojang.serialization.Codec;
import net.minecraft.util.random.SimpleWeightedRandomList;
import com.github.mdcdi1315.mdex.util.CompilableTargetBlockState;

public final class WeightedStateProviderType
    extends AbstractBlockStateProviderType<WeightedStateProvider>
{
    private final Codec<WeightedStateProvider> codec;

    public WeightedStateProviderType()
    {
        codec = SimpleWeightedRandomList.wrappedCodec(CompilableTargetBlockState.GetCodec())
                .comapFlatMap(WeightedStateProvider::create, (p) -> p.weightedList)
                .fieldOf("entries").codec();
    }

    @Override
    public Codec<WeightedStateProvider> Codec() {
        return codec;
    }
}

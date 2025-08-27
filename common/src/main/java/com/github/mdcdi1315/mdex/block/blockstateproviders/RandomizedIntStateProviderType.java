package com.github.mdcdi1315.mdex.block.blockstateproviders;

import com.github.mdcdi1315.mdex.codecs.CodecUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.util.valueproviders.IntProvider;

public final class RandomizedIntStateProviderType
    extends AbstractBlockStateProviderType<RandomizedIntStateProvider>
{
    private final MapCodec<RandomizedIntStateProvider> codec;

    public RandomizedIntStateProviderType()
    {
        codec = MapCodec.assumeMapUnsafe(CodecUtils.CreateCodecDirect(
                AbstractBlockStateProvider.CODEC.fieldOf("source").forGetter((p_161592_) -> p_161592_.source),
                Codec.STRING.fieldOf("property").forGetter((p_161590_) -> p_161590_.propertyName),
                IntProvider.CODEC.fieldOf("values").forGetter((p_161578_) -> p_161578_.values),
                RandomizedIntStateProvider::new
        ));
    }

    @Override
    public MapCodec<RandomizedIntStateProvider> Codec() {
        return codec;
    }
}


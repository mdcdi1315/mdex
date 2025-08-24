package com.github.mdcdi1315.mdex.block.blockstateproviders;

import com.mojang.serialization.Codec;
import com.github.mdcdi1315.mdex.codecs.CodecUtils;
import net.minecraft.util.valueproviders.IntProvider;

public final class RandomizedIntStateProviderType
    extends AbstractBlockStateProviderType<RandomizedIntStateProvider>
{
    private final Codec<RandomizedIntStateProvider> codec;

    public RandomizedIntStateProviderType()
    {
        codec = CodecUtils.CreateCodecDirect(
                AbstractBlockStateProvider.CODEC.fieldOf("source").forGetter((p_161592_) -> p_161592_.source),
                Codec.STRING.fieldOf("property").forGetter((p_161590_) -> p_161590_.propertyName),
                IntProvider.CODEC.fieldOf("values").forGetter((p_161578_) -> p_161578_.values),
                RandomizedIntStateProvider::new
        );
    }

    @Override
    public Codec<RandomizedIntStateProvider> Codec() {
        return codec;
    }
}


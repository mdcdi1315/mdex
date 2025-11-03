package com.github.mdcdi1315.mdex.block.blockstateproviders;

import com.github.mdcdi1315.basemodslib.codecs.CodecUtils;

import com.mojang.serialization.Codec;

import net.minecraft.util.valueproviders.IntProvider;

public final class RandomizedIntStateProviderType
    extends AbstractBlockStateProviderType<RandomizedIntStateProvider>
{
    public static final RandomizedIntStateProviderType INSTANCE = new RandomizedIntStateProviderType();

    @Override
    protected Codec<RandomizedIntStateProvider> GetCodecInstance() {
        return CodecUtils.CreateCodecDirect(
                AbstractBlockStateProvider.CODEC.fieldOf("source").forGetter((p) -> p.source),
                Codec.STRING.fieldOf("property").forGetter((p) -> p.propertyName),
                IntProvider.CODEC.fieldOf("values").forGetter((p) -> p.values),
                RandomizedIntStateProvider::new
        );
    }
}

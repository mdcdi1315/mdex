package com.github.mdcdi1315.mdex.features.config;

import com.github.mdcdi1315.mdex.block.blockstateproviders.AbstractBlockStateProvider;
import com.github.mdcdi1315.mdex.codecs.CodecUtils;
import com.mojang.serialization.Codec;

import java.util.List;

public final class ModdedSimpleBlockFeatureConfiguration
    extends ModdedFeatureConfiguration
{
    public AbstractBlockStateProvider ToPlace;

    public static Codec<ModdedSimpleBlockFeatureConfiguration> GetCodec()
    {
        return CodecUtils.CreateCodecDirect(
                GetBaseCodec(),
                AbstractBlockStateProvider.CODEC.fieldOf("to_place").forGetter((ModdedSimpleBlockFeatureConfiguration m) -> m.ToPlace),
                ModdedSimpleBlockFeatureConfiguration::new
        );
    }

    public ModdedSimpleBlockFeatureConfiguration(List<String> modids , AbstractBlockStateProvider provider)
    {
        super(modids);
        ToPlace = provider;
        Compile();
    }

    @Override
    protected void compileConfigData() {
        ToPlace.Compile();
        if (!ToPlace.IsCompiled()) {
            setConfigAsInvalid();
        }
    }

    @Override
    protected void invalidateUntransformedFields() {

    }
}

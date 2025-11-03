package com.github.mdcdi1315.mdex.features.config;

import com.github.mdcdi1315.basemodslib.codecs.CodecUtils;

import com.github.mdcdi1315.mdex.block.blockstateproviders.AbstractBlockStateProvider;

import com.mojang.serialization.MapCodec;

public final class ModdedSimpleBlockFeatureConfigurationDetails
    implements IModdedFeatureConfigurationDetails
{
    public AbstractBlockStateProvider ToPlace;

    public static MapCodec<ModdedSimpleBlockFeatureConfigurationDetails> GetCodec()
    {
        return CodecUtils.CreateMapCodecDirect(
                AbstractBlockStateProvider.CODEC.fieldOf("to_place").forGetter((ModdedSimpleBlockFeatureConfigurationDetails m) -> m.ToPlace),
                ModdedSimpleBlockFeatureConfigurationDetails::new
        );
    }

    public ModdedSimpleBlockFeatureConfigurationDetails(AbstractBlockStateProvider provider)
    {
        ToPlace = provider;
        Compile();
    }

    @Override
    public void Compile() {
        ToPlace.Compile();
        if (!ToPlace.IsCompiled()) {
            throw new FeatureCompilationFailureException();
        }
    }

}

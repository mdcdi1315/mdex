package com.github.mdcdi1315.mdex.features.placement;

import com.github.mdcdi1315.basemodslib.codecs.LazyUnitCodec;

import com.mojang.serialization.Codec;

public final class IsValidSurfacePlacementModifierType
    extends AbstractModdedPlacementModifierType<IsValidSurfacePlacementModifier>
{
    public static IsValidSurfacePlacementModifierType INSTANCE = new IsValidSurfacePlacementModifierType();

    @Override
    protected Codec<IsValidSurfacePlacementModifier> GetCodecInstance() {
        return new LazyUnitCodec<>(IsValidSurfacePlacementModifier::new);
    }
}

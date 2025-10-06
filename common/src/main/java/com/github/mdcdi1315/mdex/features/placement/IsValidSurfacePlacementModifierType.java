package com.github.mdcdi1315.mdex.features.placement;

import com.github.mdcdi1315.mdex.codecs.LazyUnitMapCodec;

import com.mojang.serialization.MapCodec;

public final class IsValidSurfacePlacementModifierType
    extends AbstractModdedPlacementModifierType<IsValidSurfacePlacementModifier>
{
    public static IsValidSurfacePlacementModifierType INSTANCE = new IsValidSurfacePlacementModifierType();

    @Override
    protected MapCodec<IsValidSurfacePlacementModifier> GetCodecInstance() {
        return new LazyUnitMapCodec<>(IsValidSurfacePlacementModifier::new);
    }
}

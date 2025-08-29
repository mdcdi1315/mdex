package com.github.mdcdi1315.mdex.features.placement;

import com.mojang.serialization.MapCodec;

public final class IsValidSurfacePlacementModifierType
    extends AbstractModdedPlacementModifierType<IsValidSurfacePlacementModifier>
{
    public static IsValidSurfacePlacementModifierType INSTANCE = new IsValidSurfacePlacementModifierType();

    @Override
    protected MapCodec<IsValidSurfacePlacementModifier> GetCodecInstance() {
        return MapCodec.unit(new IsValidSurfacePlacementModifier());
    }
}

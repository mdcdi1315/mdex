package com.github.mdcdi1315.mdex.features.placement;

import com.mojang.serialization.MapCodec;

public final class IsValidCeilingPlacementModifierType
    extends AbstractModdedPlacementModifierType<IsValidCeilingPlacementModifier>
{
    public static IsValidSurfacePlacementModifierType INSTANCE = new IsValidSurfacePlacementModifierType();

    @Override
    protected MapCodec<IsValidCeilingPlacementModifier> GetCodecInstance() {
        return MapCodec.unit(IsValidCeilingPlacementModifier::new);
    }
}

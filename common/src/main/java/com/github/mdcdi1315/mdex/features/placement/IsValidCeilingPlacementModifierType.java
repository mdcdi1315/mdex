package com.github.mdcdi1315.mdex.features.placement;

import com.mojang.serialization.Codec;

public final class IsValidCeilingPlacementModifierType
    extends AbstractModdedPlacementModifierType<IsValidCeilingPlacementModifier>
{
    public static IsValidSurfacePlacementModifierType INSTANCE = new IsValidSurfacePlacementModifierType();

    @Override
    protected Codec<IsValidCeilingPlacementModifier> GetCodecInstance() {
        return Codec.unit(new IsValidCeilingPlacementModifier());
    }
}

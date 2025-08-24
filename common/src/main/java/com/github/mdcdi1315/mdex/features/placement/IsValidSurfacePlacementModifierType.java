package com.github.mdcdi1315.mdex.features.placement;

import com.mojang.serialization.Codec;

public final class IsValidSurfacePlacementModifierType
    extends AbstractModdedPlacementModifierType<IsValidSurfacePlacementModifier>
{
    public static IsValidSurfacePlacementModifierType INSTANCE = new IsValidSurfacePlacementModifierType();

    @Override
    protected Codec<IsValidSurfacePlacementModifier> GetCodecInstance() {
        return Codec.unit(new IsValidSurfacePlacementModifier());
    }
}

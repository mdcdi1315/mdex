package com.github.mdcdi1315.mdex.features.placement;

import com.mojang.serialization.Codec;

public final class PlaceOnlyOncePlacementModifierType
    extends AbstractModdedPlacementModifierType<PlaceOnlyOncePlacementModifier>
{
    public static PlaceOnlyOncePlacementModifierType INSTANCE = new PlaceOnlyOncePlacementModifierType();

    @Override
    protected Codec<PlaceOnlyOncePlacementModifier> GetCodecInstance() {
        return Codec.unit(new PlaceOnlyOncePlacementModifier());
    }
}

package com.github.mdcdi1315.mdex.features.placement;

import com.mojang.serialization.MapCodec;

public final class PlaceOnlyOncePlacementModifierType
    extends AbstractModdedPlacementModifierType<PlaceOnlyOncePlacementModifier>
{
    public static PlaceOnlyOncePlacementModifierType INSTANCE = new PlaceOnlyOncePlacementModifierType();

    @Override
    protected MapCodec<PlaceOnlyOncePlacementModifier> GetCodecInstance() {
        return MapCodec.unit(new PlaceOnlyOncePlacementModifier());
    }
}

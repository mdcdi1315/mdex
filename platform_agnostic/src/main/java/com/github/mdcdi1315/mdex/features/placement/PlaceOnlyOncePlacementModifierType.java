package com.github.mdcdi1315.mdex.features.placement;

import com.github.mdcdi1315.basemodslib.codecs.LazyUnitCodec;

import com.mojang.serialization.Codec;

public final class PlaceOnlyOncePlacementModifierType
    extends AbstractModdedPlacementModifierType<PlaceOnlyOncePlacementModifier>
{
    public static PlaceOnlyOncePlacementModifierType INSTANCE = new PlaceOnlyOncePlacementModifierType();

    @Override
    protected Codec<PlaceOnlyOncePlacementModifier> GetCodecInstance() {
        return new LazyUnitCodec<>(PlaceOnlyOncePlacementModifier::new);
    }
}

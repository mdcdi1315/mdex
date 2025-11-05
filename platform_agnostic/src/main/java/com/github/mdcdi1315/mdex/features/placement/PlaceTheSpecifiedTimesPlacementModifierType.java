package com.github.mdcdi1315.mdex.features.placement;

import com.github.mdcdi1315.basemodslib.codecs.CodecUtils;

import com.mojang.serialization.Codec;

public final class PlaceTheSpecifiedTimesPlacementModifierType
    extends AbstractModdedPlacementModifierType<PlaceTheSpecifiedTimesPlacementModifier>
{
    public static PlaceTheSpecifiedTimesPlacementModifierType INSTANCE = new PlaceTheSpecifiedTimesPlacementModifierType();

    @Override
    protected Codec<PlaceTheSpecifiedTimesPlacementModifier> GetCodecInstance() {
        return CodecUtils.CreateCodecDirect(
                CodecUtils.ShortRange(0 , 32767).fieldOf("attempts_for_placement").forGetter((g) -> g.TimesToPlace),
                CodecUtils.FLOAT_PROBABILITY.optionalFieldOf("discard_decrementing_attempt_probability" , 1f).forGetter((g) -> g.PlacementTimeCountingDiscardProbability),
                PlaceTheSpecifiedTimesPlacementModifier::new
        );
    }
}

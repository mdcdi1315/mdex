package com.github.mdcdi1315.mdex.features.placement;

import com.github.mdcdi1315.basemodslib.codecs.CodecUtils;

import com.mojang.serialization.Codec;

import net.minecraft.util.valueproviders.IntProvider;

public final class RandomizeInputPositionPlacementModifierType
    extends AbstractModdedPlacementModifierType<RandomizeInputPositionPlacementModifier>
{
    public static RandomizeInputPositionPlacementModifierType INSTANCE = new RandomizeInputPositionPlacementModifierType();

    @Override
    protected Codec<RandomizeInputPositionPlacementModifier> GetCodecInstance()
    {
        Codec<IntProvider> OFFSET_CODEC = IntProvider.codec(-32 , 32);
        return CodecUtils.CreateCodecDirect(
                OFFSET_CODEC.fieldOf("x_offset").forGetter((m) -> m.X_Offset),
                OFFSET_CODEC.fieldOf("y_offset").forGetter((m) -> m.Y_Offset),
                OFFSET_CODEC.fieldOf("z_offset").forGetter((m) -> m.Z_Offset),
                IntProvider.codec(0 , 26).fieldOf("positions_count").forGetter((m) -> m.PositionsToReturn),
                RandomizeInputPositionPlacementModifier::new
        );
    }
}

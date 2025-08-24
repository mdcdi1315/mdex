package com.github.mdcdi1315.mdex.features.placement;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

public abstract class AbstractModdedPlacementModifierType<T extends AbstractModdedPlacementModifier>
    implements PlacementModifierType<T>
{
    private final Codec<T> codec;

    protected AbstractModdedPlacementModifierType() {
        codec = GetCodecInstance();
    }

    protected abstract Codec<T> GetCodecInstance();

    public final Codec<T> codec() {
        return codec;
    }
}

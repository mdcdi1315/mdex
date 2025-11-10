package com.github.mdcdi1315.mdex.features.placement;

import com.mojang.serialization.MapCodec;

import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

public abstract class AbstractModdedPlacementModifierType<T extends AbstractModdedPlacementModifier>
    implements PlacementModifierType<T>
{
    private final MapCodec<T> codec;

    protected AbstractModdedPlacementModifierType() {
        codec = GetCodecInstance();
    }

    protected abstract MapCodec<T> GetCodecInstance();

    public final MapCodec<T> codec() {
        return codec;
    }
}

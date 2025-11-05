package com.github.mdcdi1315.mdex.features.placement;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.placement.PlacementContext;

import java.util.stream.Stream;

public final class PlaceOnlyOncePlacementModifier
    extends AbstractModdedPlacementModifier
{
    private boolean notplaced;

    public PlaceOnlyOncePlacementModifier() {
        Compile();
        notplaced = true;
    }

    @Override
    protected boolean CompilePlacementModifierData() {
        return true;
    }

    @Override
    protected Stream<BlockPos> GetPositions(PlacementContext cxt, RandomSource rs, BlockPos origin) {
        if (notplaced) {
            notplaced = false;
            return Stream.of(origin);
        }
        return Stream.empty();
    }

    @Override
    public AbstractModdedPlacementModifierType<? extends AbstractModdedPlacementModifier> GetType() {
        return PlaceOnlyOncePlacementModifierType.INSTANCE;
    }
}

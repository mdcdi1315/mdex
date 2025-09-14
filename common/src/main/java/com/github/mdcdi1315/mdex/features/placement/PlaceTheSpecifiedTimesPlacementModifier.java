package com.github.mdcdi1315.mdex.features.placement;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.placement.PlacementContext;

import java.util.stream.Stream;

public final class PlaceTheSpecifiedTimesPlacementModifier
    extends AbstractModdedPlacementModifier
{
    public short TimesToPlace;
    // The probability of not decrementing the number of times to place.
    // If 1, the feature will just have that number of times to be placed.
    public float PlacementTimeCountingDiscardProbability;

    public PlaceTheSpecifiedTimesPlacementModifier(short t , float p)
    {
        TimesToPlace = t;
        PlacementTimeCountingDiscardProbability = p;
    }

    @Override
    protected boolean CompilePlacementModifierData() {
        return true;
    }

    @Override
    protected Stream<BlockPos> GetPositions(PlacementContext cxt, RandomSource rs, BlockPos origin) {
        if (TimesToPlace > 0) {
            if (rs.nextFloat() < PlacementTimeCountingDiscardProbability) {
                TimesToPlace--;
            }
            return Stream.of(origin);
        }
        return Stream.empty();
    }

    @Override
    public AbstractModdedPlacementModifierType<? extends AbstractModdedPlacementModifier> type() {
        return PlaceTheSpecifiedTimesPlacementModifierType.INSTANCE;
    }
}

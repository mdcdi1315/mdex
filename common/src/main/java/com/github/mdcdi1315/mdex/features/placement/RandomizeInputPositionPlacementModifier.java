package com.github.mdcdi1315.mdex.features.placement;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.placement.PlacementContext;

import java.util.stream.Stream;

public final class RandomizeInputPositionPlacementModifier
    extends AbstractModdedPlacementModifier
{
    public IntProvider X_Offset;
    public IntProvider Y_Offset;
    public IntProvider Z_Offset;
    public IntProvider PositionsToReturn;

    public RandomizeInputPositionPlacementModifier(IntProvider xofs , IntProvider yofs , IntProvider zofs , IntProvider positions)
    {
        X_Offset = xofs;
        Y_Offset = yofs;
        Z_Offset = zofs;
        PositionsToReturn = positions;
    }

    @Override
    protected boolean CompilePlacementModifierData() {
        return true;
    }

    @Override
    protected Stream<BlockPos> GetPositions(PlacementContext cxt, RandomSource rs, BlockPos origin)
    {
        // Using this is much more efficient than creating an array on the fly.
        Stream.Builder<BlockPos> build = Stream.builder();
        int p = PositionsToReturn.sample(rs);
        for (int I = 0; I < p; I++)
        {
            build.add(origin.offset(
                    X_Offset.sample(rs),
                    Y_Offset.sample(rs),
                    Z_Offset.sample(rs)
            ));
        }
        return build.build();
    }

    @Override
    public AbstractModdedPlacementModifierType<? extends AbstractModdedPlacementModifier> type() {
        return RandomizeInputPositionPlacementModifierType.INSTANCE;
    }
}

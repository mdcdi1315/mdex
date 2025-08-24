package com.github.mdcdi1315.mdex.features.placement;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.placement.PlacementContext;

import java.util.Arrays;
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
        BlockPos[] newpositions = new BlockPos[PositionsToReturn.sample(rs)];
        for (int I = 0; I < newpositions.length; I++)
        {
            newpositions[I] = origin.offset(
                    X_Offset.sample(rs),
                    Y_Offset.sample(rs),
                    Z_Offset.sample(rs)
            );
        }
        return Arrays.stream(newpositions);
    }

    @Override
    public AbstractModdedPlacementModifierType<? extends AbstractModdedPlacementModifier> type() {
        return RandomizeInputPositionPlacementModifierType.INSTANCE;
    }
}

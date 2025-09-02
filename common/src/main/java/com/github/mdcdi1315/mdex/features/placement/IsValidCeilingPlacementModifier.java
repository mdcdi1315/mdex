package com.github.mdcdi1315.mdex.features.placement;

import com.github.mdcdi1315.mdex.block.BlockUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.placement.PlacementContext;

import java.util.stream.Stream;

public final class IsValidCeilingPlacementModifier
    extends AbstractModdedPlacementModifier
{
    @Override
    protected boolean CompilePlacementModifierData() {
        return true;
    }

    @Override
    protected Stream<BlockPos> GetPositions(PlacementContext cxt, RandomSource rs, BlockPos origin) {
        if (BlockUtils.BlockIsAirAndAboveSolid(cxt.getLevel() , origin))
        {
            return Stream.of(origin);
        }
        return Stream.empty();
    }

    @Override
    public AbstractModdedPlacementModifierType<? extends AbstractModdedPlacementModifier> type() {
        return IsValidSurfacePlacementModifierType.INSTANCE;
    }
}

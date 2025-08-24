package com.github.mdcdi1315.mdex.features.placement;

import com.github.mdcdi1315.mdex.block.BlockUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.placement.PlacementContext;

import java.util.stream.Stream;

/**
 * Specifies a placement modifier that asserts whether the current position and it's above one is
 * suitable for placing a surface feature.
 */
public final class IsValidSurfacePlacementModifier
    extends AbstractModdedPlacementModifier
{
    public IsValidSurfacePlacementModifier()
    {
    }

    @Override
    protected boolean CompilePlacementModifierData() {
        return true;
    }

    @Override
    protected Stream<BlockPos> GetPositions(PlacementContext cxt, RandomSource rs, BlockPos origin)
    {
        if (BlockUtils.BlockIsSolidAndAboveIsAir(cxt.getLevel() , origin))
        {
            return Stream.of(origin);
        }
        return Stream.empty();
    }

    @Override
    public AbstractModdedPlacementModifierType<IsValidSurfacePlacementModifier> type() {
        return IsValidSurfacePlacementModifierType.INSTANCE;
    }
}

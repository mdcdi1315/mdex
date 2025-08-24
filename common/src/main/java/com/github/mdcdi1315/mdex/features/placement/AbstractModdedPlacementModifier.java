package com.github.mdcdi1315.mdex.features.placement;

import com.github.mdcdi1315.mdex.util.Compilable;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;

import java.util.stream.Stream;

public abstract class AbstractModdedPlacementModifier
    extends PlacementModifier
    implements Compilable
{
    private boolean compiled;

    protected AbstractModdedPlacementModifier() {
        compiled = false;
    }

    protected abstract boolean CompilePlacementModifierData();

    protected abstract Stream<BlockPos> GetPositions(PlacementContext cxt , RandomSource rs , BlockPos origin);

    public final void Compile()
    {
        if (CompilePlacementModifierData())
        {
            compiled = true;
        }
    }

    public final boolean IsCompiled()
    {
        return compiled;
    }

    @Override
    public Stream<BlockPos> getPositions(PlacementContext placementContext, RandomSource randomSource, BlockPos blockPos)
    {
        if (compiled) {
            return GetPositions(placementContext , randomSource , blockPos);
        } else {
            return Stream.empty();
        }
    }

    @Override
    public abstract AbstractModdedPlacementModifierType<? extends AbstractModdedPlacementModifier> type();
}

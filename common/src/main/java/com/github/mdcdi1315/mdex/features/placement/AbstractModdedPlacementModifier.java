package com.github.mdcdi1315.mdex.features.placement;

import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.NotNull;
import com.github.mdcdi1315.mdex.MDEXBalmLayer;
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
    private byte state;
    private static final byte STATE_TESTED_FOR_COMPILATION = 1 << 0, STATE_COMPILED = 1 << 1;

    protected AbstractModdedPlacementModifier() {
        state = 0;
    }

    protected abstract boolean CompilePlacementModifierData();

    @NotNull
    protected abstract AbstractModdedPlacementModifierType<? extends AbstractModdedPlacementModifier> GetType();

    @NotNull
    protected abstract Stream<BlockPos> GetPositions(PlacementContext cxt , RandomSource rs , BlockPos origin);

    public final void Compile()
    {
        if ((state & STATE_TESTED_FOR_COMPILATION) != 0) { return; }
        state |= STATE_TESTED_FOR_COMPILATION;
        try {
            if (MDEXBalmLayer.LoggingFlags.FeaturePlacementModifier()) {
                MDEXBalmLayer.LOGGER.info("Attempting to compile placement modifier DCO {} with hash code {}." , getClass().getName() , hashCode());
            }
            if (CompilePlacementModifierData()) {
                state |= STATE_COMPILED;
            } else if (MDEXBalmLayer.LoggingFlags.FeaturePlacementModifier()) {
                MDEXBalmLayer.LOGGER.warn("Compilation for placement modifier DCO {} with hash code {} failed." , getClass().getName() , hashCode());
            }
        } catch (Exception e) {
            MDEXBalmLayer.LOGGER.warn("Cannot compile placement modifier DCO {} with hash code {} due to an error: {}" , getClass().getName() , hashCode() , e);
        }
    }

    public final boolean IsCompiled()
    {
        return (state & STATE_COMPILED) != 0;
    }

    @Override
    public Stream<BlockPos> getPositions(PlacementContext placementContext, RandomSource randomSource, BlockPos blockPos)
    {
        if ((state & STATE_COMPILED) != 0) {
            return GetPositions(placementContext , randomSource , blockPos);
        } else {
            return Stream.empty();
        }
    }

    @Override
    public final AbstractModdedPlacementModifierType<? extends AbstractModdedPlacementModifier> type() {
        return GetType();
    }
}

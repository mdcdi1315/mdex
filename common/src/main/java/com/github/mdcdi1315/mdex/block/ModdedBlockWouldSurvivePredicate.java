package com.github.mdcdi1315.mdex.block;

import com.github.mdcdi1315.DotNetLayer.System.ArgumentNullException;

import net.minecraft.core.Vec3i;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicateType;

import java.util.List;

public class ModdedBlockWouldSurvivePredicate
        extends AbstractModdedBlockPredicate
{
    public Vec3i Offset;

    public ModdedBlockWouldSurvivePredicate(List<String> modids, Vec3i offset)
    {
        super(modids);
        ArgumentNullException.ThrowIfNull(offset , "offset");
        Offset = offset;
    }

    @Override
    public BlockPredicateType<?> type() {
        return ModdedBlockWouldSurviveType.INSTANCE;
    }

    @Override
    public boolean test(WorldGenLevel worldGenLevel, BlockPos blockPos)
    {
        if (!getModIdListIsValid()) {
            return false;
        }
        var bs = worldGenLevel.getBlockState(blockPos.offset(Offset).offset(0 , -1 , 0));
        for (var b : new Block[] {
                Blocks.GRASS,
                Blocks.DIRT,
                Blocks.CLAY,
                Blocks.ROOTED_DIRT,
                Blocks.PODZOL
        })
        {
            if (bs.is(b))
            {
                return true;
            }
        }
        return false;
    }
}

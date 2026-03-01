package com.github.mdcdi1315.mdex.api.teleporter;

import com.github.mdcdi1315.DotNetLayer.System.Predicate;

import com.github.mdcdi1315.mdex.api.areafinding.FindAreaContext;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.LiquidBlock;

public final class FindTeleporterArea_OverworldImpl
    implements Predicate<FindAreaContext>
{
    @Override
    public boolean predicate(FindAreaContext data)
    {
        Block p = data.GetCursorBlockState().getBlock();

        if (!(p instanceof AirBlock || p instanceof LiquidBlock)) {
            return data.GetOtherBlock(FindAreaContext.GetAbove(data.GetCursorPosition())) instanceof AirBlock;
        } else {
            return false;
        }
    }
}

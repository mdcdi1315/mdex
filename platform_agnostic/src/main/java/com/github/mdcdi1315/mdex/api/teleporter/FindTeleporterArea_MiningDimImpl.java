package com.github.mdcdi1315.mdex.api.teleporter;

import com.github.mdcdi1315.DotNetLayer.System.Predicate;

import com.github.mdcdi1315.mdex.api.areafinding.FindAreaContext;

import net.minecraft.core.SectionPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.AirBlock;

public final class FindTeleporterArea_MiningDimImpl
    implements Predicate<FindAreaContext>
{
    @Override
    public boolean predicate(FindAreaContext data)
    {
        SectionPos temp = data.GetCursorPosition();
        Block b = data.GetCursorBlockState().getBlock();

        if (b instanceof AirBlock) {
            temp = FindAreaContext.GetBelow(temp);
            if (!(data.GetOtherBlock(temp) instanceof AirBlock)) {
                return data.GetOtherBlock(FindAreaContext.GetAbove(temp, 2)) instanceof AirBlock;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}

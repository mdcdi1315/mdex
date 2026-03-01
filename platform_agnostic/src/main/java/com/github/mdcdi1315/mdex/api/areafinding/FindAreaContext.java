package com.github.mdcdi1315.mdex.api.areafinding;

import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.MaybeNull;

import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public interface FindAreaContext
{
    SectionPos GetCursorPosition();

    BlockState GetCursorBlockState();

    @MaybeNull
    BlockState GetOtherBlockState(SectionPos pos);

    @MaybeNull
    default Block GetOtherBlock(SectionPos pos) {
        BlockState state = GetOtherBlockState(pos);
        return state == null ? null : state.getBlock();
    }

    static SectionPos GetAbove(SectionPos pos) { return GetYOffseted(pos,1); }

    static SectionPos GetAbove(SectionPos pos, int dist) { return GetYOffseted(pos, dist); }

    static SectionPos GetBelow(SectionPos pos) { return GetYOffseted(pos,-1); }

    static SectionPos GetBelow(SectionPos pos, int dist) { return GetYOffseted(pos, -dist); }

    static SectionPos GetNorth(SectionPos pos) { return GetRelative(pos, Direction.NORTH, 1); }

    static SectionPos GetNorth(SectionPos pos, int dist) { return GetRelative(pos, Direction.NORTH, dist); }

    static SectionPos GetSouth(SectionPos pos) { return GetRelative(pos, Direction.SOUTH, 1); }

    static SectionPos GetSouth(SectionPos pos, int dist) { return GetRelative(pos, Direction.SOUTH, dist); }

    static SectionPos GetEast(SectionPos pos) { return GetRelative(pos, Direction.EAST, 1); }

    static SectionPos GetEast(SectionPos pos, int dist) { return GetRelative(pos, Direction.EAST, dist); }

    static SectionPos GetWest(SectionPos pos) { return GetRelative(pos, Direction.WEST, 1); }

    static SectionPos GetWest(SectionPos pos, int dist) { return GetRelative(pos, Direction.WEST, dist); }

    static SectionPos GetRelative(SectionPos pos, Direction direction, int dist) {
        return SectionPos.of((direction.getStepX() * dist) + pos.getX(), (direction.getStepY() * dist) + pos.getY(), (direction.getStepZ() * dist) + pos.getZ());
    }

    static SectionPos GetYOffseted(SectionPos pos, int dist) { return SectionPos.of(pos.getX(), pos.getY() + dist, pos.getZ()); }
}

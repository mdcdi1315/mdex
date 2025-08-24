package com.github.mdcdi1315.mdex.features;

import com.github.mdcdi1315.DotNetLayer.System.Collections.Generic.KeyValuePair;

import net.minecraft.util.Mth;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import com.mojang.serialization.Codec;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.WorldGenLevel;
import com.github.mdcdi1315.mdex.util.SingleBlockState;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import com.github.mdcdi1315.mdex.features.config.ModdedOreFeatureConfiguration;

import java.util.List;
import java.util.function.Function;

public final class ModdedOreFeature
    extends ModdedFeature<ModdedOreFeatureConfiguration>
{
    public ModdedOreFeature(Codec<ModdedOreFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    protected boolean placeModdedFeature(FeaturePlaceContext<ModdedOreFeatureConfiguration> fpc)
    {
        RandomSource randomsource = fpc.random();
        BlockPos blockpos = fpc.origin();
        WorldGenLevel worldgenlevel = fpc.level();
        ModdedOreFeatureConfiguration oreconfiguration = fpc.config();

        // Select one of the four direction pairs randomly.

        KeyValuePair<Direction , Direction> selecteddir = FeaturePlacementUtils.SampleFromRandomSource(List.of(
                new KeyValuePair<>(Direction.EAST , Direction.NORTH),
                new KeyValuePair<>(Direction.WEST , Direction.NORTH),
                new KeyValuePair<>(Direction.EAST , Direction.SOUTH),
                new KeyValuePair<>(Direction.WEST , Direction.SOUTH)
        ) , randomsource);

        int sampledsize = randomsource.nextInt(oreconfiguration.Size);

        if (sampledsize < 9) {
            return PlaceOreLessThan8Blocks(worldgenlevel , blockpos , randomsource , oreconfiguration.TargetStates , selecteddir , sampledsize , oreconfiguration.DiscardChanceOnAirExposure);
        } else {
            return PlaceOreMoreThan8Blocks(worldgenlevel , blockpos , randomsource , oreconfiguration.TargetStates , selecteddir , sampledsize , oreconfiguration.DiscardChanceOnAirExposure);
        }
    }

    private static boolean PlaceOreMoreThan8Blocks(WorldGenLevel wgl , BlockPos origin , RandomSource rs , List<SingleBlockState> states , KeyValuePair<Direction , Direction> selected , int numberoforestoplace , float discardchanceonairexposure)
    {
        int remaining = numberoforestoplace;

        BlockPos[] positions = new BlockPos[numberoforestoplace / 8];
        BlockPos temp = origin;
        for (int I = 0; I < positions.length; I++ , temp = temp.above()) { positions[I] = temp; }

        Direction randomdirfinal = (rs.nextFloat() > 0.5f) ? selected.getValue() : selected.getKey();
        BlockPos offset;
        if (randomdirfinal.getStepX() != 0) {
            offset = new BlockPos(0 , 0 , randomdirfinal.getStepX());
        } else { // if (randomdirfinal.getStepZ() != 0)
            offset = new BlockPos(randomdirfinal.getStepZ() , 0 , 0);
        }

        for (BlockPos ps : positions)
        {
            int r = Mth.ceil(rs.nextIntBetweenInclusive(0, remaining) / 2f);
            for (int I = 0; I < r; I++)
            {
                boolean placed = false;
                temp = ps.relative(randomdirfinal , I);
                for (var s : states)
                {
                    if (CanPlaceOre(wgl.getBlockState(temp) , wgl::getBlockState , rs , discardchanceonairexposure , s , temp))
                    {
                        wgl.setBlock(temp , s.State.BlockState, 2);
                        remaining--;
                        placed = true;
                        break;
                    }
                }
                if (remaining < 1) { break; }
                if (placed && rs.nextInt(8) > 4)
                {
                    temp = temp.offset(offset);
                    for (var s : states)
                    {
                        if (CanPlaceOre(wgl.getBlockState(temp) , wgl::getBlockState , rs , discardchanceonairexposure , s , temp))
                        {
                            wgl.setBlock(temp , s.State.BlockState, 2);
                            break;
                        }
                    }
                }
            }
        }

        return remaining < numberoforestoplace;
    }

    private static boolean PlaceOreLessThan8Blocks(WorldGenLevel wgl , BlockPos origin , RandomSource rs , List<SingleBlockState> states , KeyValuePair<Direction , Direction> selected , int numberoforestoplace , float discardchanceonairexposure)
    {
        int placed = 0;

        for (BlockPos ps : new BlockPos[] { origin , origin.above() })
        {
            for (BlockPos pos : new BlockPos[] {
                    ps ,
                    ps.relative(selected.getKey()) ,
                    ps.relative(selected.getValue()) ,
                    ps.relative(selected.getValue(), 2)
            })
            {
                for (var s : states)
                {
                    if (CanPlaceOre(wgl.getBlockState(pos) , wgl::getBlockState , rs , discardchanceonairexposure , s , pos))
                    {
                        wgl.setBlock(pos , s.State.BlockState, 2);
                        placed++;
                        break;
                    }
                }
                if (placed >= numberoforestoplace) { break; }
            }
            if (placed >= numberoforestoplace) { break; }
        }

        return placed > 0;
    }

    public static boolean CanPlaceOre(BlockState state, Function<BlockPos, BlockState> adjacentStateAccessor, RandomSource random, float discardchanceonairexposure, SingleBlockState targetState, BlockPos pos)
    {
        if (!targetState.Target.test(state, random)) {
            return false;
        } else if (ShouldSkipAirCheck(random, discardchanceonairexposure)) {
            return true;
        } else {
            return !isAdjacentToAir(adjacentStateAccessor, pos);
        }
    }

    public static boolean CanPlaceOre(BlockGetter level , RandomSource random , float discardchanceonairexposure , SingleBlockState targetState , BlockPos pos)
    {
        return CanPlaceOre(level.getBlockState(pos) , level::getBlockState , random , discardchanceonairexposure , targetState , pos);
    }

    private static boolean ShouldSkipAirCheck(RandomSource random, float chance)
    {
        if (chance <= 0.0F) {
            return true;
        } else if (chance >= 1.0F) {
            return false;
        } else {
            return random.nextFloat() >= chance;
        }
    }
}

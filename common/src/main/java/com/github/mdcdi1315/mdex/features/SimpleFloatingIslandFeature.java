package com.github.mdcdi1315.mdex.features;

import com.github.mdcdi1315.DotNetLayer.System.Predicate;
import com.github.mdcdi1315.mdex.MDEXBalmLayer;
import com.github.mdcdi1315.mdex.block.BlockUtils;
import com.github.mdcdi1315.mdex.features.config.SimpleFloatingIslandConfiguration;
import com.github.mdcdi1315.mdex.features.floatingisland.CompilableIslandLayer;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;


public final class SimpleFloatingIslandFeature
    extends ModdedFeature<SimpleFloatingIslandConfiguration>
{
    public SimpleFloatingIslandFeature(Codec<SimpleFloatingIslandConfiguration> codec) {
        super(codec);
    }

    @Override
    protected boolean placeModdedFeature(FeaturePlaceContext<SimpleFloatingIslandConfiguration> fpc)
    {
        WorldGenLevel wgl = fpc.level();
        Predicate<BlockState> predicate = FeaturePlacementUtils.IsStrictlyAir();

        BlockPos layer = fpc.origin();

        // Ensure first that the current block is an air block, and it's below one is solid.
        if (BlockUtils.ReferentIsSolidBlock(wgl.getBlockState(layer)) ||
            BlockUtils.ReferentIsAirBlock(wgl.getBlockState(layer.below()))) {
            return false;
        }

        // Apply offset from the ground.
        layer = layer.offset(0 , fpc.config().MaxDistanceFromGround.sample(fpc.random()) , 0);

        // Check that we have enough space to generate the feature
        if (!IsConsideredValidPlacement(wgl , layer , fpc.config().Layers.size())) { return false; }

        // Read the layers in reverse order than they are defined.
        // Placement will be done layer by layer.

        boolean generated = false;
        // After each layer end, the FOR loop will also automatically update the y for the next island layer.
        for (int I = fpc.config().Layers.size() - 1 ; I > -1; I-- , layer = layer.offset(0 , 1 , 0))
        {
            if (wgl.ensureCanWrite(layer) == false)
            {
                MDEXBalmLayer.LOGGER.warn("Cannot place the simple floating island because the level is not writeable here.");
                return false;
            }
            PlaceSingleLayer(wgl , layer , predicate , fpc.config().Layers.get(I));
            generated = true;
        }

        // Do not generate the top features if we have not generated anything
        if (generated == false)
        {
            return true;
        }

        // Thankfully for us, the layer variable is already placed above the lastly placed layer.
        // From there, we can dispatch additional features to be generated on the island.

        int lastlayersize = fpc.config().Layers.get(fpc.config().Layers.size() - 1).Size;

        FeaturePlacementUtils.ForEachInHolderSetUnsafe(fpc.config().FeaturesToGenerateOnTop , fpc , layer , lastlayersize , SimpleFloatingIslandFeature::RunTopFeature);

        return true;
    }

    private static void RunTopFeature(PlacedFeature p , FeaturePlaceContext<SimpleFloatingIslandConfiguration> cfg , BlockPos layer , int lastlayersize)
    {
        var rd = cfg.random();
        // Run random values at most 12 times
        int rtimes = rd.nextInt(12);
        for (int r = 0; r < rtimes; r++)
        {
            // We do not care whether these will succeed or not.
            // However, break the loop on one such successful placement.
            if (p.place(cfg.level() , cfg.chunkGenerator() , rd , layer.offset(
                    rd.nextIntBetweenInclusive(-lastlayersize, lastlayersize) ,
                    rd.nextIntBetweenInclusive(0 , 3), // Play with y too but by only 3 blocks max
                    rd.nextIntBetweenInclusive(-lastlayersize, lastlayersize))))
            {
                break;
            }
        }
    }

    private static void PlaceSingleLayerModifyXAxis(WorldGenLevel level , Direction direct , BlockPos layerpos , Predicate<BlockState> predicate , CompilableIslandLayer layer)
    {
        BlockPos pos;
        for (int I = 0 , J = layer.Size - 1; I < layer.Size; I++ , J--)
        {
            pos = BlockUtils.RelativeUnmodifiedY(layerpos, direct, I);
            // This is equivalently the same as calling safeSetBlock.
            FeaturePlacementUtils.SafeSetBlock(level , pos , layer.State.BlockState , predicate);
            // No IFs here, the for loop manages the conditions appropriately for us.
            for (int O = J; O > 0; O--)
            {
                FeaturePlacementUtils.SafeSetBlock(level , pos.offset(0 , 0 ,O) , layer.State.BlockState , predicate);
                FeaturePlacementUtils.SafeSetBlock(level , pos.offset(0 , 0 , -O) , layer.State.BlockState , predicate);
            }
        }
    }

    private static void PlaceSingleLayerModifyZAxis(WorldGenLevel level , Direction direct , BlockPos layerpos , Predicate<BlockState> predicate , CompilableIslandLayer layer)
    {
        BlockPos pos;
        for (int I = 0 , J = layer.Size - 1; I < layer.Size; I++ , J--)
        {
            pos = BlockUtils.RelativeUnmodifiedY(layerpos, direct, I);
            // This is equivalently the same as calling safeSetBlock.
            FeaturePlacementUtils.SafeSetBlock(level , pos , layer.State.BlockState , predicate);
            // No IFs here, the for loop manages the conditions appropriately for us.
            for (int O = J; O > 0; O--)
            {
                FeaturePlacementUtils.SafeSetBlock(level , pos.offset(O , 0 , 0) , layer.State.BlockState , predicate);
                FeaturePlacementUtils.SafeSetBlock(level , pos.offset(-O , 0 , 0) , layer.State.BlockState , predicate);
            }
        }
    }

    private static void PlaceSingleLayer(WorldGenLevel level, BlockPos layerpos , Predicate<BlockState> predicate , CompilableIslandLayer layer)
    {
        for (Direction direct : new Direction[] { Direction.EAST , Direction.WEST , Direction.SOUTH , Direction.NORTH })
        {
            if (direct.getAxis() == Direction.Axis.X) {
                PlaceSingleLayerModifyXAxis(level , direct , layerpos , predicate , layer);
            } else {
                PlaceSingleLayerModifyZAxis(level , direct , layerpos , predicate , layer);
            }
        }
    }

    public static boolean IsConsideredValidPlacement(WorldGenLevel wgl , BlockPos origin , int nlayers)
    {
        BlockPos ofs = origin;
        for (int I = 0; I < nlayers; I++ , ofs = ofs.above())
        {
            if (BlockUtils.ReferentIsSolidBlock(wgl.getBlockState(ofs)))
            {
                // Not air block, possibly we are into a solid region which does not consist of caves
                return false;
            }
        }
        return true;
    }
}

package com.github.mdcdi1315.mdex.features;

import com.github.mdcdi1315.DotNetLayer.System.Predicate;

import com.github.mdcdi1315.mdex.MDEXBalmLayer;
import com.github.mdcdi1315.mdex.block.BlockUtils;
import com.github.mdcdi1315.mdex.features.config.AdvancedFloatingIslandConfiguration;
import com.github.mdcdi1315.mdex.features.floatingisland.AdvancedCompilableIslandLayer;
import com.github.mdcdi1315.mdex.features.floatingisland.FloatingIslandLayerDimensions;

import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public final class AdvancedFloatingIslandFeature
    extends ModdedFeature<AdvancedFloatingIslandConfiguration>
{
    public AdvancedFloatingIslandFeature(Codec<AdvancedFloatingIslandConfiguration> codec) {
        super(codec);
    }

    @Override
    protected boolean placeModdedFeature(FeaturePlaceContext<AdvancedFloatingIslandConfiguration> fpc)
    {
        Predicate<BlockState> predicate = FeaturePlacementUtils.IsStrictlyAir();

        WorldGenLevel wgl = fpc.level();
        BlockPos layer = fpc.origin();

        // Ensure first that the current block is an air block, and it's below one is solid.
        if (BlockUtils.ReferentIsSolidBlock(wgl.getBlockState(layer)) ||
            BlockUtils.ReferentIsAirBlock(wgl.getBlockState(layer.below()))) {
            return false;
        }

        // Apply offset from the ground.
        layer = layer.above(fpc.config().MaxDistanceFromGround.sample(fpc.random()));

        // Check that we have enough space to generate the feature
        if (!SimpleFloatingIslandFeature.IsConsideredValidPlacement(wgl , layer , fpc.config().Layers.size())) { return false; }

        // Read the layers in reverse order than they are defined.
        // Placement will be done layer by layer.

        // After each layer end, the FOR loop will also automatically update the y for the next island layer.
        FloatingIslandLayerDimensions dims = null;
        for (int I = fpc.config().Layers.size() - 1; I > -1; I-- , layer = layer.above())
        {
            if (!wgl.ensureCanWrite(layer))
            {
                MDEXBalmLayer.LOGGER.warn("Cannot place the simple floating island because the level is not writeable here.");
                return false;
            }
            dims = PlaceSingleLayer(wgl , layer , predicate , fpc.random() , fpc.config().Layers.get(I));
        }

        // Do not generate the top features if we have not generated anything
        if (dims == null)
        {
            return true;
        }

        FeaturePlacementUtils.ForEachInHolderSetUnsafe(fpc.config().FeaturesToGenerateOnTop , fpc , layer , dims , AdvancedFloatingIslandFeature::RunTopFeature);

        return true;
    }

    private static void RunTopFeature(PlacedFeature p , FeaturePlaceContext<AdvancedFloatingIslandConfiguration> cfg , BlockPos layer , FloatingIslandLayerDimensions lastlayerdims)
    {
        var rd = cfg.random();
        // Run random values at most 12 times
        int rtimes = rd.nextInt(12);
        for (int r = 0; r < rtimes; r++)
        {
            // We do not care whether these will succeed or not.
            // However, break the loop on one such successful placement.
            if (p.place(cfg.level() , cfg.chunkGenerator() , rd , layer.offset(
                    rd.nextIntBetweenInclusive(lastlayerdims.MINUS_X(), lastlayerdims.X()) ,
                    rd.nextIntBetweenInclusive(0 , 3), // Play with y too but by only 3 blocks max
                    rd.nextIntBetweenInclusive(lastlayerdims.MINUS_Z(), lastlayerdims.Z())
            ))) {
                break;
            }
        }
    }

    private static FloatingIslandLayerDimensions PlaceSingleLayer(WorldGenLevel level, BlockPos layerpos , Predicate<BlockState> predicate , RandomSource rs , AdvancedCompilableIslandLayer layer)
    {
        // Compute widths with randomness, if allowed by config
        int wz = layer.Randomized_Z.sample(rs);
        int wx = layer.Randomized_X.sample(rs);
        BlockPos pos1 , pos2;
        // Place the central block first.
        FeaturePlacementUtils.SafeSetBlock(level , layerpos , layer.Provider.GetBlockState(level , rs , layerpos) , predicate);
        // Modify Z and expand the blocks there.
        for (int Z = 1; Z < wz; Z++)
        {
            pos1 = layerpos.offset(0 , 0 , Z);
            pos2 = layerpos.offset(0 , 0 , -Z);
            FeaturePlacementUtils.SafeSetBlock(level , pos1 , layer.Provider.GetBlockState(level , rs , pos1) , predicate);
            FeaturePlacementUtils.SafeSetBlock(level , pos2 , layer.Provider.GetBlockState(level , rs , pos2) , predicate);
            BlockPos pzx1 , pzx2;
            // Expand the block column by Z_WIDTH / 2.
            for (int ZX = wz / 2; ZX > 0; ZX--)
            {
                pzx1 = pos1.offset(ZX , 0 , 0);
                FeaturePlacementUtils.SafeSetBlock(level , pzx1 , layer.Provider.GetBlockState(level, rs , pzx1) , predicate);
                pzx1 = pos1.offset(-ZX , 0 , 0);
                FeaturePlacementUtils.SafeSetBlock(level , pzx1 , layer.Provider.GetBlockState(level, rs , pzx1) , predicate);
                pzx2 = pos2.offset(ZX , 0 , 0);
                FeaturePlacementUtils.SafeSetBlock(level , pzx2 , layer.Provider.GetBlockState(level, rs , pzx2) , predicate);
                pzx2 = pos2.offset(-ZX , 0 , 0);
                FeaturePlacementUtils.SafeSetBlock(level , pzx2 , layer.Provider.GetBlockState(level, rs , pzx2) , predicate);
            }
        }
        // Modify X and expand also the blocks.
        for (int X = 1; X < wx; X++)
        {
            pos1 = layerpos.offset(X , 0 , 0);
            pos2 = layerpos.offset(-X , 0 , 0);
            FeaturePlacementUtils.SafeSetBlock(level , pos1 , layer.Provider.GetBlockState(level, rs , pos1) , predicate);
            FeaturePlacementUtils.SafeSetBlock(level , pos2 , layer.Provider.GetBlockState(level, rs , pos2) , predicate);
            BlockPos pxz1 , pxz2;
            // Expand the block column by X_WIDTH / 2.
            for (int XZ = wx / 2; XZ > 0; XZ--)
            {
                pxz1 = pos1.offset(0 , 0 , XZ);
                FeaturePlacementUtils.SafeSetBlock(level , pxz1 , layer.Provider.GetBlockState(level, rs , pxz1) , predicate);
                pxz1 = pos1.offset(0 , 0 , -XZ);
                FeaturePlacementUtils.SafeSetBlock(level , pxz1 , layer.Provider.GetBlockState(level, rs , pxz1) , predicate);
                pxz2 = pos2.offset(0 , 0 , XZ);
                FeaturePlacementUtils.SafeSetBlock(level , pxz2 , layer.Provider.GetBlockState(level, rs , pxz2) , predicate);
                pxz2 = pos2.offset(0 , 0 , -XZ);
                FeaturePlacementUtils.SafeSetBlock(level , pxz2 , layer.Provider.GetBlockState(level, rs , pxz2) , predicate);
            }
        }
        return new FloatingIslandLayerDimensions(wx , wz);
    }
}

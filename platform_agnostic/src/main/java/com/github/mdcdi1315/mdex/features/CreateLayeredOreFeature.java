package com.github.mdcdi1315.mdex.features;

/*
 * Portions of code are taken from Create mod:
 *
 * MIT License
 *
 * Copyright (c) The Create Team / The Creators of Create
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

import com.github.mdcdi1315.basemodslib.utils.*;

import com.github.mdcdi1315.mdex.util.SingleTargetBlockState;
import com.github.mdcdi1315.mdex.features.createlayeredore.*;
import com.github.mdcdi1315.mdex.features.config.ModdedFeatureConfiguration;
import com.github.mdcdi1315.mdex.features.config.CreateLayeredOreFeatureConfigurationDetails;

import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.BulkSectionAccess;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.levelgen.synth.SimplexNoise;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public final class CreateLayeredOreFeature
    extends ModdedFeature<ModdedFeatureConfiguration<CreateLayeredOreFeatureConfigurationDetails>>
{
    private static final float MAX_LAYER_DISPLACEMENT = 1.75f;
    private static final float LAYER_NOISE_FREQUENCY = 0.125f;

    private static final float MAX_RADIAL_THRESHOLD_REDUCTION = 0.25f;
    private static final float RADIAL_NOISE_FREQUENCY = 0.125f;

    public CreateLayeredOreFeature(Codec<ModdedFeatureConfiguration<CreateLayeredOreFeatureConfigurationDetails>> codec) {
        super(codec);
    }

    @Override
    protected boolean PlaceModdedFeature(FeaturePlaceContext<ModdedFeatureConfiguration<CreateLayeredOreFeatureConfigurationDetails>> fpc)
    {
        RandomSource random = fpc.random();
        WorldGenLevel worldGenLevel = fpc.level();
        CreateLayeredOreFeatureConfigurationDetails config = fpc.config().Details;

        // Before proceeding, check whether we can construct these. It will save CPU time if these throw OOM's at any scenario.
        SimplexNoise layerDisplacementNoise = new SimplexNoise(random),
                radiusNoise = new SimplexNoise(random);

        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

        int x0 = fpc.origin().getX();
        int y0 = fpc.origin().getY();
        int z0 = fpc.origin().getZ();

        // Check whether we are in a good and buildable area.
        if (y0 > worldGenLevel.getMaxY() - 50) { return false; }

        byte size = (byte)(config.Size + 1);

        // Choose stacking direction
        float theta = random.nextFloat() * Extensions.TWO_PI;
        float gy = (float) Math.cbrt(Extensions.RandomBetweenUnsafe(random, -1.0d, 1.0d)); // Make layer alignment tend towards horizontal more than vertical
        float xzRescale = Extensions.SquareRoot(1.0f - Extensions.Square(gy));
        float gx = Extensions.Cos(theta) * xzRescale;
        float gz = Extensions.Sin(theta) * xzRescale;

        boolean atleastone = false;

        // Compute now the layers to use.
        List<ResolvedLayerEntry> resolvedLayers = GetLayerEntries(config.GetRandomPattern(random) , size , random);

        // BulkSectionAccess is a disposable object, so create it just before it is used.
        BulkSectionAccess bulkSectionAccess = new BulkSectionAccess(worldGenLevel);

        try {

            /*
            * Original code defined this variable:
            * float radius = config.Size * 0.5f;
            *
            * However, since only two times it is used in this code, just replace it with the expression.
            */

            float onebyradius = 1.0f / (config.Size * 0.5f);
            int radiusBound = Extensions.Ceiling(config.Size * 0.5f) - 1;

            for (int dzBlock = -radiusBound; dzBlock <= radiusBound; dzBlock++)
            {
                float dz = dzBlock * onebyradius;
                if (Extensions.Square(dz) > 1) { continue; }

                for (int dxBlock = -radiusBound; dxBlock <= radiusBound; dxBlock++)
                {
                    float dx = dxBlock * onebyradius;
                    if (Extensions.Square(dz) + Extensions.Square(dx) > 1) { continue; }

                    for (int dyBlock = -radiusBound; dyBlock <= radiusBound; dyBlock++)
                    {
                        int currentX = x0 + dxBlock;
                        int currentY = y0 + dyBlock;
                        int currentZ = z0 + dzBlock;

                        // Since we can do it, precompute these to avoid the overhead of computing the noise values.
                        if (worldGenLevel.isOutsideBuildHeight(currentY)) { continue; }

                        mutablePos.set(currentX, currentY, currentZ);
                        if (!worldGenLevel.ensureCanWrite(mutablePos)) { continue; }

                        LevelChunkSection levelChunkSection = bulkSectionAccess.getSection(mutablePos);
                        if (levelChunkSection == null) { continue; }

                        float dy = dyBlock * onebyradius;

                        float distanceSquared = Extensions.Square(dz) + Extensions.Square(dx) + Extensions.Square(dy);

                        if (distanceSquared > 1) { continue; }

                        ResolvedLayerEntry layerEntry = GetLayerEntryBasedOnRampValue(resolvedLayers , (gx * dx + gy * dy + gz * dz) + (
                                (float) layerDisplacementNoise.getValue(
                                        currentX * LAYER_NOISE_FREQUENCY, currentY * LAYER_NOISE_FREQUENCY, currentZ * LAYER_NOISE_FREQUENCY
                                ) * (MAX_LAYER_DISPLACEMENT / size)
                        ));

                        if (
                            (distanceSquared > layerEntry.radialThresholdMultiplier()) || (
                                distanceSquared > (layerEntry.radialThresholdMultiplier() * Extensions.MapToRange(
                                    (float) radiusNoise.getValue(currentX * RADIAL_NOISE_FREQUENCY, currentY * RADIAL_NOISE_FREQUENCY, currentZ * RADIAL_NOISE_FREQUENCY),
                                    -1.0f, 1.0f, 1.0f - MAX_RADIAL_THRESHOLD_REDUCTION, 1.0f
                                ))
                            )
                        ) { continue; }

                        int localX = SectionPos.sectionRelative(currentX);
                        int localY = SectionPos.sectionRelative(currentY);
                        int localZ = SectionPos.sectionRelative(currentZ);
                        BlockState original = levelChunkSection.getBlockState(localX, localY, localZ) , target;

                        for (SingleTargetBlockState targetBlockState : layerEntry.layer().RollBlock(random))
                        {
                            target = targetBlockState.State.BlockState;
                            if (!target.isAir() && ModdedOreFeature.CanPlaceOre(
                                    original,
                                    bulkSectionAccess::getBlockState,
                                    random,
                                    config.DiscardChanceOnAirExposure,
                                    targetBlockState,
                                    mutablePos))
                            {
                                levelChunkSection.setBlockState(localX, localY, localZ, target, false);
                                atleastone = true;
                                break;
                            }
                        }

                    }
                }
            }

        } catch (Throwable throwable1) {
            try {
                bulkSectionAccess.close();
            } catch (Throwable throwable) {
                throwable1.addSuppressed(throwable);
            }

            throw throwable1;
        }

        bulkSectionAccess.close();
        return atleastone;
    }

    @SuppressWarnings("all")
    private static Pair<List<TemporaryLayerEntry> , Float> GetTemporaryEntries(LayerPattern pattern, byte size, RandomSource random)
    {
        List<TemporaryLayerEntry> tempLayers = new ArrayList<>();
        float layerSizeTotal = 0.0f;
        Layer current = null;
        while (layerSizeTotal < size) {
            Layer next = pattern.RollNext(current, random);
            // Not null at all times. We asserted that at compile-time.
            float layerSize = Extensions.RandomBetweenUnsafe(random, next.min_size, next.max_size);
            tempLayers.add(new TemporaryLayerEntry(next, layerSize));
            layerSizeTotal += layerSize;
            current = next;
        }
        return new Pair<>(tempLayers , layerSizeTotal);
    }

    private static List<ResolvedLayerEntry> GetLayerEntries(LayerPattern pattern, byte size, RandomSource random)
    {
        var p = GetTemporaryEntries(pattern , size , random);
        ArrayList<ResolvedLayerEntry> resolvedLayers = new ArrayList<>(p.first().size());
        float cumulativeLayerSize = -(p.second() - size) * random.nextFloat();
        for (TemporaryLayerEntry tempLayerEntry : p.first()) {
            float rampStartValue = resolvedLayers.isEmpty() ? Float.NEGATIVE_INFINITY : cumulativeLayerSize * (2.0f / size) - 1.0f;
            if ((cumulativeLayerSize += tempLayerEntry.size()) < 0) { continue; }
            resolvedLayers.add(new ResolvedLayerEntry(tempLayerEntry.layer(), Extensions.RandomBetweenUnsafe(random, 0.5f, 1.0f), rampStartValue));
        }
        resolvedLayers.trimToSize();
        return resolvedLayers;
    }

    private static ResolvedLayerEntry GetLayerEntryBasedOnRampValue(List<ResolvedLayerEntry> entries, float ramp_value)
    {
        int layerIndex = Collections.binarySearch(entries, new ResolvedLayerEntry(null, 0, ramp_value));

        if (layerIndex < 0) {
            // Counter (-insertionIndex - 1) return result, where insertionIndex = layerIndex + 1
            layerIndex = -2 - layerIndex;
        }

        return entries.get(layerIndex);
    }
}

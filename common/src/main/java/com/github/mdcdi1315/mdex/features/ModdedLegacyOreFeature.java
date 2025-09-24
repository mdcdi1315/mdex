package com.github.mdcdi1315.mdex.features;

import com.github.mdcdi1315.mdex.util.Extensions;
import com.github.mdcdi1315.mdex.features.config.ModdedOreFeatureConfiguration;

import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import com.mojang.serialization.Codec;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.BulkSectionAccess;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

import java.util.BitSet;

public final class ModdedLegacyOreFeature
    extends ModdedFeature<ModdedOreFeatureConfiguration>
{
    public ModdedLegacyOreFeature(Codec<ModdedOreFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    protected boolean placeModdedFeature(FeaturePlaceContext<ModdedOreFeatureConfiguration> fpc)
    {
        RandomSource randomsource = fpc.random();
        BlockPos blockpos = fpc.origin();
        WorldGenLevel worldgenlevel = fpc.level();
        ModdedOreFeatureConfiguration oreconfiguration = fpc.config();
        float f = randomsource.nextFloat() * Extensions.PI;
        float f1 = (float)oreconfiguration.Size / 8.0F;
        int i = Extensions.Ceiling(((float)oreconfiguration.Size / 33.0F) / 2.0F);
        double sinf1 = Math.sin(f) * (double)f1;
        double cosf1 = Math.cos(f) * (double)f1;
        int ceilf1mi = Extensions.Ceiling(f1) - i;
        int k = blockpos.getX() - ceilf1mi;
        int l = blockpos.getY() - 2 - i;
        int i1 = blockpos.getZ() - ceilf1mi;
        int j1 = 2 * (Extensions.Ceiling(f1) + i);

        for (int l1 = k; l1 <= k + j1; ++l1)
        {
            for (int i2 = i1; i2 <= i1 + j1; ++i2)
            {
                if (l <= worldgenlevel.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, l1, i2))
                {
                    return this.doPlace(
                            worldgenlevel,
                            randomsource,
                            oreconfiguration,
                            (double)blockpos.getX() + sinf1,
                            (double)blockpos.getX() - sinf1,
                            (double)blockpos.getZ() + cosf1,
                            (double)blockpos.getZ() - cosf1,
                            blockpos.getY() + randomsource.nextInt(3) - 2,
                            blockpos.getY() + randomsource.nextInt(3) - 2,
                            k,
                            l,
                            i1,
                            j1,
                            2 * (2 + i)
                    );
                }
            }
        }

        return false;
    }


    private boolean doPlace(WorldGenLevel level, RandomSource random, ModdedOreFeatureConfiguration config, double minX, double maxX, double minZ, double maxZ, double minY, double maxY, int x, int y, int z, int width, int height)
    {
        byte j = config.Size;
        double[] adouble = new double[j * 4];

        for (int k = 0; k < j; ++k)
        {
            float f = (float)k / (float)j;
            adouble[k * 4] = Extensions.Lerp(f, minX, maxX);
            adouble[k * 4 + 1] = Extensions.Lerp(f, minY, maxY);
            adouble[k * 4 + 2] = Extensions.Lerp(f, minZ, maxZ);
            adouble[k * 4 + 3] = ((double)(Extensions.Sin(Extensions.PI * f) + 1.0F) * (random.nextDouble() * (double)j / 16.0D) + 1.0D) / 2.0D;
        }

        for (byte l3 = 0; l3 < j - 1; ++l3)
        {
            int il3 = l3 * 4 + 3;
            if (adouble[il3] > 0.0D)
            {
                for (byte i4 = (byte)(l3 + 1); i4 < j; ++i4)
                {
                    int ii4 = i4 * 4 + 3;
                    if (adouble[ii4] > 0.0D)
                    {
                        double d14 = adouble[il3] - adouble[ii4];
                        if (Extensions.Square(d14) >
                                Extensions.Square(adouble[l3 * 4] - adouble[i4 * 4]) +
                                        Extensions.Square(adouble[l3 * 4 + 1] - adouble[i4 * 4 + 1]) +
                                        Extensions.Square(adouble[l3 * 4 + 2] - adouble[i4 * 4 + 2])
                        )
                        {
                            if (d14 > 0.0D) {
                                adouble[ii4] = -1.0D;
                            } else {
                                adouble[il3] = -1.0D;
                            }
                        }
                    }
                }
            }
        }

        boolean atleastone = false;
        BitSet bitset = new BitSet(width * height * width);

        try (BulkSectionAccess bulksectionaccess = new BulkSectionAccess(level))
        {
            for (byte j4 = 0; j4 < j; ++j4)
            {
                double d9 = adouble[j4 * 4 + 3];
                if (d9 >= 0.0D)
                {
                    double d11 = adouble[j4 * 4];
                    double d13 = adouble[j4 * 4 + 1];
                    double d15 = adouble[j4 * 4 + 2];
                    int k4 = Math.max(Extensions.Floor(d11 - d9), x);
                    int l = Math.max(Extensions.Floor(d13 - d9), y);
                    int i1 = Math.max(Extensions.Floor(d15 - d9), z);
                    int j1 = Math.max(Extensions.Floor(d11 + d9), k4);
                    int k1 = Math.max(Extensions.Floor(d13 + d9), l);
                    int l1 = Math.max(Extensions.Floor(d15 + d9), i1);

                    BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

                    for (int i2 = k4; i2 <= j1; ++i2)
                    {
                        double d5 = ((double)i2 + 0.5D - d11) / d9;
                        if ((d5 *= d5) < 1.0D)
                        {
                            for (int j2 = l; !level.isOutsideBuildHeight(j2) && j2 <= k1; ++j2)
                            {
                                double d6 = ((double)j2 + 0.5D - d13) / d9;
                                if (d5 + (d6 *= d6) < 1.0D)
                                {
                                    for (int k2 = i1; k2 <= l1; ++k2)
                                    {
                                        double d7 = ((double)k2 + 0.5D - d15) / d9;
                                        if (d5 + d6 + Extensions.Square(d7) < 1.0D)
                                        {
                                            int l2 = i2 - x + (j2 - y) * width + (k2 - z) * width * height;
                                            if (!bitset.get(l2))
                                            {
                                                bitset.set(l2);
                                                blockpos$mutableblockpos.set(i2, j2, k2);
                                                // Although the below check is a bit useless, leave it as is
                                                if (level.ensureCanWrite(blockpos$mutableblockpos))
                                                {
                                                    LevelChunkSection levelchunksection = bulksectionaccess.getSection(blockpos$mutableblockpos);
                                                    if (levelchunksection != null)
                                                    {
                                                        int i3 = SectionPos.sectionRelative(i2);
                                                        int j3 = SectionPos.sectionRelative(j2);
                                                        int k3 = SectionPos.sectionRelative(k2);
                                                        BlockState blockstate = levelchunksection.getBlockState(i3, j3, k3);

                                                        for (var statetarget : config.TargetStates)
                                                        {
                                                            if (ModdedOreFeature.CanPlaceOre(blockstate, bulksectionaccess::getBlockState, random, config.DiscardChanceOnAirExposure, statetarget, blockpos$mutableblockpos)) {
                                                                levelchunksection.setBlockState(i3, j3, k3, statetarget.State.BlockState, false);
                                                                atleastone = true;
                                                                break;
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return atleastone;
    }
}

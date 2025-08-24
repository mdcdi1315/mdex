package com.github.mdcdi1315.mdex.block.blockstateproviders;

import net.minecraft.util.Mth;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import com.google.common.collect.Lists;
import net.minecraft.util.InclusiveRange;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

import com.github.mdcdi1315.mdex.util.CompilableTargetBlockState;

import java.util.List;


public class DualNoiseProvider
        extends NoiseProvider
{
    public final InclusiveRange<Integer> variety;
    public final NormalNoise.NoiseParameters slowNoiseParameters;
    public final float slowScale;
    public final NormalNoise slowNoise;

    public DualNoiseProvider(InclusiveRange<Integer> variety, NormalNoise.NoiseParameters slowNoiseParameters, float slowScale, long seed, NormalNoise.NoiseParameters parameters, float scale, List<CompilableTargetBlockState> states)
    {
        super(seed, parameters, scale, states);
        this.variety = variety;
        this.slowScale = slowScale;
        this.slowNoise = NormalNoise.create(new WorldgenRandom(new LegacyRandomSource(seed)), this.slowNoiseParameters = slowNoiseParameters);
    }

    public BlockState getState(RandomSource random, BlockPos pos) {
        double d0 = this.getSlowNoiseValue(pos);
        int i = (int) Mth.clampedMap(d0, -1.0, 1.0, (double)this.variety.minInclusive(), (this.variety.maxInclusive() + 1));
        List<BlockState> list = Lists.newArrayListWithCapacity(i);

        for(int j = 0; j < i; ++j) {
            list.add(this.getRandomState(this.states.stream().map((CompilableTargetBlockState o) -> o.BlockState).toList(), this.getSlowNoiseValue(pos.offset(j * '픑', 0, j * '薺'))));
        }

        return this.getRandomState(list, pos, (double)this.scale);
    }

    protected double getSlowNoiseValue(BlockPos pos) {
        return this.slowNoise.getValue(pos.getX() * this.slowScale, pos.getY() * this.slowScale, pos.getZ() * this.slowScale);
    }

    public AbstractBlockStateProviderType<?> type() {
        return CustomBlockStateProviderRegistrySubsystem.DUAL_NOISE_PROVIDER;
    }
}

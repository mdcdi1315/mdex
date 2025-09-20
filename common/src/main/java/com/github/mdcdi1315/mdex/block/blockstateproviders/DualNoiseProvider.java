package com.github.mdcdi1315.mdex.block.blockstateproviders;

import com.github.mdcdi1315.mdex.util.Extensions;
import com.github.mdcdi1315.mdex.util.CompilableBlockState;
import com.github.mdcdi1315.mdex.util.IntegerInclusiveRange;

import net.minecraft.core.Holder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import com.google.common.collect.Lists;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.minecraft.world.level.levelgen.XoroshiroRandomSource;

import java.util.List;

public class DualNoiseProvider
        extends NoiseProvider
{
    public final IntegerInclusiveRange variety;
    public final Holder<NormalNoise.NoiseParameters> slowNoiseParameters;
    public final float slowScale;
    public NormalNoise slowNoise;

    public DualNoiseProvider(IntegerInclusiveRange variety, Holder<NormalNoise.NoiseParameters> slowNoiseParameters, float slowScale, long seed, Holder<NormalNoise.NoiseParameters> parameters, float scale, List<CompilableBlockState> states)
    {
        super(seed, parameters, scale, states);
        this.variety = variety;
        this.slowScale = slowScale;
        this.slowNoiseParameters = slowNoiseParameters;
    }

    public BlockState getState(RandomSource random, BlockPos pos) {
        int i = (int) Math.round(Extensions.ClampedMapToRange(this.getSlowNoiseValue(pos), -1.0, 1.0, this.variety.GetSelectedMinInclusiveValue(), (this.variety.GetSelectedMaxInclusiveValue() + 1)));
        List<BlockState> list = Lists.newArrayListWithCapacity(i);

        for (int j = 0; j < i; ++j) {
            list.add(this.getRandomState(this.states.stream().map(o -> o.BlockState).toList(), this.getSlowNoiseValue(pos.offset(j * '픑', 0, j * '薺'))));
        }

        return this.getRandomState(list, pos, this.scale);
    }

    private void CreateSlowNoise() {
        // The same here as well
        slowNoise = NormalNoise.create(new XoroshiroRandomSource(seed), slowNoiseParameters.value());
    }

    protected double getSlowNoiseValue(BlockPos pos) {
        if (slowNoise == null) { CreateSlowNoise(); }
        return this.slowNoise.getValue(pos.getX() * this.slowScale, pos.getY() * this.slowScale, pos.getZ() * this.slowScale);
    }

    public AbstractBlockStateProviderType<?> type() {
        return CustomBlockStateProviderRegistrySubsystem.DUAL_NOISE_PROVIDER;
    }
}

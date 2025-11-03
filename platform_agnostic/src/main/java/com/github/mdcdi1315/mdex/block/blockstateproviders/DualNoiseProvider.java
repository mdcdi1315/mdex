package com.github.mdcdi1315.mdex.block.blockstateproviders;

import com.github.mdcdi1315.basemodslib.utils.Extensions;
import com.github.mdcdi1315.mdex.util.CompilableBlockState;
import com.github.mdcdi1315.mdex.util.IntegerInclusiveRange;

import net.minecraft.core.Holder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import com.google.common.collect.Lists;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

import java.util.List;

public class DualNoiseProvider
        extends NoiseStateProvider
{
    public final IntegerInclusiveRange variety;
    public final Holder<NormalNoise.NoiseParameters> slowNoiseParameters;
    public final float slowScale;
    public NormalNoise slowNoise;

    public DualNoiseProvider(NoiseStateProviderData data, IntegerInclusiveRange variety, Holder<NormalNoise.NoiseParameters> slowNoiseParameters, float slowScale, List<CompilableBlockState> states)
    {
        super(data, states);
        this.variety = variety;
        this.slowScale = slowScale;
        this.slowNoiseParameters = slowNoiseParameters;
    }

    @Override
    public BlockState GetBlockState(BlockStateProviderContext context) {
        var random = context.source();
        var pos = context.position();
        int i = (int) Math.round(Extensions.ClampedMapToRange(this.getSlowNoiseValue(random, pos), -1.0, 1.0, this.variety.GetSelectedMinInclusiveValue(), (this.variety.GetSelectedMaxInclusiveValue() + 1)));
        List<BlockState> list = Lists.newArrayListWithCapacity(i);

        for (int j = 0; j < i; ++j) {
            list.add(this.GetRandomState(States, this.getSlowNoiseValue(random, pos.offset(j * '픑', 0, j * '薺'))));
        }

        return this.GetRandomState_2(random, list, pos, Data.scale());
    }

    private void CreateSlowNoise(RandomSource rs) {
        // The same here as well
        slowNoise = NormalNoise.create(rs, slowNoiseParameters.value());
    }

    protected double getSlowNoiseValue(RandomSource rs, BlockPos pos) {
        if (slowNoise == null) { CreateSlowNoise(rs); }
        return this.slowNoise.getValue(pos.getX() * this.slowScale, pos.getY() * this.slowScale, pos.getZ() * this.slowScale);
    }

    @Override
    public AbstractBlockStateProviderType<?> GetType() {
        return DualNoiseProviderType.INSTANCE;
    }
}
package com.github.mdcdi1315.mdex.block.blockstateproviders;

import net.minecraft.util.Mth;
import net.minecraft.core.BlockPos;
import com.mojang.datafixers.Products;
import com.mojang.serialization.Codec;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import com.github.mdcdi1315.mdex.util.CompilableTargetBlockState;

import java.util.List;

public class NoiseProvider extends NoiseBasedStateProvider {
    public static final Codec<NoiseProvider> CODEC = RecordCodecBuilder.create((p_191462_) -> noiseProviderCodec(p_191462_).apply(p_191462_, NoiseProvider::new));
    protected final List<CompilableTargetBlockState> states;
    private boolean compiled;

    protected static <P extends NoiseProvider> Products.P4<RecordCodecBuilder.Mu<P>, Long, NormalNoise.NoiseParameters, Float, List<CompilableTargetBlockState>> noiseProviderCodec(RecordCodecBuilder.Instance<P> instance) {
        return noiseCodec(instance).and(Codec.list(CompilableTargetBlockState.GetCodec().codec()).fieldOf("states").forGetter((p_191448_) -> p_191448_.states));
    }

    public NoiseProvider(long seed, NormalNoise.NoiseParameters parameters, float scale, List<CompilableTargetBlockState> states) {
        super(seed, parameters, scale);
        this.states = states;
    }

    @Override
    protected AbstractBlockStateProviderType<?> type() {
        return CustomBlockStateProviderRegistrySubsystem.NOISE_PROVIDER;
    }

    public BlockState getState(RandomSource random, BlockPos pos) {
        return this.getRandomState(this.states.stream().map((CompilableTargetBlockState s) -> s.BlockState).toList(), pos, this.scale);
    }

    protected BlockState getRandomState(List<BlockState> possibleStates, BlockPos pos, double delta) {
        double d0 = this.getNoiseValue(pos, delta);
        return this.getRandomState(possibleStates, d0);
    }

    protected BlockState getRandomState(List<BlockState> possibleStates, double delta) {
        double d0 = Mth.clamp(((double)1.0F + delta) / (double)2.0F, 0.0F, 0.9999);
        return possibleStates.get((int)(d0 * (double)possibleStates.size()));
    }

    @Override
    public void Compile()
    {
        for (var i : states)
        {
            i.Compile();
            if (i.IsCompiled() == false)
            {
                compiled = false;
                return;
            }
        }
        compiled = true;
    }

    @Override
    public boolean IsCompiled() {
        return compiled;
    }
}

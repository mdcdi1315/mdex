package com.github.mdcdi1315.mdex.block.blockstateproviders;

import com.github.mdcdi1315.mdex.util.Extensions;
import com.github.mdcdi1315.mdex.util.CompilableBlockState;

import net.minecraft.core.Holder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

import com.mojang.datafixers.Products;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

public class NoiseProvider extends NoiseBasedStateProvider {
    public static final Codec<NoiseProvider> CODEC = RecordCodecBuilder.create((p_191462_) -> noiseProviderCodec(p_191462_).apply(p_191462_, NoiseProvider::new));
    protected final List<CompilableBlockState> states;
    private boolean compiled;

    protected static <P extends NoiseProvider> Products.P4<RecordCodecBuilder.Mu<P>, Long, Holder<NormalNoise.NoiseParameters>, Float, List<CompilableBlockState>> noiseProviderCodec(RecordCodecBuilder.Instance<P> instance) {
        return noiseCodec(instance).and(Codec.list(CompilableBlockState.GetCodec()).fieldOf("states").forGetter((n) -> n.states));
    }

    public NoiseProvider(long seed, Holder<NormalNoise.NoiseParameters> parameters, float scale, List<CompilableBlockState> states) {
        super(seed, parameters, scale);
        this.states = states;
    }

    @Override
    protected AbstractBlockStateProviderType<?> type() {
        return CustomBlockStateProviderRegistrySubsystem.NOISE_PROVIDER;
    }

    public BlockState getState(RandomSource random, BlockPos pos) {
        return this.getRandomState(this.states.stream().map(s -> s.BlockState).toList(), pos, this.scale);
    }

    protected BlockState getRandomState(List<BlockState> possibleStates, BlockPos pos, double delta) {
        return this.getRandomState(possibleStates, this.getNoiseValue(pos, delta));
    }

    protected BlockState getRandomState(List<BlockState> possibleStates, double delta) {
        return possibleStates.get((int)(Extensions.Clamp(((double)1.0F + delta) / (double)2.0F, 0.0F, 0.9999) * possibleStates.size()));
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

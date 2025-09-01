package com.github.mdcdi1315.mdex.block.blockstateproviders;

import com.mojang.datafixers.Products;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.Holder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.minecraft.world.level.levelgen.XoroshiroRandomSource;


public abstract class NoiseBasedStateProvider extends AbstractBlockStateProvider {
    protected final long seed;
    protected final Holder<NormalNoise.NoiseParameters> parameters;
    protected final float scale;
    private NormalNoise noise;

    protected static <P extends NoiseBasedStateProvider> Products.P3<RecordCodecBuilder.Mu<P>, Long, Holder<NormalNoise.NoiseParameters>, Float> noiseCodec(RecordCodecBuilder.Instance<P> instance) {
        return instance.group(
                Codec.LONG.fieldOf("seed").forGetter((n) -> n.seed),
                NormalNoise.NoiseParameters.CODEC.fieldOf("noise").forGetter((n) -> n.parameters),
                ExtraCodecs.POSITIVE_FLOAT.fieldOf("scale").forGetter((n) -> n.scale)
        );
    }

    protected NoiseBasedStateProvider(long seed, Holder<NormalNoise.NoiseParameters> parameters, float scale) {
        this.parameters = parameters;
        this.scale = scale;
        this.seed = seed;
    }

    private void CreateNoise() {
        // Prefer saving memory than CPU in this case, by lazily creating the noise.
        // The null check is itself very fast.
        noise = NormalNoise.create(new XoroshiroRandomSource(seed), parameters.value());
    }

    protected double getNoiseValue(BlockPos pos, double delta) {
        if (noise == null) { CreateNoise(); }
        return noise.getValue((double)pos.getX() * delta, (double)pos.getY() * delta, (double)pos.getZ() * delta);
    }
}

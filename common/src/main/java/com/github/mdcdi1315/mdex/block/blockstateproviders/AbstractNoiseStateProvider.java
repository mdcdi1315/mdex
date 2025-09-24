package com.github.mdcdi1315.mdex.block.blockstateproviders;

import com.github.mdcdi1315.DotNetLayer.System.ArgumentNullException;

import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

public abstract class AbstractNoiseStateProvider
    extends AbstractBlockStateProvider
{
    protected final NoiseStateProviderData Data;
    private NormalNoise noise;

    protected static <P extends AbstractNoiseStateProvider> RecordCodecBuilder<P , NoiseStateProviderData> GetBaseCodec() {
        return NoiseStateProviderData.GetCodec().forGetter((P np) -> np.Data);
    }

    protected AbstractNoiseStateProvider(NoiseStateProviderData data) {
        ArgumentNullException.ThrowIfNull(data , "data");
        Data = data;
    }

    private void CreateNoise(RandomSource rs) {
        // Prefer saving memory than CPU in this case, by lazily creating the noise.
        // The null check is itself very fast.
        noise = Data.CreateNoise(rs);
    }

    protected double GetNoiseValue(RandomSource source, BlockPos pos, double delta) {
        if (noise == null) { CreateNoise(source); }
        return noise.getValue((double)pos.getX() * delta, (double)pos.getY() * delta, (double)pos.getZ() * delta);
    }
}

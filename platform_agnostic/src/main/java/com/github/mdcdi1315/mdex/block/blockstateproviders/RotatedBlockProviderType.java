package com.github.mdcdi1315.mdex.block.blockstateproviders;

import com.github.mdcdi1315.basemodslib.codecs.CodecUtils;
import com.github.mdcdi1315.mdex.util.CompilableBlockState;
import com.mojang.serialization.Codec;

public final class RotatedBlockProviderType
    extends AbstractBlockStateProviderType<RotatedBlockProvider>
{
    public static final RotatedBlockProviderType INSTANCE = new RotatedBlockProviderType();

    @Override
    protected Codec<RotatedBlockProvider> GetCodecInstance() {
        return CodecUtils.CreateCodecDirect(
                CompilableBlockState.GetCodec().fieldOf("state").forGetter((s) -> s.Block),
                RotatedBlockProvider::new
        );
    }
}

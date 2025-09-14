package com.github.mdcdi1315.mdex.features.floatingisland;

import com.github.mdcdi1315.mdex.codecs.CodecUtils;
import com.github.mdcdi1315.mdex.util.Compilable;
import com.github.mdcdi1315.mdex.util.CompilableTargetBlockState;
import com.mojang.serialization.Codec;

public class CompilableIslandLayer
    implements Compilable
{
    public CompilableTargetBlockState State;
    public byte Size;

    public CompilableIslandLayer(CompilableTargetBlockState state , byte size)
    {
        State = state;
        Size = size;
    }

    public static Codec<CompilableIslandLayer> GetCodec()
    {
        return CodecUtils.CreateCodecDirect(
                CompilableTargetBlockState.GetCodec().fieldOf("state").forGetter((p) -> p.State),
                CodecUtils.ByteRange(1 , 32).fieldOf("size").forGetter((p) -> p.Size),
                CompilableIslandLayer::new
        );
    }

    @Override
    public void Compile() {
        State.Compile();
    }

    @Override
    public boolean IsCompiled() {
        return State.IsCompiled();
    }
}

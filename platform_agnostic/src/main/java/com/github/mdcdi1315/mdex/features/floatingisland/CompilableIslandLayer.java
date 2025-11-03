package com.github.mdcdi1315.mdex.features.floatingisland;

import com.github.mdcdi1315.basemodslib.codecs.CodecUtils;

import com.github.mdcdi1315.mdex.dco_logic.Compilable;
import com.github.mdcdi1315.mdex.util.CompilableBlockState;

import com.mojang.serialization.Codec;

public class CompilableIslandLayer
    implements Compilable
{
    public CompilableBlockState State;
    public byte Size;

    public CompilableIslandLayer(CompilableBlockState state , byte size)
    {
        State = state;
        Size = size;
    }

    public static Codec<CompilableIslandLayer> GetCodec()
    {
        return CodecUtils.CreateCodecDirect(
                CompilableBlockState.GetCodec().fieldOf("state").forGetter((p) -> p.State),
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

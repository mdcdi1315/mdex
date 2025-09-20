package com.github.mdcdi1315.mdex.util;

import com.github.mdcdi1315.mdex.codecs.CodecUtils;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;

public class SingleTargetBlockState
    implements Compilable
{
    private static Codec<SingleTargetBlockState> codec;

    public RuleTest Target;
    public CompilableBlockState State;

    public SingleTargetBlockState(RuleTest target , CompilableBlockState state)
    {
        Target = target;
        State = state;
    }

    public static Codec<SingleTargetBlockState> GetCodec()
    {
        if (codec == null)
        {
            codec = CodecUtils.CreateCodecDirect(
                    RuleTest.CODEC.fieldOf("target").forGetter(bs -> bs.Target),
                    CompilableBlockState.GetCodec().fieldOf("state").forGetter(bs -> bs.State),
                    SingleTargetBlockState::new
            );
        }
        return codec;
    }

    public void Compile() {
        State.Compile();
    }

    @Override
    public boolean IsCompiled() {
        return State.IsCompiled();
    }
}

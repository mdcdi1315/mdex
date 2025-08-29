package com.github.mdcdi1315.mdex.util;

import com.mojang.serialization.Codec;
import com.github.mdcdi1315.mdex.codecs.CodecUtils;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;

public class SingleBlockState
    implements Compilable
{
    private static Codec<SingleBlockState> codec;

    public RuleTest Target;
    public CompilableTargetBlockState State;

    public SingleBlockState(RuleTest target , CompilableTargetBlockState state)
    {
        Target = target;
        State = state;
    }

    public static Codec<SingleBlockState> GetCodec()
    {
        if (codec == null)
        {
            codec = CodecUtils.CreateCodecDirect(
                    RuleTest.CODEC.fieldOf("target").forGetter((SingleBlockState bs) -> bs.Target),
                    CompilableTargetBlockState.GetCodec().fieldOf("state").forGetter((SingleBlockState bs) -> bs.State),
                    SingleBlockState::new
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

package com.github.mdcdi1315.mdex.util;

import com.github.mdcdi1315.mdex.dco_logic.Compilable;

import com.github.mdcdi1315.basemodslib.codecs.CodecUtils;
import com.github.mdcdi1315.basemodslib.codecs.StrictListCodec;
import com.github.mdcdi1315.mdex.structures.AbstractModdedRuleTest;

import com.mojang.serialization.Codec;

import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;

import java.util.List;

public final class SingleTargetBlockState
    implements Compilable
{
    private static Codec<SingleTargetBlockState> codec;
    private static Codec<List<SingleTargetBlockState>> list_codec;

    public RuleTest Target;
    public CompilableBlockState State;
    private boolean compiled;

    public SingleTargetBlockState(RuleTest target , CompilableBlockState state)
    {
        Target = target;
        State = state;
        compiled = false;
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

    public static Codec<List<SingleTargetBlockState>> GetListCodec()
    {
        if (list_codec == null) {
            // Enforce strict parsing for lists
            list_codec = new StrictListCodec<>(GetCodec());
        }
        return list_codec;
    }

    public void Compile() {
        State.Compile();
        if (State.IsCompiled())
        {
            if (Target instanceof AbstractModdedRuleTest amrt) {
                amrt.Compile();
                compiled = amrt.IsCompiled();
            } else {
                compiled = true;
            }
        }
    }

    @Override
    public boolean IsCompiled() {
        return compiled;
    }
}

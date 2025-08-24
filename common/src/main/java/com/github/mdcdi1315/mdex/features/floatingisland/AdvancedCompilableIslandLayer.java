package com.github.mdcdi1315.mdex.features.floatingisland;

import com.mojang.serialization.Codec;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;

import com.github.mdcdi1315.mdex.util.Compilable;
import com.github.mdcdi1315.mdex.codecs.CodecUtils;
import com.github.mdcdi1315.mdex.block.blockstateproviders.AbstractBlockStateProvider;

public class AdvancedCompilableIslandLayer
    implements Compilable
{
    public AbstractBlockStateProvider Provider;
    public IntProvider Randomized_X , Randomized_Z;

    public static Codec<AdvancedCompilableIslandLayer> GetCodec()
    {
        Codec<IntProvider> WIDTH_RANGE = IntProvider.codec(1 , 24);
        return CodecUtils.CreateCodecDirect(
                AbstractBlockStateProvider.CODEC.fieldOf("state_provider").forGetter((AdvancedCompilableIslandLayer d) -> d.Provider),
                WIDTH_RANGE.optionalFieldOf("width_x" , ConstantInt.of(5)).forGetter((AdvancedCompilableIslandLayer d) -> d.Randomized_X),
                WIDTH_RANGE.optionalFieldOf("width_z" , ConstantInt.of(5)).forGetter((AdvancedCompilableIslandLayer d) -> d.Randomized_Z),
                AdvancedCompilableIslandLayer::new
        );
    }

    public AdvancedCompilableIslandLayer(AbstractBlockStateProvider p , IntProvider x , IntProvider z)
    {
        Provider = p;
        Randomized_X = x;
        Randomized_Z = z;
    }

    @Override
    public void Compile() {
        Provider.Compile();
    }

    @Override
    public boolean IsCompiled() {
        return Provider.IsCompiled();
    }
}

package com.github.mdcdi1315.mdex.block.blockstateproviders;

import com.github.mdcdi1315.DotNetLayer.System.InvalidOperationException;

import com.github.mdcdi1315.basemodslib.codecs.CodecUtils;

import com.github.mdcdi1315.basemodslib.utils.random.weighted.WeightedList;
import com.github.mdcdi1315.basemodslib.utils.random.weighted.IWeightedEntry;
import com.github.mdcdi1315.basemodslib.utils.random.weighted.WeightedRandomUtils;

import com.github.mdcdi1315.mdex.dco_logic.DCOUtils;
import com.github.mdcdi1315.mdex.dco_logic.Compilable;
import com.github.mdcdi1315.mdex.util.CompilableBlockState;

import com.mojang.serialization.Codec;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public final class WeightedStateProvider
    extends AbstractBlockStateProvider
{
    public static final class CompilableWeightedEntry
        implements IWeightedEntry, Compilable
    {
        private final int weight;
        private final CompilableBlockState state;

        public CompilableWeightedEntry(int weight, CompilableBlockState state)
        {
            this.state = state;
            this.weight = weight;
        }

        public static Codec<CompilableWeightedEntry> GetCodec()
        {
            return CodecUtils.CreateCodecDirect(
                    WeightedRandomUtils.GetRecommendedRecordFieldConfig(),
                    CompilableBlockState.GetMapCodec().forGetter((g) -> g.state),
                    CompilableWeightedEntry::new
            );
        }

        @Override
        public int GetWeight() { return weight; }

        @Override
        public void Compile() { state.Compile(); }

        public BlockState GetState() { return state.BlockState; }

        @Override
        public boolean IsCompiled() { return state.IsCompiled(); }
    }

    public WeightedList<CompilableWeightedEntry> States;

    public WeightedStateProvider(WeightedList<CompilableWeightedEntry> states) { States = states; }

    @Override
    public BlockState GetBlockState(BlockStateProviderContext context)
    {
        Optional<CompilableWeightedEntry> e = WeightedRandomUtils.PickRandomElement(context.source(), States, States.GetTotalWeight());
        if (e.isPresent()) {
            return e.get().GetState();
        } else {
            throw new InvalidOperationException("Cannot find a block state to use!");
        }
    }

    @Override
    public AbstractBlockStateProviderType<?> GetType() { return WeightedStateProviderType.INSTANCE; }

    @Override
    protected boolean CompileImplementation()
    {
        boolean ret = DCOUtils.CompileAllOrFail(States);
        if (!ret) { States = null; }
        return ret;
    }
}

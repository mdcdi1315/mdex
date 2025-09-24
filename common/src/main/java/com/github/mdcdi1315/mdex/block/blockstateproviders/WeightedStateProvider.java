package com.github.mdcdi1315.mdex.block.blockstateproviders;

import com.github.mdcdi1315.mdex.util.CompilableBlockState;
import com.github.mdcdi1315.mdex.util.weight.SimpleWeightedEntryList;

import com.github.mdcdi1315.DotNetLayer.System.InvalidOperationException;

import com.github.mdcdi1315.mdex.util.weight.SimpleWeightedEntryList_Entry;
import com.mojang.serialization.DataResult;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public final class WeightedStateProvider
    extends AbstractBlockStateProvider
{
   public SimpleWeightedEntryList<CompilableBlockState> States;

   public static DataResult<WeightedStateProvider> create(SimpleWeightedEntryList<CompilableBlockState> weightedList)
   {
      return weightedList.IsEmpty() ?
              DataResult.error(() -> "Supplied an WeightedStateProvider which does not have any valid states.") :
              DataResult.success(new WeightedStateProvider(weightedList));
   }

   public WeightedStateProvider(SimpleWeightedEntryList<CompilableBlockState> list)
   {
       States = list;
   }

    private static BlockState Mapper(SimpleWeightedEntryList_Entry<CompilableBlockState> s) {
        return s.getValue().BlockState;
    }

    @Override
    public BlockState GetBlockState(BlockStateProviderContext context) {
        Optional<SimpleWeightedEntryList_Entry<CompilableBlockState>> obs = States.GetRandom(context.source());
        return obs.map(WeightedStateProvider::Mapper).orElseThrow(() -> new InvalidOperationException("Cannot find a block state to use!"));
    }

    @Override
    public AbstractBlockStateProviderType<?> GetType() {
        return WeightedStateProviderType.INSTANCE;
    }

    @Override
    protected boolean CompileImplementation()
    {
        for (var s : States)
        {
            var cs = s.getValue();
            cs.Compile();
            if (!cs.IsCompiled()) {
                States = null;
                return false;
            }
        }
        return true;
    }
}

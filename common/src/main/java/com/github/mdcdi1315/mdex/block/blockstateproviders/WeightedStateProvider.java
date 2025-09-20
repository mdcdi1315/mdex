package com.github.mdcdi1315.mdex.block.blockstateproviders;

import com.github.mdcdi1315.mdex.util.CompilableBlockState;
import com.github.mdcdi1315.mdex.util.weight.SimpleWeightedEntryList;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import com.mojang.serialization.DataResult;
import net.minecraft.world.level.block.state.BlockState;

public class WeightedStateProvider
        extends AbstractBlockStateProvider
{
   public final SimpleWeightedEntryList<CompilableBlockState> weightedList;
   private boolean compiled;

   public static DataResult<WeightedStateProvider> create(SimpleWeightedEntryList<CompilableBlockState> weightedList)
   {
      return weightedList.IsEmpty() ?
              DataResult.error(() -> "Supplied an WeightedStateProvider which does not have any valid states.") :
              DataResult.success(new WeightedStateProvider(weightedList));
   }

   public WeightedStateProvider(SimpleWeightedEntryList<CompilableBlockState> weightedList) {
      this.weightedList = weightedList;
      compiled = false;
   }

   @Override
   protected AbstractBlockStateProviderType<?> type() {
      return CustomBlockStateProviderRegistrySubsystem.WEIGHTED_STATE_PROVIDER;
   }

   public BlockState getState(RandomSource random, BlockPos pos) {
      return this.weightedList.GetRandom(random).orElseThrow(IllegalStateException::new).getValue().BlockState;
   }

   @Override
   public void Compile() {
      CompilableBlockState s;
      for (var i : weightedList.GetUnderlying())
      {
         (s = i.getValue()).Compile();
         if (s.IsCompiled() == false)
         {
            compiled = false;
            return;
         }
      }
      compiled = true;
   }

   @Override
   public boolean IsCompiled() {
      return compiled;
   }
}

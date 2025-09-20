package com.github.mdcdi1315.mdex.block.blockstateproviders;

import com.github.mdcdi1315.mdex.util.CompilableBlockState;

import com.mojang.serialization.DataResult;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.level.block.state.BlockState;

public class WeightedStateProvider
        extends AbstractBlockStateProvider
{
   public final SimpleWeightedRandomList<CompilableBlockState> weightedList;
   private boolean compiled;

   public static DataResult<WeightedStateProvider> create(SimpleWeightedRandomList<CompilableBlockState> weightedList)
   {
      return weightedList.isEmpty() ?
              DataResult.error(() -> "Supplied an WeightedStateProvider which does not have any valid states.") :
              DataResult.success(new WeightedStateProvider(weightedList));
   }

   public WeightedStateProvider(SimpleWeightedRandomList<CompilableBlockState> weightedList) {
      this.weightedList = weightedList;
      compiled = false;
   }

   public WeightedStateProvider(SimpleWeightedRandomList.Builder<CompilableBlockState> builder) {
      this(builder.build());
   }

   @Override
   protected AbstractBlockStateProviderType<?> type() {
      return CustomBlockStateProviderRegistrySubsystem.WEIGHTED_STATE_PROVIDER;
   }

   public BlockState getState(RandomSource random, BlockPos pos) {
      return this.weightedList.getRandomValue(random).orElseThrow(IllegalStateException::new).BlockState;
   }

   @Override
   public void Compile() {
      CompilableBlockState s;
      for (var i : weightedList.unwrap())
      {
         (s = i.getData()).Compile();
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

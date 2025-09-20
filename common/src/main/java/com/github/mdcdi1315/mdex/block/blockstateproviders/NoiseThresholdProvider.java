package com.github.mdcdi1315.mdex.block.blockstateproviders;

import com.github.mdcdi1315.mdex.util.Extensions;
import com.github.mdcdi1315.mdex.util.CompilableBlockState;

import net.minecraft.core.Holder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

import java.util.List;

public class NoiseThresholdProvider
        extends NoiseBasedStateProvider
{
   private boolean compiled;
   public final float threshold;
   public final float highChance;
   public final CompilableBlockState defaultState;
   public final List<CompilableBlockState> lowStates;
   public final List<CompilableBlockState> highStates;

   public NoiseThresholdProvider(long seed, Holder<NormalNoise.NoiseParameters> parameters, float scale, float threshold, float highChance, CompilableBlockState defaultState, List<CompilableBlockState> lowStates, List<CompilableBlockState> highStates)
   {
      super(seed, parameters, scale);
      this.threshold = threshold;
      this.highChance = highChance;
      this.defaultState = defaultState;
      this.lowStates = lowStates;
      this.highStates = highStates;
      compiled = false;
   }

   @Override
   protected AbstractBlockStateProviderType<?> type() {
      return CustomBlockStateProviderRegistrySubsystem.NOISE_THRESHOLD_PROVIDER;
   }

   public BlockState getState(RandomSource random, BlockPos pos) {
      if (this.getNoiseValue(pos, this.scale) < (double)this.threshold) {
          return Extensions.SelectRandomFromList(lowStates , random).BlockState;
      } else {
          return random.nextFloat() < this.highChance ? Extensions.SelectRandomFromList(this.highStates, random).BlockState : this.defaultState.BlockState;
      }
   }

   @Override
   public void Compile()
   {
      defaultState.Compile();
      compiled = defaultState.IsCompiled();
      if (compiled == false)
      {
         return;
      }
      for (var i : lowStates)
      {
         i.Compile();
         if (i.IsCompiled() == false)
         {
            compiled = false;
            return;
         }
      }
      for (var i : highStates)
      {
         i.Compile();
         if (i.IsCompiled() == false)
         {
            compiled = false;
            return;
         }
      }
   }

   @Override
   public boolean IsCompiled() {
      return compiled;
   }
}

package com.github.mdcdi1315.mdex.block.blockstateproviders;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import com.github.mdcdi1315.mdex.util.CompilableTargetBlockState;

import java.util.List;

public class NoiseThresholdProvider
        extends NoiseBasedStateProvider
{
   private boolean compiled;
   public final float threshold;
   public final float highChance;
   public final CompilableTargetBlockState defaultState;
   public final List<CompilableTargetBlockState> lowStates;
   public final List<CompilableTargetBlockState> highStates;

   public NoiseThresholdProvider(long seed, Holder<NormalNoise.NoiseParameters> parameters, float scale, float threshold, float highChance, CompilableTargetBlockState defaultState, List<CompilableTargetBlockState> lowStates, List<CompilableTargetBlockState> highStates)
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
      double d0 = this.getNoiseValue(pos, this.scale);
      if (d0 < (double)this.threshold) {
         return Util.getRandom(lowStates, random).BlockState;
      } else {
         return random.nextFloat() < this.highChance ? Util.getRandom(this.highStates, random).BlockState : this.defaultState.BlockState;
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
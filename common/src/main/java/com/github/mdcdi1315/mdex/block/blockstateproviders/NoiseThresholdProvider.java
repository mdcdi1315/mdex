package com.github.mdcdi1315.mdex.block.blockstateproviders;

import com.github.mdcdi1315.mdex.util.Extensions;
import com.github.mdcdi1315.mdex.util.CompilableBlockState;

import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public final class NoiseThresholdProvider
        extends AbstractNoiseStateProvider
{
   public final float Threshold;
   public final float HighChance;
   public CompilableBlockState DefaultState;
   public List<CompilableBlockState> LowStates;
   public List<CompilableBlockState> HighStates;

   public NoiseThresholdProvider(NoiseStateProviderData data, float threshold, float highChance, CompilableBlockState defaultState, List<CompilableBlockState> lowStates, List<CompilableBlockState> highStates)
   {
      super(data);
      Threshold = threshold;
      HighChance = highChance;
      DefaultState = defaultState;
      LowStates = lowStates;
      HighStates = highStates;
   }

    @Override
    public BlockState GetBlockState(BlockStateProviderContext context) {
        var random = context.source();
        if (this.GetNoiseValue(random , context.position(), Data.scale()) < Threshold) {
            return Extensions.SelectRandomFromList(LowStates , random).BlockState;
        } else {
            return (random.nextFloat() < HighChance ? Extensions.SelectRandomFromList(this.HighStates, random) : DefaultState).BlockState;
        }
    }

    @Override
    public AbstractBlockStateProviderType<?> GetType() {
        return NoiseThresholdProviderType.INSTANCE;
    }

    @Override
    protected boolean CompileImplementation() {
       if (Extensions.CompileAllOrFail(LowStates))
       {
           if (Extensions.CompileAllOrFail(HighStates))
           {
               DefaultState.Compile();
               boolean c = DefaultState.IsCompiled();
               if (!c) { DefaultState = null; }
               return c;
           } else {
               HighStates.clear();
               HighStates = null;
           }
       } else {
           LowStates.clear();
           LowStates = null;
       }
        return false;
    }
}
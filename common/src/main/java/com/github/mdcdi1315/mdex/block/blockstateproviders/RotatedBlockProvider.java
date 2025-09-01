package com.github.mdcdi1315.mdex.block.blockstateproviders;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.RotatedPillarBlock;
import com.github.mdcdi1315.mdex.util.CompilableTargetBlockState;

public class RotatedBlockProvider
        extends AbstractBlockStateProvider
{
   public final CompilableTargetBlockState block;

   public RotatedBlockProvider(CompilableTargetBlockState block) {
      this.block = block;
   }

   @Override
   protected AbstractBlockStateProviderType<?> type() {
      return CustomBlockStateProviderRegistrySubsystem.ROTATED_BLOCK_PROVIDER;
   }

   public BlockState getState(RandomSource random, BlockPos pos) {
      Direction.Axis axis = Direction.Axis.getRandom(random);
      // Use the desired block state that the user wants to, but set the axis property right after all the properties are defined in the provider itself
      return this.block.BlockState.setValue(RotatedPillarBlock.AXIS, axis);
   }

   @Override
   public void Compile() {
      block.Compile();
   }

   @Override
   public boolean IsCompiled() {
      return block.IsCompiled();
   }
}
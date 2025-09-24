package com.github.mdcdi1315.mdex.block.blockstateproviders;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public record BlockStateIterationResult<TPos extends BlockPos>(BlockState state , TPos position) { }
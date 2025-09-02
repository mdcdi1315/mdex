package com.github.mdcdi1315.mdex.block;

import com.github.mdcdi1315.mdex.MDEXBalmLayer;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicateType;

import java.util.List;
import java.util.Optional;

public final class ModdedBlockMatchesBlockPredicate
    extends AbstractModdedBlockPredicate
{
    public ResourceLocation BlockID;
    private Block blockdef;

    public ModdedBlockMatchesBlockPredicate(List<String> modids , ResourceLocation blockid)
    {
        super(modids);
        BlockID = blockid;
        blockdef = null;
    }

    @Override
    public BlockPredicateType<ModdedBlockMatchesBlockPredicate> type() {
        return ModdedBlockPredicateType.INSTANCE;
    }

    @Override
    public void Compile() {
        Optional<Block> b = BuiltInRegistries.BLOCK.getOptional(BlockID);
        if (b.isEmpty()) {
            MDEXBalmLayer.LOGGER.error("ERROR: Cannot get the block for the block ID {}." , BlockID);
            Invalidate();
        } else {
            blockdef = b.get();
        }
        BlockID = null;
    }

    @Override
    public boolean Test(WorldGenLevel lvl, BlockPos pos)
    {
        return lvl.getBlockState(pos).is(blockdef);
    }
}
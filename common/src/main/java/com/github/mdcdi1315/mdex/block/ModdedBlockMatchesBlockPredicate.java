package com.github.mdcdi1315.mdex.block;


import com.github.mdcdi1315.mdex.util.BlockNotFoundException;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.WorldGenLevel;
import com.github.mdcdi1315.mdex.MDEXBalmLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicateType;

import java.util.List;

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
    public boolean test(WorldGenLevel lvl, BlockPos pos)
    {
        if (!getModIdListIsValid()) {
            return false;
        }
        if (blockdef == null)
        {
            try {
                blockdef = BlockUtils.GetBlockFromID(BlockID);
            } catch (BlockNotFoundException e) {
                MDEXBalmLayer.LOGGER.error("ERROR: Cannot get the block for the block ID {}." , BlockID.toDebugFileName());
                return false;
            } finally {
                BlockID = null;
            }
        }
        return lvl.getBlockState(pos).getBlock().equals(blockdef);
    }
}

package com.github.mdcdi1315.mdex.structures;

import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.MaybeNull;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import com.github.mdcdi1315.mdex.util.BlockIdOrBlockTagEntry;
import com.github.mdcdi1315.mdex.util.ConcatenatingHolderSet;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;

import java.util.List;

public final class ModdedProtectedBlocksProcessor
    extends AbstractModdedStructureProcessor
{
    public List<BlockIdOrBlockTagEntry> entries;
    private ConcatenatingHolderSet<Block> compiled;

    public ModdedProtectedBlocksProcessor(List<String> modids , List<BlockIdOrBlockTagEntry> entries)
    {
        super(modids);
        this.entries = entries;
    }

    @Override
    protected void CompileData() {
        compiled = new ConcatenatingHolderSet<>();
        for (var i : entries)
        {
            compiled.Add(i.GetBlocks());
            // Allow the runtime to quickly reclaim mem if it needs it.
            i.Invalidate();
        }
        entries = null;
        // Trash entries that are unresolvable and optimize then the capacity of the list
        compiled.Optimize();
    }

    @Override
    protected @MaybeNull StructureTemplate.StructureBlockInfo ProcessModdedBlock(LevelReader level, BlockPos offset, BlockPos pos, StructureTemplate.StructureBlockInfo blockInfo, StructureTemplate.StructureBlockInfo relativeBlockInfo, StructurePlaceSettings settings)
    {
        var bs = level.getBlockState(relativeBlockInfo.pos());
        for (var b : compiled)
        {
            if (bs.is(b.value())) {
                // This is a protected block, thus this position should not be replaced by the structure
                return null;
            }
        }
        return relativeBlockInfo;
    }

    @Override
    protected AbstractModdedStructureProcessorType<?> GetType() {
        return ModdedProtectedBlocksProcessorType.INSTANCE;
    }
}

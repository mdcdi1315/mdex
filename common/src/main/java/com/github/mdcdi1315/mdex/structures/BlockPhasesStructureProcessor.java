package com.github.mdcdi1315.mdex.structures;

import com.github.mdcdi1315.DotNetLayer.System.ArgumentException;

import com.github.mdcdi1315.mdex.block.BlockUtils;
import com.github.mdcdi1315.mdex.block.blockstateproviders.AbstractBlockStateProvider;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;

import java.util.List;

public final class BlockPhasesStructureProcessor
    extends AbstractModdedStructureProcessor
{
    public List<AbstractBlockStateProvider> States;
    public float probability;

    public BlockPhasesStructureProcessor(List<String> modids , List<AbstractBlockStateProvider> states , float p)
    {
        super(modids);
        States = states;
        probability = p;
    }

    @Override
    protected StructureTemplate.StructureBlockInfo ProcessModdedBlock(LevelReader level, BlockPos offset, BlockPos pos, StructureTemplate.StructureBlockInfo blockInfo, StructureTemplate.StructureBlockInfo relativeBlockInfo, StructurePlaceSettings settings)
    {
        BlockPos rbipos = relativeBlockInfo.pos();
        var rs = settings.getRandom(rbipos);
        if (rs.nextFloat() < probability)
        {
            var bs = level.getBlockState(rbipos);
            int s = States.size() - 1;
            for (int I = 0; I < s; I++)
            {
                if (BlockUtils.BlockStatesMatch(States.get(I).GetBlockState(level, rs , rbipos) , bs)) {
                    // Roll the next state provider from the list, and return that instead
                    return new StructureTemplate.StructureBlockInfo(rbipos , States.get(I+1).GetBlockState(level, rs , rbipos) , null);
                }
            }
        }
        return relativeBlockInfo;
    }

    @Override
    protected void CompileData() {
        if (States.size() < 2) {
            throw new ArgumentException("States provided must be at least two.");
        }
        for (var i : States)
        {
            i.Compile();
            if (!i.IsCompiled()) {
                this.MarkAsInvalid();
                break;
            }
        }
    }

    @Override
    protected AbstractModdedStructureProcessorType<?> GetType() {
        return BlockPhasesStructureProcessorType.INSTANCE;
    }
}

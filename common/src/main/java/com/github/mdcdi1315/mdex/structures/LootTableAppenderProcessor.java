package com.github.mdcdi1315.mdex.structures;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.resources.ResourceLocation;
import com.github.mdcdi1315.mdex.block.BlockUtils;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;

import java.util.List;

public final class LootTableAppenderProcessor
    extends AbstractModdedStructureProcessor
{
    public ResourceLocation ContainerBlockID;
    public Block ContainerBlock;
    public ResourceLocation LootTable;
    public float Probability; // Probability of 1 always appends the specified loot table. Optional in the codec and there defaults to 1.

    public LootTableAppenderProcessor(List<String> modids , ResourceLocation containerid , ResourceLocation lt , float ltprop)
    {
        super(modids);
        ContainerBlockID = containerid;
        LootTable = lt;
        Probability = ltprop;
    }

    @Override
    protected void CompileData() {
        ContainerBlock = BlockUtils.GetBlockFromID(ContainerBlockID);
        ContainerBlockID = null;
    }

    @Override
    protected StructureTemplate.StructureBlockInfo ProcessModdedBlock(LevelReader level, BlockPos offset, BlockPos pos, StructureTemplate.StructureBlockInfo blockInfo, StructureTemplate.StructureBlockInfo relativeBlockInfo, StructurePlaceSettings settings)
    {
        BlockPos rbipos = relativeBlockInfo.pos();
        RandomSource rs = settings.getRandom(rbipos);
        if (rs.nextFloat() < Probability)
        {
            BlockState bs;
            if ((bs = level.getBlockState(rbipos)).is(ContainerBlock))
            {
                BlockEntity ent = level.getBlockEntity(rbipos);
                if (ent instanceof RandomizableContainerBlockEntity rdcbe) {
                    rdcbe.setLootTable(LootTable , rs.nextLong());
                    return new StructureTemplate.StructureBlockInfo(rbipos , bs , rdcbe.saveWithFullMetadata());
                } else {
                    return relativeBlockInfo;
                }
            }
        }
        // Block remains as is already
        return relativeBlockInfo;
    }

    @Override
    protected AbstractModdedStructureProcessorType<?> GetType() {
        return LootTableAppenderProcessorType.INSTANCE;
    }
}

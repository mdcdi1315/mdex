package com.github.mdcdi1315.mdex.structures;

import com.github.mdcdi1315.mdex.MDEXBalmLayer;
import com.github.mdcdi1315.mdex.block.BlockUtils;

import com.github.mdcdi1315.mdex.util.ItemStackChestPlacement;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.List;

public final class SpecificLootAppenderProcessor
    extends AbstractModdedStructureProcessor
{
    public ResourceLocation ContainerBlockID;
    public Block ContainerBlock;
    public List<ItemStackChestPlacement> ItemStacks;
    public float Probability; // Probability of 1 always appends the specified loot. Optional in the codec and there defaults to 1.

    public SpecificLootAppenderProcessor(List<String> modids, ResourceLocation containerid , List<ItemStackChestPlacement> stacks , float p)
    {
        super(modids);
        ContainerBlockID = containerid;
        ItemStacks = stacks;
        Probability = p;
    }

    @Override
    protected StructureTemplate.StructureBlockInfo processModdedBlock(LevelReader level, BlockPos offset, BlockPos pos, StructureTemplate.StructureBlockInfo blockInfo, StructureTemplate.StructureBlockInfo relativeBlockInfo, StructurePlaceSettings settings) {
        BlockPos rbipos = relativeBlockInfo.pos();
        RandomSource rs = settings.getRandom(rbipos);
        if (rs.nextFloat() < Probability)
        {
            BlockState bs;
            if ((bs = level.getBlockState(rbipos)).is(ContainerBlock)) {
                BlockEntity ent = level.getBlockEntity(rbipos);
                if (ent instanceof RandomizableContainerBlockEntity rdcbe) {
                    for (var c : ItemStacks) {
                        if (c.ShouldBeSelected(rs)) {
                            var pi = c.GetPlacementIndex();
                            if (pi < rdcbe.getContainerSize()) {
                                rdcbe.setItem(pi, c.GetStack());
                            } else {
                                MDEXBalmLayer.LOGGER.warn("SpecificLootAppenderProcessor: Cannot apply item stack because it's placement index into the container was invalid. Got index {} while the size of the container is {}.", pi, rdcbe.getContainerSize());
                            }
                        }
                    }
                    return new StructureTemplate.StructureBlockInfo(rbipos, bs, rdcbe.saveWithFullMetadata(level.registryAccess()));
                } else {
                    return relativeBlockInfo;
                }
            }
        }
        // Block remains as is already
        return relativeBlockInfo;
    }

    @Override
    protected void compileData() {
        ContainerBlock = BlockUtils.GetBlockFromID(ContainerBlockID);
        ContainerBlockID = null;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return SpecificLootAppenderProcessorType.INSTANCE;
    }
}

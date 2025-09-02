package com.github.mdcdi1315.mdex.structures;

import com.github.mdcdi1315.mdex.codecs.CodecUtils;
import com.github.mdcdi1315.mdex.codecs.StableItemStackCodec;
import com.github.mdcdi1315.mdex.util.ItemStackChestPlacement;

import com.mojang.serialization.MapCodec;

import net.minecraft.resources.ResourceLocation;

public final class SpecificLootAppenderProcessorType
    extends AbstractModdedStructureProcessorType<SpecificLootAppenderProcessor>
{
    public static SpecificLootAppenderProcessorType INSTANCE = new SpecificLootAppenderProcessorType();

    @Override
    protected MapCodec<SpecificLootAppenderProcessor> GetCodecInstance() {
        return CodecUtils.CreateMapCodecDirect(
                GetBaseCodec(),
                ResourceLocation.CODEC.fieldOf("containerid").forGetter((SpecificLootAppenderProcessor p) -> p.ContainerBlockID),
                ItemStackChestPlacement.CreateCodec(StableItemStackCodec.INSTANCE).listOf().fieldOf("stacks").forGetter((SpecificLootAppenderProcessor p) -> p.ItemStacks),
                CodecUtils.FLOAT_PROBABILITY.optionalFieldOf("probability" , 1f).forGetter((SpecificLootAppenderProcessor p) -> p.Probability),
                SpecificLootAppenderProcessor::new
        );
    }
}

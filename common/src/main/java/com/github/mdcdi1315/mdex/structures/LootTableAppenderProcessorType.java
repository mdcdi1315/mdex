package com.github.mdcdi1315.mdex.structures;

import com.github.mdcdi1315.mdex.codecs.CodecUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.resources.ResourceLocation;

public final class LootTableAppenderProcessorType
    extends AbstractModdedStructureProcessorType<LootTableAppenderProcessor>
{
    public static LootTableAppenderProcessorType INSTANCE = new LootTableAppenderProcessorType();

    @Override
    protected MapCodec<LootTableAppenderProcessor> GetCodecInstance()
    {
        return CodecUtils.CreateMapCodecDirect(
                GetBaseCodec(),
                ResourceLocation.CODEC.fieldOf("containerid").forGetter((LootTableAppenderProcessor p) -> p.ContainerBlockID),
                ResourceLocation.CODEC.fieldOf("loot_table").forGetter((LootTableAppenderProcessor p) -> p.LootTable),
                Codec.floatRange(0f , 1f).optionalFieldOf("probability" , 1f).forGetter((LootTableAppenderProcessor p) -> p.Probability),
                LootTableAppenderProcessor::new
        );
    }
}

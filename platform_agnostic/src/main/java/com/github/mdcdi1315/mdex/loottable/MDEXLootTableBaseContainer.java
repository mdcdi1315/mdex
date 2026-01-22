package com.github.mdcdi1315.mdex.loottable;

import com.github.mdcdi1315.basemodslib.codecs.ListCodec;
import com.github.mdcdi1315.basemodslib.codecs.CodecUtils;

import com.github.mdcdi1315.mdex.util.weight.Weight;

import com.mojang.datafixers.Products;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;

import java.util.List;

public abstract class MDEXLootTableBaseContainer
    extends LootPoolEntryContainer
{
    public static final int DEFAULT_WEIGHT = 1;
    public static final int DEFAULT_QUALITY = 0;

    protected final int quality;
    protected final Weight weight;

    public MDEXLootTableBaseContainer(List<LootItemCondition> conditions, Weight weight, int quality)
    {
        super(conditions);
        this.weight = weight;
        this.quality = quality;
    }

    protected static <T extends MDEXLootTableBaseContainer> Products.P3<RecordCodecBuilder.Mu<T>, List<LootItemCondition>, Weight, Integer> CommonBaseEntryFields(RecordCodecBuilder.Instance<T> instance)
    {
        return instance.group(
                new ListCodec<>(LootItemCondition.DIRECT_CODEC).optionalFieldOf("conditions", List.of()).forGetter((T i) -> i.conditions),
                Weight.CODEC.optionalFieldOf("weight", Weight.ONE).forGetter((T i) -> i.weight),
                CodecUtils.ZERO_OR_POSITIVE_INTEGER.optionalFieldOf("quality" , DEFAULT_QUALITY).forGetter((T b) -> b.quality)
        );
    }
}

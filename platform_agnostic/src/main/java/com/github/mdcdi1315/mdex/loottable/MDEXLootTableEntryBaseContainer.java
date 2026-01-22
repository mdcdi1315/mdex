package com.github.mdcdi1315.mdex.loottable;

import com.github.mdcdi1315.basemodslib.codecs.ListCodec;
import com.github.mdcdi1315.DotNetLayer.System.StringUtils;

import com.github.mdcdi1315.mdex.util.weight.Weight;

import com.mojang.datafixers.Products;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.List;

public abstract class MDEXLootTableEntryBaseContainer
    extends MDEXLootTableBaseContainer
{
    protected final List<LootItemFunction> functions;

    protected MDEXLootTableEntryBaseContainer(List<LootItemCondition> conditions, Weight weight, int quality, List<LootItemFunction> functions)
    {
        super(conditions, weight, quality);
        this.functions = functions;
    }

    @Override
    public void validate(ValidationContext vc)
    {
        super.validate(vc);

        int I = 0;
        for (LootItemFunction f : functions) {
            f.validate(vc.forChild(StringUtils.Format(".functions[{0}]", I++)));
        }
    }

    protected static <T extends MDEXLootTableEntryBaseContainer> Products.P4<RecordCodecBuilder.Mu<T>, List<LootItemCondition>, Weight, Integer, List<LootItemFunction>> CommonEntryFields(RecordCodecBuilder.Instance<T> instance)
    {
        return CommonBaseEntryFields(instance).and(
                new ListCodec<>(LootItemFunctions.ROOT_CODEC).optionalFieldOf("functions", List.of()).forGetter((T i) -> i.functions)
        );
    }
}

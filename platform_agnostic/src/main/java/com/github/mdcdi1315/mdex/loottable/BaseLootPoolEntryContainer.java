package com.github.mdcdi1315.mdex.loottable;

import com.mojang.datafixers.Products;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntry;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.List;
import java.util.function.Consumer;

/**
 * Note: This class directly acts as a {@link net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer}. <br />
 * It calls canRun already before calling this {@link BaseLootPoolEntryContainer#expand(LootContext, Consumer)}.
 */
public abstract class BaseLootPoolEntryContainer
{
    public final List<LootItemCondition> conditions;

    public BaseLootPoolEntryContainer(List<LootItemCondition> conditions) {
        this.conditions = conditions;
    }

    protected static <T extends BaseLootPoolEntryContainer> Products.P1<RecordCodecBuilder.Mu<T>, List<LootItemCondition>> CommonFields(RecordCodecBuilder.Instance<T> instance) {
        return instance.group(LootItemCondition.DIRECT_CODEC.listOf().optionalFieldOf("conditions", List.of()).forGetter((p_298548_) -> p_298548_.conditions));
    }

    // falls to LootPoolEntryContainer
    public void validate(ValidationContext validationContext) {}

    // falls to LootPoolEntryContainer
    public abstract boolean expand(LootContext lootContext, Consumer<LootPoolEntry> consumer);
}
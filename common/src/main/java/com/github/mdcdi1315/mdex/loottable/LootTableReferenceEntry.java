package com.github.mdcdi1315.mdex.loottable;

import com.github.mdcdi1315.mdex.MDEXBalmLayer;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public final class LootTableReferenceEntry
    extends BaseSingletonLootPoolEntryContainer
{
    private ResourceLocation loottable;
    private ResourceKey<LootTable> resolved;

    LootTableReferenceEntry(int weight, int quality , List<LootItemCondition> c, List<LootItemFunction> functions , ResourceLocation table) {
        super(c, weight, quality, functions);
        loottable = table;
        resolved = ResourceKey.create(Registries.LOOT_TABLE, loottable);
    }

    @Override
    protected void createItemStack(Consumer<ItemStack> stackConsumer, LootContext lootContext) {
        Optional<Holder.Reference<LootTable>> loottable = lootContext.getResolver().get(Registries.LOOT_TABLE , resolved);
        loottable.ifPresent(lootTableReference -> lootTableReference.value().getRandomItemsRaw(lootContext, stackConsumer));
    }

    @Override
    public void validate(ValidationContext validationContext)
    {
        super.validate(validationContext);
        if (validationContext.hasVisitedElement(resolved)) {
            validationContext.reportProblem(String.format("Table %s is recursively called" , loottable));
        } else {
            super.validate(validationContext);
            validationContext.resolver().get(Registries.LOOT_TABLE , resolved).ifPresentOrElse(
                    ( lt) -> lt.value().validate(validationContext.enterElement("->{" + this.loottable + "}", resolved)),
                    () -> {
                        MDEXBalmLayer.LOGGER.warn("LootTableManager: Cannot find the loot table with ID '{}'. The table will not be loaded." , loottable);
                        loottable = null;
                        resolved = null;
                        Invalidate();
                    });
        }
    }

    public static MapCodec<LootTableReferenceEntry> GetCodec()
    {
        return RecordCodecBuilder.mapCodec(
                (RecordCodecBuilder.Instance<LootTableReferenceEntry> d) -> GetBaseCodec(d).and(
                        ResourceLocation.CODEC.fieldOf("name").forGetter((LootTableReferenceEntry e) -> e.loottable)
                ).apply(d , LootTableReferenceEntry::new)
        );
    }
}

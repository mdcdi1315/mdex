package com.github.mdcdi1315.mdex.loottable;

import com.github.mdcdi1315.mdex.MDEXBalmLayer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public final class LootTableItemEntry
    extends BaseSingletonLootPoolEntryContainer
{
    private Item item;

    LootTableItemEntry(int weight, int quality, List<LootItemFunction> functions , ResourceLocation location) {
        super(weight, quality, functions);
        Optional<Item> i = BuiltInRegistries.ITEM.getOptional(location);
        if (i.isEmpty()) {
            MDEXBalmLayer.LOGGER.warn("LootTableManager: Cannot find the item with ID '{}', this entry will not be loaded." , location);
            item = null;
            Invalidate();
        } else {
            item = i.get();
        }
    }

    public void createItemStack(Consumer<ItemStack> stackConsumer, LootContext lootContext) {
        stackConsumer.accept(new ItemStack(this.item));
    }

    public static Codec<LootTableItemEntry> GetCodec()
    {
        return RecordCodecBuilder.create(
                (RecordCodecBuilder.Instance<LootTableItemEntry> d) -> GetBaseCodec(d).and(
                        ResourceLocation.CODEC.fieldOf("name").forGetter((LootTableItemEntry e) -> null)
                ).apply(d , LootTableItemEntry::new)
        );
    }

}

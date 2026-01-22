package com.github.mdcdi1315.mdex.loottable;

import com.github.mdcdi1315.basemodslib.codecs.CodecUtils;
import com.github.mdcdi1315.basemodslib.utils.Extensions;

import com.github.mdcdi1315.mdex.util.weight.Weight;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntry;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public final class OptionalMDEXLootTableItemEntry
    extends MDEXLootTableEntryBaseContainer
{
    private final Item item;
    private final int random_count_bound;

    public OptionalMDEXLootTableItemEntry(List<LootItemCondition> conditions, Weight weight, int quality, List<LootItemFunction> functions, int additional_random_count, ResourceLocation item_location)
    {
        super(conditions, weight, quality, functions);
        random_count_bound = additional_random_count;
        Optional<Item> i = BuiltInRegistries.ITEM.getOptional(item_location);
        if (i.isEmpty()) { item = null; } else { item = i.get(); }
    }

    public static MapCodec<OptionalMDEXLootTableItemEntry> GetCodec()
    {
        return RecordCodecBuilder.mapCodec(
                (RecordCodecBuilder.Instance<OptionalMDEXLootTableItemEntry> e) -> CommonEntryFields(e).and(
                        CodecUtils.ZERO_OR_POSITIVE_INTEGER.optionalFieldOf("additional_random_count", 0).forGetter((OptionalMDEXLootTableItemEntry o) -> o.random_count_bound)
                ).and(
                        ResourceLocation.CODEC.fieldOf("name").forGetter((OptionalMDEXLootTableItemEntry g) -> BuiltInRegistries.ITEM.getKey(g.item))
                ).apply(e, OptionalMDEXLootTableItemEntry::new)
        );
    }

    private static final class LP_ENTRY
        implements LootPoolEntry
    {
        private final int rc;
        private final float wt;
        private final Item item;
        private final float quality;
        private final List<LootItemFunction> functions;

        public LP_ENTRY(Weight weight, Item item, int quality, int additional_random_count, List<LootItemFunction> functions)
        {
            this.item = item;
            wt = weight.getValue();
            this.quality = quality;
            this.functions = functions;
            rc = additional_random_count;
        }

        @Override
        public int getWeight(float luck) {
            return Extensions.Floor(wt + (luck * quality));
        }

        @Override
        public void createItemStack(Consumer<ItemStack> consumer, LootContext lc)
        {
            ItemStack stack = (rc == 0) ? new ItemStack(item, 1) : new ItemStack(item, lc.getRandom().nextInt(rc));
            for (LootItemFunction f : functions) { f.apply(stack, lc); }
            consumer.accept(stack);
        }
    }

    @Override
    public LootPoolEntryType getType() { return LootTableRegistrySubsystem.OPTIONAL_LOOT_TABLE_ITEM; }

    @Override
    public boolean expand(LootContext lc, Consumer<LootPoolEntry> consumer)
    {
        if (item == null) {
            return false;
        } else {
            consumer.accept(new LP_ENTRY(weight, item, quality, random_count_bound, functions));
            return true;
        }
    }
}

package com.github.mdcdi1315.mdex.loottable;

import com.github.mdcdi1315.basemodslib.utils.Extensions;

import com.github.mdcdi1315.mdex.MDEXModInstance;
import com.github.mdcdi1315.mdex.util.weight.Weight;

import com.google.common.collect.ImmutableList;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntry;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public final class OptionalMDEXLootTableEntry
    extends MDEXLootTableBaseContainer
{
    private ResourceKey<LootTable> loot_table;
    private Holder.Reference<LootTable> actual_object;

    public OptionalMDEXLootTableEntry(List<LootItemCondition> conditions, Weight weight, int quality, ResourceLocation loot_table) {
        super(conditions, weight, quality);
        actual_object = null;
        this.loot_table = ResourceKey.create(Registries.LOOT_TABLE, loot_table);
    }

    public static MapCodec<OptionalMDEXLootTableEntry> GetCodec()
    {
        return RecordCodecBuilder.mapCodec(
                (RecordCodecBuilder.Instance<OptionalMDEXLootTableEntry> o) -> CommonBaseEntryFields(o).and(
                        ResourceLocation.CODEC.fieldOf("name").forGetter((i) -> i.loot_table.location())
                ).apply(o, OptionalMDEXLootTableEntry::new)
        );
    }

    @Override
    public void validate(ValidationContext vc)
    {
        super.validate(vc);

        if (vc.hasVisitedElement(loot_table)) {
            vc.reportProblem(String.format("Table %s is recursively called" , loot_table));
        } else {
            Optional<Holder.Reference<LootTable>> lref = vc.resolver().get(loot_table);
            if (lref.isEmpty()) {
                loot_table = null;
                MDEXModInstance.LOGGER.warn("LootTableManager: Cannot find the loot table with ID '{}'. The table will not be loaded." , loot_table.location());
            } else {
                actual_object = lref.get();
            }
        }
    }

    private static final class LP_ENTRY
        implements LootPoolEntry
    {
        private final ItemStack stack;
        private final float weight, quality;

        public LP_ENTRY(ItemStack stack, Weight weight, int quality)
        {
            this.stack = stack;
            this.quality = quality;
            this.weight = weight.getValue();
        }

        @Override
        public int getWeight(float luck) { return Extensions.Floor(weight + (luck * quality)); }

        @Override
        public void createItemStack(Consumer<ItemStack> consumer, LootContext lc) { consumer.accept(stack); }
    }

    @Override
    public LootPoolEntryType getType() {
        return LootTableRegistrySubsystem.OPTIONAL_LOOT_TABLE;
    }

    @Override
    public boolean expand(LootContext lc, Consumer<LootPoolEntry> consumer)
    {
        if (actual_object == null) {
            return false;
        } else {
            ImmutableList.Builder<ItemStack> b = ImmutableList.builder();
            actual_object.value().getRandomItems(lc, b::add);
            for (ItemStack is : b.build()) {
                consumer.accept(new LP_ENTRY(is, weight, quality));
            }

            return true;
        }
    }
}

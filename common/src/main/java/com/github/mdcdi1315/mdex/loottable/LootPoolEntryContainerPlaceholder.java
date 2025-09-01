package com.github.mdcdi1315.mdex.loottable;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.DataResult;

import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntry;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;

import java.util.List;
import java.util.function.Consumer;

public final class LootPoolEntryContainerPlaceholder<TBase extends BaseLootPoolEntryContainer>
    extends LootPoolEntryContainer
{
    private final LootPoolEntryType entrytype;
    private final TBase container;

    public LootPoolEntryContainerPlaceholder(List<LootItemCondition> conditions , TBase container , MapCodec<TBase> codec) {
        super(conditions);
        entrytype = new LootPoolEntryType(codec.flatXmap((TBase b) -> DataResult.success(new LootPoolEntryContainerPlaceholder<>(conditions , b , codec)) , (LootPoolEntryContainerPlaceholder<TBase> b) -> DataResult.success(b.GetContainer())));
        this.container = container;
    }

    public TBase GetContainer() {
        return container;
    }

    @Override
    public void validate(ValidationContext validationContext) {
        super.validate(validationContext);
        container.validate(validationContext);
    }

    @Override
    public LootPoolEntryType getType() {
        return entrytype;
    }

    @Override
    public boolean expand(LootContext lootContext, Consumer<LootPoolEntry> consumer) {
        if (canRun(lootContext)) {
            return container.expand(lootContext , consumer);
        }
        return false;
    }
}

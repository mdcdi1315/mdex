package com.github.mdcdi1315.mdex.loottable;

import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntry;

import java.util.function.Consumer;

/**
 * Note: This class directly acts as a {@link net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer}. <br />
 * It calls canRun already before calling this {@link BaseLootPoolEntryContainer#expand(LootContext, Consumer)}.
 */
public abstract class BaseLootPoolEntryContainer
{
    // falls to LootPoolEntryContainer
    public void validate(ValidationContext validationContext) {}

    // falls to LootPoolEntryContainer
    public abstract boolean expand(LootContext lootContext, Consumer<LootPoolEntry> consumer);
}

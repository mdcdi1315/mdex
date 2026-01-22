package com.github.mdcdi1315.mdex.loottable;

import com.github.mdcdi1315.basemodslib.registries.IRegistryRegistrar;
import com.github.mdcdi1315.basemodslib.registries.IBulkRegistryObjectRegister;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;

public final class LootTableRegistrySubsystem
{
    public static LootPoolEntryType OPTIONAL_LOOT_TABLE;
    public static LootPoolEntryType OPTIONAL_LOOT_TABLE_ITEM;

    public static void Initialize(IRegistryRegistrar registries)
    {
        IBulkRegistryObjectRegister<LootPoolEntryType> t = registries.GetBulkRegister(Registries.LOOT_POOL_ENTRY_TYPE);
        t.Add("optional_loot_table_reference", OPTIONAL_LOOT_TABLE = new LootPoolEntryType(OptionalMDEXLootTableEntry.GetCodec()));
        t.Add("optional_item", OPTIONAL_LOOT_TABLE_ITEM = new LootPoolEntryType(OptionalMDEXLootTableItemEntry.GetCodec()));
    }
}
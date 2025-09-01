package com.github.mdcdi1315.mdex.loottable;

import com.github.mdcdi1315.DotNetLayer.System.ArgumentNullException;

import com.github.mdcdi1315.mdex.MDEXBalmLayer;

import net.blay09.mods.balm.api.BalmRegistries;

import com.mojang.serialization.Codec;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;

public final class LootTableRegistrySubsystem
{
    public static void Initialize(BalmRegistries registries)
    {
        Register(registries , "optional_loot_table_reference" , LootTableReferenceEntry.GetCodec());
        Register(registries , "optional_item" , LootTableItemEntry.GetCodec());
    }

    public static <LPEC extends BaseLootPoolEntryContainer> void Register(BalmRegistries registries , String name , Codec<LPEC> sercodec)
    {
        ArgumentNullException.ThrowIfNull(name , "name");
        Register(registries , MDEXBalmLayer.id(name) , sercodec);
    }

    public static <LPEC extends BaseLootPoolEntryContainer> void Register(BalmRegistries registries , ResourceLocation location , Codec<LPEC> sercodec)
    {
        ArgumentNullException.ThrowIfNull(registries , "registries");
        ArgumentNullException.ThrowIfNull(location , "location");
        ArgumentNullException.ThrowIfNull(sercodec , "sercodec");
        registries.register(BuiltInRegistries.LOOT_POOL_ENTRY_TYPE , (ResourceLocation rl) -> new LootPoolEntryType(new ByCodecLootPoolSerializer<>(sercodec)) , location);
    }
}

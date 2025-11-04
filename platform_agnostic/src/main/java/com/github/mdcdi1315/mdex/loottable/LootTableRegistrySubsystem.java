package com.github.mdcdi1315.mdex.loottable;

import com.github.mdcdi1315.DotNetLayer.System.ArgumentNullException;

import com.github.mdcdi1315.basemodslib.registries.IRegistryRegistrar;
import com.github.mdcdi1315.basemodslib.registries.RegistryObjectSupplier;

import com.mojang.serialization.Codec;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;

public final class LootTableRegistrySubsystem
{
    public static void Initialize(IRegistryRegistrar registries)
    {
        Register(registries , "optional_loot_table_reference" , LootTableReferenceEntry.GetCodec());
        Register(registries , "optional_item" , LootTableItemEntry.GetCodec());
    }

    public static <LPEC extends BaseLootPoolEntryContainer> void Register(IRegistryRegistrar registries , String location , Codec<LPEC> sercodec)
    {
        ArgumentNullException.ThrowIfNull(registries , "registries");
        ArgumentNullException.ThrowIfNull(location , "location");
        ArgumentNullException.ThrowIfNull(sercodec , "sercodec");
        registries.RegisterObject(Registries.LOOT_POOL_ENTRY_TYPE , location, new LootPoolEntryObject<>(sercodec));
    }

    private static final class LootPoolEntryObject<T extends BaseLootPoolEntryContainer>
        extends RegistryObjectSupplier<LootPoolEntryType>
    {
        private final Codec<T> codec;

        public LootPoolEntryObject(Codec<T> cdc) { codec = cdc; }

        @Override
        protected LootPoolEntryType Get(ResourceLocation resourceLocation) {
            return new LootPoolEntryType(new ByCodecLootPoolSerializer<>(codec));
        }
    }
}

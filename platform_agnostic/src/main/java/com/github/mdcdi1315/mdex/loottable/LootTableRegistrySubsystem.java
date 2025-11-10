package com.github.mdcdi1315.mdex.loottable;

import com.github.mdcdi1315.DotNetLayer.System.ArgumentNullException;

import com.github.mdcdi1315.basemodslib.registries.IRegistryRegistrar;

import com.github.mdcdi1315.basemodslib.registries.RegistryObjectSupplier;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.DataResult;

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

    private static final class LootPoolEntryTypeObjectSupplier
        extends RegistryObjectSupplier<LootPoolEntryType>
    {
        private final MapCodec<? extends BaseLootPoolEntryContainer> sercodec;

        public LootPoolEntryTypeObjectSupplier(MapCodec<? extends BaseLootPoolEntryContainer> map_codec) {
            sercodec = map_codec;
        }

        @Override
        protected LootPoolEntryType Get(ResourceLocation resourceLocation) {
            return new LootPoolEntryType(CreateProxyObject(sercodec));
        }
    }

    public static <LPEC extends BaseLootPoolEntryContainer> void Register(IRegistryRegistrar registries , String name , MapCodec<LPEC> sercodec)
    {
        ArgumentNullException.ThrowIfNull(name , "name");
        ArgumentNullException.ThrowIfNull(registries , "registries");
        ArgumentNullException.ThrowIfNull(sercodec , "sercodec");
        registries.RegisterObject(Registries.LOOT_POOL_ENTRY_TYPE , name , new LootPoolEntryTypeObjectSupplier(sercodec));
    }

    private static <TActual extends BaseLootPoolEntryContainer> MapCodec<LootPoolEntryContainerPlaceholder<TActual>> CreateProxyObject(MapCodec<TActual> actual)
    {
        return actual.flatXmap(
                (obj) -> DataResult.success(new LootPoolEntryContainerPlaceholder<>(obj.conditions , obj , actual)),
                (LootPoolEntryContainerPlaceholder<TActual> b) -> DataResult.success(b.GetContainer())
        );
    }
}
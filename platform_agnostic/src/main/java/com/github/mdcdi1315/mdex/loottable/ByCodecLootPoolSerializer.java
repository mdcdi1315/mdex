package com.github.mdcdi1315.mdex.loottable;

import com.github.mdcdi1315.DotNetLayer.System.ArgumentNullException;

import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonDeserializationContext;

import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;

public final class ByCodecLootPoolSerializer<T extends BaseLootPoolEntryContainer>
    extends LootPoolEntryContainer.Serializer<LootPoolEntryContainerPlaceholder<T>>
{
    private final Codec<T> codec;

    public ByCodecLootPoolSerializer(Codec<T> codec) {
        ArgumentNullException.ThrowIfNull(codec , "codec");
        this.codec = codec;
    }

    @Override
    public void serializeCustom(JsonObject json, LootPoolEntryContainerPlaceholder<T> data, JsonSerializationContext jsonSerializationContext) {
        var dr = codec.encode(data.GetContainer() , JsonOps.INSTANCE , json);
        var e = dr.error();
        if (e.isPresent()) {
            throw new UnsupportedOperationException(String.format("Cannot encode loot pool entry.\nError details: %s" , e.get().message()));
        }
    }

    @Override
    @SuppressWarnings("all")
    public LootPoolEntryContainerPlaceholder<T> deserializeCustom(JsonObject jsonObject, JsonDeserializationContext cxt, LootItemCondition[] conds) {
        var dr = codec.decode(JsonOps.INSTANCE , jsonObject);
        var e = dr.error();
        if (e.isPresent()) {
            throw new UnsupportedOperationException(String.format("Cannot decode loot pool entry.\nError details: %s" , e.get().message()));
        }
        return new LootPoolEntryContainerPlaceholder<>(conds , dr.result().get().getFirst() , codec); // dr.result().get() <- We checked it above, is OK to do this.
    }
}

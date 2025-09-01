package com.github.mdcdi1315.mdex.loottable;

import com.google.gson.JsonElement;
import com.google.gson.JsonDeserializationContext;

import com.mojang.serialization.*;
import com.mojang.datafixers.util.Pair;

import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;

public final class LootItemFunctionTypeDecoder<T extends LootItemFunction>
    implements Decoder<T>
{
    private LootItemFunctionType ft;

    public LootItemFunctionTypeDecoder(LootItemFunctionType ft) {
        this.ft = ft;
    }

    @Override
    public <T1> DataResult<Pair<T, T1>> decode(DynamicOps<T1> ops, T1 input) {
        JsonElement p;
        if (input instanceof JsonElement already) {
            // If is JsonElement , decode it directly.
            p = already;
        } else {
            // Otherwise pass through JsonOps to obtain one.
            p = ops.convertTo(JsonOps.INSTANCE , input);
        }
        Serializer<T> ser = (Serializer<T>) ft.getSerializer();
        try {
            T obj = ser.deserialize(p.getAsJsonObject(), (JsonDeserializationContext) LootItemFunctions.createGsonAdapter());
            return DataResult.success(
                    new Pair<>(
                            obj,
                            input
                    )
            );
        } catch (Exception e) {
            return DataResult.error(e::toString);
        }
    }
}

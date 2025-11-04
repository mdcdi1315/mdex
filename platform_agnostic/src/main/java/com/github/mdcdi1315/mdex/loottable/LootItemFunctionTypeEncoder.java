package com.github.mdcdi1315.mdex.loottable;

import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.mojang.serialization.Encoder;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;

public final class LootItemFunctionTypeEncoder<T extends LootItemFunction>
    implements Encoder<T>
{
    @Override
    public <T1> DataResult<T1> encode(T input, DynamicOps<T1> ops, T1 prefix)
    {
        JsonObject obj = new JsonObject();
        try {
            ((Serializer<T>) input.getType().getSerializer()).serialize(obj, input, (JsonSerializationContext) LootItemFunctions.createGsonAdapter());
        } catch (Exception e) {
            return DataResult.error(e::toString);
        }
        return DataResult.success(JsonOps.INSTANCE.convertTo(ops , obj));
    }
}

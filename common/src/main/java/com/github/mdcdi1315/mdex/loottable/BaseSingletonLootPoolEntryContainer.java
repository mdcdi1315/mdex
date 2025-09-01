package com.github.mdcdi1315.mdex.loottable;

import com.mojang.datafixers.Products;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.core.registries.BuiltInRegistries;
import com.mojang.serialization.codecs.KeyDispatchCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntry;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.BiFunction;

import static net.minecraft.world.level.storage.loot.functions.LootItemFunctions.IDENTITY;

public abstract class BaseSingletonLootPoolEntryContainer
    extends BaseLootPoolEntryContainer
{
    public static final int DEFAULT_WEIGHT = 1;
    public static final int DEFAULT_QUALITY = 0;
    /**
     * The weight of the entry.
     */
    protected final int weight;
    /**
     * The quality of the entry.
     */
    protected final int quality;
    /**
     * Functions that are ran on the entry.
     */
    List<LootItemFunction> functions;
    private BiFunction<ItemStack, LootContext, ItemStack> compositeFunction;
    private SingletonPoolEntry entry;

    protected BaseSingletonLootPoolEntryContainer(int weight, int quality, List<LootItemFunction> functions)
    {
        this.weight = weight;
        this.quality = quality;
        this.functions = functions;
        this.compositeFunction = composefunctions(functions);
        entry = new SingletonPoolEntry(this);
    }

    private static BiFunction<ItemStack, LootContext, ItemStack> composefunctions(List<? extends BiFunction<ItemStack, LootContext, ItemStack>> functions) {
        switch (functions.size()) {
            case 0:
                return IDENTITY;
            case 1:
                return functions.get(0);
            case 2:
                BiFunction<ItemStack, LootContext, ItemStack> bifunction = functions.get(0);
                BiFunction<ItemStack, LootContext, ItemStack> bifunction1 = functions.get(1);
                return (p_80768_, p_80769_) -> (ItemStack)bifunction1.apply(bifunction.apply(p_80768_, p_80769_), p_80769_);
            default:
                return (p_80774_, p_80775_) -> {
                    for (BiFunction<ItemStack, LootContext, ItemStack> bifunction2 : functions) {
                        p_80774_ = bifunction2.apply(p_80774_, p_80775_);
                    }

                    return p_80774_;
                };
        }
    }

    public void validate(ValidationContext validationContext)
    {
        super.validate(validationContext);

        int I = 0;
        for (var f : functions)
        {
            f.validate(validationContext.forChild(String.format(".functions[%d]" , I)));
            I++;
        }
    }

    /**
     * Generate the loot stacks of this entry.
     * Contrary to the method name this method does not always generate one stack, it can also generate zero or multiple stacks.
     */
    protected abstract void createItemStack(Consumer<ItemStack> stackConsumer, LootContext lootContext);

    /**
     * Expand this loot pool entry container by calling {@code entryConsumer} with any applicable entries
     *
     * @return whether this loot pool entry container successfully expanded or not
     */
    public boolean expand(LootContext lootContext, Consumer<LootPoolEntry> entryConsumer)
    {
        if (entry == null) {
            return false;
        } else {
            entryConsumer.accept(this.entry);
            return true;
        }
    }

    public void Invalidate()
    {
        functions = null;
        compositeFunction = null;
        entry = null;
    }

    private static class SingletonPoolEntry
        implements LootPoolEntry
    {
        private final BaseSingletonLootPoolEntryContainer b;

        public SingletonPoolEntry(BaseSingletonLootPoolEntryContainer c) {
            b = c;
        }

        @Override
        public int getWeight(float luck) {
            return Math.max(Mth.floor((float)b.weight + (float)b.quality * luck), 0);
        }

        @Override
        public void createItemStack(Consumer<ItemStack> cis, LootContext c) {
            b.createItemStack(LootItemFunction.decorate(b.compositeFunction, cis, c), c);
        }
    }

    public static <TB extends BaseSingletonLootPoolEntryContainer> Products.P3<RecordCodecBuilder.Mu<TB>, Integer , Integer , List<LootItemFunction>> GetBaseCodec(RecordCodecBuilder.Instance<TB> instance)
    {
        return instance.group(
                Codec.INT.optionalFieldOf("weight" , DEFAULT_WEIGHT).forGetter((TB b) -> b.weight),
                Codec.INT.optionalFieldOf("quality" , DEFAULT_QUALITY).forGetter((TB b) -> b.quality),
                KeyDispatchCodec.unsafe("function" , // Using this hacky unsafe context allows us to magically pass the JsonElement to read directly , and thus read the function object on the fly.
                        BuiltInRegistries.LOOT_FUNCTION_TYPE.byNameCodec() ,
                        (LootItemFunction f) -> DataResult.success(f.getType()),
                        (LootItemFunctionType f) -> DataResult.success(new LootItemFunctionTypeDecoder<>(f)), // For decoders only, we need the type reference itself so that we can use the underlying serializer and kick in.
                        (LootItemFunction f) -> DataResult.success(new LootItemFunctionTypeEncoder<>())
                ).codec().listOf().optionalFieldOf("functions", List.of()).forGetter((TB b) -> b.functions)
        );
    }
}

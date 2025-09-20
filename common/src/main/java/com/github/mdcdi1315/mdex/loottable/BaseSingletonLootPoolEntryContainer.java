package com.github.mdcdi1315.mdex.loottable;

import com.github.mdcdi1315.mdex.util.Extensions;
import com.github.mdcdi1315.mdex.codecs.CodecUtils;

import com.mojang.datafixers.Products;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntry;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

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

    protected BaseSingletonLootPoolEntryContainer(List<LootItemCondition> c, int weight, int quality, List<LootItemFunction> functions)
    {
        super(c);
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
        if (functions == null) { return; }
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
            return Math.max(Extensions.Floor((float)b.weight + (float)b.quality * luck), 0);
        }

        @Override
        public void createItemStack(Consumer<ItemStack> cis, LootContext c) {
            b.createItemStack(LootItemFunction.decorate(b.compositeFunction, cis, c), c);
        }
    }

    public static <TB extends BaseSingletonLootPoolEntryContainer> Products.P4<RecordCodecBuilder.Mu<TB>,  Integer ,  Integer , List<LootItemCondition> , List<LootItemFunction>> GetBaseCodec(RecordCodecBuilder.Instance<TB> instance)
    {
        return instance.group(
                CodecUtils.ZERO_OR_POSITIVE_INTEGER.optionalFieldOf("weight" , DEFAULT_WEIGHT).forGetter((TB b) -> b.weight),
                CodecUtils.ZERO_OR_POSITIVE_INTEGER.optionalFieldOf("quality" , DEFAULT_QUALITY).forGetter((TB b) -> b.quality)
        ).and(CommonFields(instance).t1()).and(
                LootItemFunctions.ROOT_CODEC.listOf().optionalFieldOf("functions", List.of()).forGetter((TB b) -> b.functions)
        );
    }
}

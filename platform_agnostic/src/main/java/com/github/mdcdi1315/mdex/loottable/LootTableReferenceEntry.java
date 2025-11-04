package com.github.mdcdi1315.mdex.loottable;

import com.github.mdcdi1315.mdex.MDEXModInstance;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;

import java.util.List;
import java.util.function.Consumer;

public final class LootTableReferenceEntry
    extends BaseSingletonLootPoolEntryContainer
{
    private ResourceLocation loottable;

    LootTableReferenceEntry(int weight, int quality, List<LootItemFunction> functions , ResourceLocation table) {
        super(weight, quality, functions);
        loottable = table;
    }

    @Override
    protected void createItemStack(Consumer<ItemStack> stackConsumer, LootContext lootContext) {
        LootTable loottable = lootContext.getResolver().getLootTable(this.loottable);
        loottable.getRandomItemsRaw(lootContext, stackConsumer);
    }

    @Override
    public void validate(ValidationContext validationContext)
    {
        super.validate(validationContext);
        LootDataId<LootTable> lootdataid = new LootDataId<>(LootDataType.TABLE, this.loottable);
        if (validationContext.hasVisitedElement(lootdataid)) {
            validationContext.reportProblem(String.format("Table %s is recursively called" , loottable));
        } else {
            super.validate(validationContext);
            validationContext.resolver().getElementOptional(lootdataid).ifPresentOrElse(
                    ( lt) -> lt.validate(validationContext.enterElement("->{" + this.loottable + "}", lootdataid)),
                    () -> {
                        MDEXModInstance.LOGGER.warn("LootTableManager: Cannot find the loot table with ID '{}'. The table will not be loaded." , loottable);
                        loottable = null;
                        Invalidate();
                    });
        }
    }

    public static Codec<LootTableReferenceEntry> GetCodec()
    {
        return RecordCodecBuilder.create(
                (RecordCodecBuilder.Instance<LootTableReferenceEntry> d) -> GetBaseCodec(d).and(
                        ResourceLocation.CODEC.fieldOf("name").forGetter((LootTableReferenceEntry e) -> e.loottable)
                ).apply(d , LootTableReferenceEntry::new)
        );
    }
}

package com.github.mdcdi1315.mdex.util;

import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.MaybeNull;
import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.NotNull;

import com.github.mdcdi1315.basemodslib.codecs.CodecUtils;
import com.github.mdcdi1315.basemodslib.utils.random.weighted.IWeightedEntry;

import com.github.mdcdi1315.mdex.MDEXModInstance;
import com.github.mdcdi1315.mdex.dco_logic.Compilable;

import com.mojang.serialization.Codec;

import net.minecraft.world.entity.EntityType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.Optional;


public class WeightedEntityEntry
    implements IWeightedEntry, Compilable
{
    protected ResourceLocation EntityID;
    // Using this way you check whether this entry is elsewise invalid.
    @MaybeNull
    public EntityType<?> Entity;
    public int weight;

    public WeightedEntityEntry(ResourceLocation loc, int t)
    {
        EntityID = loc;
        weight = t;
        Entity = null;
    }

    @Override
    public void Compile()
    {
        Optional<EntityType<?>> ent = BuiltInRegistries.ENTITY_TYPE.getOptional(EntityID);
        if (ent.isEmpty()) {
            MDEXModInstance.LOGGER.error("Cannot register an entity entry with ID '{}' because it does not exist." , EntityID);
        } else {
            Entity = ent.get();
        }
        EntityID = null;
    }

    public static Codec<WeightedEntityEntry> GetCodec()
    {
        return CodecUtils.CreateCodecDirect(
                ResourceLocation.CODEC.fieldOf("id").forGetter((WeightedEntityEntry e) -> e.EntityID),
                CodecUtils.ZERO_OR_POSITIVE_INTEGER.optionalFieldOf("weight", 1).forGetter((WeightedEntityEntry e) -> e.weight),
                WeightedEntityEntry::new
        );
    }

    @NotNull
    @Override
    public int GetWeight() { return weight; }

    @Override
    public boolean IsCompiled() { return Entity != null; }
}

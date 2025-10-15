package com.github.mdcdi1315.mdex.util;

import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.MaybeNull;
import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.NotNull;

import com.github.mdcdi1315.mdex.MDEXBalmLayer;
import com.github.mdcdi1315.mdex.codecs.CodecUtils;
import com.github.mdcdi1315.mdex.util.weight.Weight;
import com.github.mdcdi1315.mdex.util.weight.IWeightedEntry;

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
    public Weight weight;

    public WeightedEntityEntry(ResourceLocation loc , Weight t)
    {
        EntityID = loc;
        weight = t;
        Entity = null;
    }

    public void Compile()
    {
        Optional<EntityType<?>> ent = BuiltInRegistries.ENTITY_TYPE.getOptional(EntityID);
        if (ent.isEmpty()) {
            MDEXBalmLayer.LOGGER.error("Cannot register an entity entry with ID '{}' because it does not exist." , EntityID);
        } else {
            Entity = ent.get();
        }
        EntityID = null;
    }

    public boolean IsCompiled()
    {
        return Entity != null;
    }

    public static Codec<WeightedEntityEntry> GetCodec()
    {
        return CodecUtils.CreateCodecDirect(
                ResourceLocation.CODEC.fieldOf("id").forGetter((WeightedEntityEntry e) -> e.EntityID),
                Weight.CODEC.optionalFieldOf("weight" , Weight.ONE).forGetter((WeightedEntityEntry e) -> e.weight),
                WeightedEntityEntry::new
        );
    }

    @Override
    @NotNull
    public Weight getWeight() {
        return weight;
    }
}

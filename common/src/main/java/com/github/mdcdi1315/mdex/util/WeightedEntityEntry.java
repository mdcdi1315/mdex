package com.github.mdcdi1315.mdex.util;

import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.MaybeNull;
import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.NotNull;

import com.github.mdcdi1315.mdex.MDEXBalmLayer;
import com.mojang.serialization.Codec;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.random.Weight;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.resources.ResourceLocation;
import com.github.mdcdi1315.mdex.codecs.CodecUtils;
import net.minecraft.world.entity.EntityType;


public class WeightedEntityEntry
    implements WeightedEntry , Compilable
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
        try {
            Entity = BuiltInRegistries.ENTITY_TYPE.get(EntityID);
        } catch (Exception e) {
            MDEXBalmLayer.LOGGER.error("Cannot register an entity entry with ID '{}' because it does not exist.\nException data: {}" , EntityID , e);
        } finally {
            EntityID = null;
        }
    }

    public boolean IsCompiled()
    {
        return Entity != null;
    }

    public static Codec<WeightedEntityEntry> GetCodec()
    {
        return CodecUtils.CreateCodecDirect(
                ResourceLocation.CODEC.fieldOf("id").forGetter((WeightedEntityEntry e) -> e.EntityID),
                Weight.CODEC.fieldOf("weight").orElse(Weight.of(1)).forGetter((WeightedEntityEntry e) -> e.weight),
                WeightedEntityEntry::new
        );
    }

    @Override
    @NotNull
    public Weight getWeight() {
        return weight;
    }
}

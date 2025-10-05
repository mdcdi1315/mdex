package com.github.mdcdi1315.mdex.util;

import com.github.mdcdi1315.DotNetLayer.System.InvalidOperationException;
import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.NotNull;

import com.github.mdcdi1315.mdex.codecs.CodecUtils;
import com.github.mdcdi1315.mdex.util.weight.Weight;

import com.mojang.serialization.Codec;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;

public class WeightedCountedEntityEntry
    extends WeightedEntityEntry
{
    public IntProvider Count;

    public WeightedCountedEntityEntry(ResourceLocation loc, Weight t , IntProvider ct)
    {
        super(loc, t);
        Count = ct;
    }

    /**
     * Equivalently converts a {@link WeightedEntityEntry} to a {@link WeightedCountedEntityEntry} instance.
     * @param ent The simple {@link WeightedEntityEntry} to convert.
     * @return The converted {@link WeightedCountedEntityEntry} instance.
     */
    public static WeightedCountedEntityEntry ToCountedEntry(@NotNull WeightedEntityEntry ent)
    {
        if (ent.IsCompiled())
        {
            throw new InvalidOperationException("Cannot convert compiled entity entries");
        }
        return new WeightedCountedEntityEntry(ent.EntityID , ent.weight , ConstantInt.of(1));
    }

    public static Codec<WeightedCountedEntityEntry> GetCountedCodec()
    {
        return CodecUtils.CreateCodecDirect(
                ResourceLocation.CODEC.fieldOf("id").forGetter((WeightedCountedEntityEntry e) -> e.EntityID),
                Weight.CODEC.optionalFieldOf("weight" , Weight.Of(1)).forGetter((WeightedCountedEntityEntry e) -> e.weight),
                IntProvider.codec(1 , 32).optionalFieldOf("count" , ConstantInt.of(1)).forGetter((WeightedCountedEntityEntry e) -> e.Count),
                WeightedCountedEntityEntry::new
        );
    }
}

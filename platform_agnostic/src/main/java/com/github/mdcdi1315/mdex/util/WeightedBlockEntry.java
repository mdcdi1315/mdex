package com.github.mdcdi1315.mdex.util;

import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.NotNull;

import com.github.mdcdi1315.mdex.MDEXModInstance;
import com.github.mdcdi1315.mdex.util.weight.Weight;
import com.github.mdcdi1315.mdex.dco_logic.Compilable;
import com.github.mdcdi1315.basemodslib.codecs.CodecUtils;
import com.github.mdcdi1315.mdex.util.weight.IWeightedEntry;

import com.mojang.serialization.Codec;

import net.minecraft.world.level.block.Block;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.Optional;

public class WeightedBlockEntry
        implements IWeightedEntry, Compilable
{
    private ResourceLocation BlockID;
    public Block Block;
    private final Weight weight;

    public WeightedBlockEntry(ResourceLocation loc , Weight t)
    {
        BlockID = loc;
        weight = t;
        this.Block = null;
    }

    public void Compile()
    {
        Optional<Block> g = BuiltInRegistries.BLOCK.getOptional(BlockID);
        if (g.isEmpty()) {
            MDEXModInstance.LOGGER.warn("Cannot load weighted block entry because the block with ID '{}' does not exist." , BlockID);
        } else {
            this.Block = g.get();
        }
        BlockID = null;
    }

    public boolean IsCompiled()
    {
        return this.Block != null;
    }

    public static Codec<WeightedBlockEntry> GetCodec()
    {
        return CodecUtils.CreateCodecDirect(
                ResourceLocation.CODEC.fieldOf("id").forGetter((WeightedBlockEntry e) -> e.BlockID),
                Weight.CODEC.optionalFieldOf("weight" , Weight.ONE).forGetter((WeightedBlockEntry e) -> e.weight),
                WeightedBlockEntry::new
        );
    }

    @Override
    @NotNull
    public Weight getWeight() {
        return weight;
    }
}
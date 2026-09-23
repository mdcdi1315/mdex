package com.github.mdcdi1315.mdex.util;

import com.github.mdcdi1315.basemodslib.codecs.CodecUtils;
import com.github.mdcdi1315.basemodslib.utils.random.weighted.IWeightedEntry;

import com.github.mdcdi1315.mdex.MDEXModInstance;
import com.github.mdcdi1315.mdex.dco_logic.Compilable;

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
    private final int weight;

    public WeightedBlockEntry(ResourceLocation loc , int t)
    {
        BlockID = loc;
        weight = t;
        this.Block = null;
    }

    @Override
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

    public static Codec<WeightedBlockEntry> GetCodec()
    {
        return CodecUtils.CreateCodecDirect(
                ResourceLocation.CODEC.fieldOf("id").forGetter((WeightedBlockEntry e) -> e.BlockID),
                CodecUtils.ZERO_OR_POSITIVE_INTEGER.optionalFieldOf("weight", 1).forGetter((WeightedBlockEntry e) -> e.weight),
                WeightedBlockEntry::new
        );
    }

    @Override
    public int GetWeight() { return weight; }

    @Override
    public boolean IsCompiled() { return this.Block != null; }
}
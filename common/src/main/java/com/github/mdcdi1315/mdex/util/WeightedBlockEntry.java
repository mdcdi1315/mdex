package com.github.mdcdi1315.mdex.util;

import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.NotNull;
import com.github.mdcdi1315.mdex.MDEXBalmLayer;
import com.github.mdcdi1315.mdex.codecs.CodecUtils;
import com.mojang.serialization.Codec;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.Weight;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.level.block.Block;

import java.util.Optional;

public class WeightedBlockEntry
        implements WeightedEntry , Compilable
{
    private ResourceLocation BlockID;
    public Block Block;
    private Weight weight;

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
            MDEXBalmLayer.LOGGER.warn("Cannot load weighted block entry because the block with ID '{}' does not exist." , BlockID);
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
                Weight.CODEC.fieldOf("weight").orElse(Weight.of(1)).forGetter((WeightedBlockEntry e) -> e.weight),
                WeightedBlockEntry::new
        );
    }

    @Override
    @NotNull
    public Weight getWeight() {
        return weight;
    }
}
package com.github.mdcdi1315.mdex.util;

import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.NotNull;
import com.github.mdcdi1315.mdex.MDEXBalmLayer;
import com.github.mdcdi1315.mdex.codecs.CodecUtils;
import com.github.mdcdi1315.mdex.util.weight.IWeightedEntry;
import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import com.github.mdcdi1315.mdex.util.weight.Weight;
import net.minecraft.world.level.block.Block;

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
        Holder.Reference<Block> ref;
        Optional<Holder.Reference<Block>> g = BuiltInRegistries.BLOCK.get(BlockID);
        if (g.isEmpty() || !(ref = g.get()).isBound()) {
            MDEXBalmLayer.LOGGER.error("Cannot load weighted block entry because the block with ID '{}' does not exist." , BlockID);
        } else {
            this.Block = ref.value();
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
                Weight.CODEC.optionalFieldOf("weight" , Weight.Of(1)).forGetter((WeightedBlockEntry e) -> e.weight),
                WeightedBlockEntry::new
        );
    }

    @Override
    @NotNull
    public Weight getWeight() {
        return weight;
    }
}
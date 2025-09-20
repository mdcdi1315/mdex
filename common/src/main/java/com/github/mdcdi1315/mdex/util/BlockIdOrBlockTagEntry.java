package com.github.mdcdi1315.mdex.util;

import net.minecraft.core.Holder;
import net.minecraft.tags.TagKey;
import net.minecraft.core.HolderSet;
import com.mojang.serialization.Codec;
import com.mojang.datafixers.util.Either;
import net.minecraft.world.level.block.Block;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.BuiltInRegistries;

import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.MaybeNull;

/**
 * A class for defining either a block tag or a single block entry.
 * In either case, the final transformed result should be obtained through the GetBlocks method.
 */
public final class BlockIdOrBlockTagEntry
{
    @MaybeNull
    private ResourceLocation BlockID;
    @MaybeNull
    private TagKey<Block> Tag;

    @SuppressWarnings("unchecked")
    private static Either<TagKey<Block> , ResourceLocation> DecodeFunction(Object obj)
    {
        if (obj instanceof ResourceLocation loc) {
            return Either.right(loc);
        } else if (obj instanceof TagKey<?> k) {
            return Either.left((TagKey<Block>)k);
        } else {
            throw new MDEXException("Invalid conversion code path");
        }
    }

    public static Codec<BlockIdOrBlockTagEntry> GetCodec()
    {
        return Codec.either(TagKey.hashedCodec(Registries.BLOCK) , ResourceLocation.CODEC).xmap(
                BlockIdOrBlockTagEntry::new,
                BlockIdOrBlockTagEntry::DecodeFunction
        );
    }

    @SuppressWarnings("unchecked")
    public BlockIdOrBlockTagEntry(Object blortag)
    {
        if (blortag instanceof ResourceLocation l) {
            BlockID = l;
        } else {
            try {
                Tag = (TagKey<Block>) blortag;
            } catch (ClassCastException cce)
            {
                throw new MDEXException("Cannot recognize the given object because it is not possibly a TagKey<Block>.");
            }
        }
    }

    public HolderSet<Block> GetBlocks()
    {
        if (BlockID != null)
        {
            var bl = BuiltInRegistries.BLOCK.getOptional(BlockID);
            if (bl.isPresent()) {
                return HolderSet.direct(Holder.direct(bl.get()));
            } else {
                throw new BlockNotFoundException(BlockID);
            }
        }
        return new ConcatenatingHolderSet<>(BuiltInRegistries.BLOCK.getTagOrEmpty(Tag));
    }

    public void Invalidate()
    {
        BlockID = null;
        Tag = null;
    }
}

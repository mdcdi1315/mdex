package com.github.mdcdi1315.mdex.util;

import com.github.mdcdi1315.DotNetLayer.System.ObjectDisposedException;
import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.MaybeNull;

import com.mojang.serialization.Codec;

import com.mojang.datafixers.util.Either;

import net.minecraft.core.Holder;
import net.minecraft.tags.TagKey;
import net.minecraft.core.HolderSet;
import net.minecraft.world.level.block.Block;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.Optional;

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

    private static Either<TagKey<Block> , ResourceLocation> DecodeFunction(BlockIdOrBlockTagEntry e)
    {
        if (e.BlockID != null) {
            return Either.right(e.BlockID);
        } else if (e.Tag != null) {
            return Either.left(e.Tag);
        } else {
            throw new ObjectDisposedException(e.getClass().getName());
        }
    }

    public static Codec<BlockIdOrBlockTagEntry> GetCodec()
    {
        return Codec.either(TagKey.hashedCodec(Registries.BLOCK) , ResourceLocation.CODEC).xmap(
                BlockIdOrBlockTagEntry::new,
                BlockIdOrBlockTagEntry::DecodeFunction
        );
    }

    public BlockIdOrBlockTagEntry(Either<TagKey<Block> , ResourceLocation> blortag)
    {
        Optional<TagKey<Block>> b = blortag.left();
        if (b.isPresent()) {
            Tag = b.get();
        } else {
            BlockID = blortag.right().get();
        }
    }

    public HolderSet<Block> GetBlocks()
    {
        ObjectDisposedException.ThrowIf(BlockID == null && Tag == null , this);
        if (BlockID != null)
        {
            var bl = BuiltInRegistries.BLOCK.getOptional(BlockID);
            if (bl.isPresent()) {
                return HolderSet.direct(Holder.direct(bl.get()));
            } else {
                throw new BlockNotFoundException(BlockID);
            }
        }
        var optlist = BuiltInRegistries.BLOCK.get(Tag);
        if (optlist.isPresent()) {
            return optlist.get();
        } else {
            throw new MDEXException("The specified tag is not bound with the Block Registry!!");
        }
    }

    public void Invalidate()
    {
        BlockID = null;
        Tag = null;
    }
}

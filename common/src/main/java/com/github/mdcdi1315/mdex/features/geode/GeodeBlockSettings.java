package com.github.mdcdi1315.mdex.features.geode;

import net.minecraft.tags.TagKey;
import com.mojang.serialization.Codec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.block.Block;
import net.minecraft.core.registries.Registries;
import com.github.mdcdi1315.mdex.codecs.CodecUtils;
import net.minecraft.world.level.block.state.BlockState;
import com.github.mdcdi1315.mdex.block.blockstateproviders.AbstractBlockStateProvider;

import java.util.List;

public class GeodeBlockSettings {
    public final AbstractBlockStateProvider fillingProvider;
    public final AbstractBlockStateProvider innerLayerProvider;
    public final AbstractBlockStateProvider alternateInnerLayerProvider;
    public final AbstractBlockStateProvider middleLayerProvider;
    public final AbstractBlockStateProvider outerLayerProvider;
    public final List<BlockState> innerPlacements;
    public final TagKey<Block> cannotReplace;
    public final TagKey<Block> invalidBlocks;

    public static Codec<GeodeBlockSettings> GetCodec()
    {
        return CodecUtils.CreateCodecDirect(
                AbstractBlockStateProvider.CODEC.fieldOf("filling_provider").forGetter((p_158323_) -> p_158323_.fillingProvider),
                AbstractBlockStateProvider.CODEC.fieldOf("inner_layer_provider").forGetter((p_158321_) -> p_158321_.innerLayerProvider),
                AbstractBlockStateProvider.CODEC.fieldOf("alternate_inner_layer_provider").forGetter((p_158319_) -> p_158319_.alternateInnerLayerProvider),
                AbstractBlockStateProvider.CODEC.fieldOf("middle_layer_provider").forGetter((p_158317_) -> p_158317_.middleLayerProvider),
                AbstractBlockStateProvider.CODEC.fieldOf("outer_layer_provider").forGetter((p_158315_) -> p_158315_.outerLayerProvider),
                ExtraCodecs.nonEmptyList(BlockState.CODEC.listOf()).fieldOf("inner_placements").forGetter((p_158313_) -> p_158313_.innerPlacements),
                TagKey.hashedCodec(Registries.BLOCK).fieldOf("cannot_replace").forGetter((p_204566_) -> p_204566_.cannotReplace),
                TagKey.hashedCodec(Registries.BLOCK).fieldOf("invalid_blocks").forGetter((p_204564_) -> p_204564_.invalidBlocks),
                GeodeBlockSettings::new
        );
    }

    public GeodeBlockSettings(
            AbstractBlockStateProvider fillingProvider,
            AbstractBlockStateProvider innerLayerProvider,
            AbstractBlockStateProvider alternateInnerLayerProvider,
            AbstractBlockStateProvider middleLayerProvider,
            AbstractBlockStateProvider outerLayerProvider,
            List<BlockState> innerPlacements,
            TagKey<Block> cannotReplace,
            TagKey<Block> invalidBlocks)
    {
        this.fillingProvider = fillingProvider;
        this.innerLayerProvider = innerLayerProvider;
        this.alternateInnerLayerProvider = alternateInnerLayerProvider;
        this.middleLayerProvider = middleLayerProvider;
        this.outerLayerProvider = outerLayerProvider;
        this.innerPlacements = innerPlacements;
        this.cannotReplace = cannotReplace;
        this.invalidBlocks = invalidBlocks;
    }
}

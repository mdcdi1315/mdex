package com.github.mdcdi1315.mdex.features.geode;

import com.github.mdcdi1315.mdex.block.blockstateproviders.AbstractBlockStateProvider;
import com.github.mdcdi1315.mdex.codecs.CodecUtils;
import com.github.mdcdi1315.mdex.util.Compilable;
import com.github.mdcdi1315.mdex.util.CompilableTargetBlockState;
import com.github.mdcdi1315.mdex.util.InvalidFeatureConfigurationException;
import com.mojang.serialization.Codec;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.block.Block;

import java.util.List;

public class GeodeBlockSettings
    implements Compilable
{
    public final AbstractBlockStateProvider fillingProvider;
    public final AbstractBlockStateProvider innerLayerProvider;
    public final AbstractBlockStateProvider alternateInnerLayerProvider;
    public final AbstractBlockStateProvider middleLayerProvider;
    public final AbstractBlockStateProvider outerLayerProvider;
    public final List<CompilableTargetBlockState> innerPlacements;
    public final TagKey<Block> cannotReplace;
    public TagKey<Block> invalidBlocks;

    public static Codec<GeodeBlockSettings> GetCodec()
    {
        Codec<TagKey<Block>> HASHED_CODEC = TagKey.hashedCodec(Registries.BLOCK);
        return CodecUtils.CreateCodecDirect(
                AbstractBlockStateProvider.CODEC.fieldOf("filling_provider").forGetter((p_158323_) -> p_158323_.fillingProvider),
                AbstractBlockStateProvider.CODEC.fieldOf("inner_layer_provider").forGetter((p_158321_) -> p_158321_.innerLayerProvider),
                AbstractBlockStateProvider.CODEC.fieldOf("alternate_inner_layer_provider").forGetter((p_158319_) -> p_158319_.alternateInnerLayerProvider),
                AbstractBlockStateProvider.CODEC.fieldOf("middle_layer_provider").forGetter((p_158317_) -> p_158317_.middleLayerProvider),
                AbstractBlockStateProvider.CODEC.fieldOf("outer_layer_provider").forGetter((p_158315_) -> p_158315_.outerLayerProvider),
                ExtraCodecs.nonEmptyList(CompilableTargetBlockState.GetCodec().codec().listOf()).fieldOf("inner_placements").forGetter((p_158313_) -> p_158313_.innerPlacements),
                HASHED_CODEC.fieldOf("cannot_replace").forGetter((p_204566_) -> p_204566_.cannotReplace),
                HASHED_CODEC.fieldOf("invalid_blocks").forGetter((p_204564_) -> p_204564_.invalidBlocks),
                GeodeBlockSettings::new
        );
    }

    public GeodeBlockSettings(
            AbstractBlockStateProvider fillingProvider,
            AbstractBlockStateProvider innerLayerProvider,
            AbstractBlockStateProvider alternateInnerLayerProvider,
            AbstractBlockStateProvider middleLayerProvider,
            AbstractBlockStateProvider outerLayerProvider,
            List<CompilableTargetBlockState> innerPlacements,
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

    @Override
    public void Compile()
    {
        for (var i : innerPlacements)
        {
            i.Compile();
            if (!i.IsCompiled())
            {
                invalidBlocks = null;
                return;
            }
        }
        fillingProvider.Compile();
        if (!fillingProvider.IsCompiled())
        {
            throw new InvalidFeatureConfigurationException("Cannot compile the feature data.");
        }
        alternateInnerLayerProvider.Compile();
        if (!alternateInnerLayerProvider.IsCompiled())
        {
            throw new InvalidFeatureConfigurationException("Cannot compile the feature data.");
        }
        innerLayerProvider.Compile();
        if (!innerLayerProvider.IsCompiled())
        {
            throw new InvalidFeatureConfigurationException("Cannot compile the feature data.");
        }
        middleLayerProvider.Compile();
        if (!middleLayerProvider.IsCompiled())
        {
            throw new InvalidFeatureConfigurationException("Cannot compile the feature data.");
        }
        outerLayerProvider.Compile();
        if (!outerLayerProvider.IsCompiled())
        {
            throw new InvalidFeatureConfigurationException("Cannot compile the feature data.");
        }
    }

    @Override
    public boolean IsCompiled() {
        return invalidBlocks != null;
    }
}

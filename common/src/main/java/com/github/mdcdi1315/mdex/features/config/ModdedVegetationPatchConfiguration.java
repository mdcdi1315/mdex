package com.github.mdcdi1315.mdex.features.config;

import com.github.mdcdi1315.mdex.codecs.CodecUtils;
import com.github.mdcdi1315.mdex.block.blockstateproviders.AbstractBlockStateProvider;

import com.mojang.serialization.Codec;

import net.minecraft.core.Holder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.placement.CaveSurface;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.List;

public final class ModdedVegetationPatchConfiguration
    extends ModdedFeatureConfiguration
{
    public final TagKey<Block> replaceable;
    public final AbstractBlockStateProvider groundState;
    public Holder<PlacedFeature> vegetationFeature;
    public CaveSurface surface;
    public final IntProvider depth;
    public final float extraBottomBlockChance;
    public final short verticalRange;
    public final float vegetationChance;
    public final IntProvider xzRadius;
    public final float extraEdgeColumnChance;

    public static Codec<ModdedVegetationPatchConfiguration> GetCodec()
    {
        return CodecUtils.CreateCodecDirect(
                GetBaseCodec(),
                TagKey.hashedCodec(Registries.BLOCK).fieldOf("replaceable").forGetter((p) -> p.replaceable),
                AbstractBlockStateProvider.CODEC.fieldOf("ground_state").forGetter((p) -> p.groundState),
                PlacedFeature.CODEC.fieldOf("vegetation_feature").forGetter((p) -> p.vegetationFeature),
                CaveSurface.CODEC.fieldOf("surface").forGetter((p) -> p.surface),
                IntProvider.codec(1, 128).fieldOf("depth").forGetter((p) -> p.depth),
                CodecUtils.FLOAT_PROBABILITY.fieldOf("extra_bottom_block_chance").forGetter((p) -> p.extraBottomBlockChance),
                CodecUtils.ShortRange(1, 256).fieldOf("vertical_range").forGetter((p) -> p.verticalRange),
                CodecUtils.FLOAT_PROBABILITY.fieldOf("vegetation_chance").forGetter((p) -> p.vegetationChance),
                IntProvider.CODEC.fieldOf("xz_radius").forGetter((p) -> p.xzRadius),
                CodecUtils.FLOAT_PROBABILITY.fieldOf("extra_edge_column_chance").forGetter((p) -> p.extraEdgeColumnChance),
                ModdedVegetationPatchConfiguration::new
        );
    }

    public ModdedVegetationPatchConfiguration(List<String> modids, TagKey<Block> replaceable, AbstractBlockStateProvider groundState, Holder<PlacedFeature> vegetationFeature, CaveSurface surface, IntProvider depth, float extraBottomBlockChance, short verticalRange, float vegetationChance, IntProvider xzRadius, float extraEdgeColumnChance)
    {
        super(modids);
        this.replaceable = replaceable;
        this.groundState = groundState;
        this.vegetationFeature = vegetationFeature;
        this.surface = surface;
        this.depth = depth;
        this.extraBottomBlockChance = extraBottomBlockChance;
        this.verticalRange = verticalRange;
        this.vegetationChance = vegetationChance;
        this.xzRadius = xzRadius;
        this.extraEdgeColumnChance = extraEdgeColumnChance;
        Compile();
    }

    @Override
    protected void compileConfigData() {
        groundState.Compile();
        if (!groundState.IsCompiled()) {
            setConfigAsInvalid();
        }
    }

    @Override
    protected void invalidateUntransformedFields() {

    }
}
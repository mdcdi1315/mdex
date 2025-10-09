package com.github.mdcdi1315.mdex.features.config;

import com.github.mdcdi1315.mdex.block.BlockUtils;
import com.github.mdcdi1315.mdex.codecs.CodecUtils;
import com.github.mdcdi1315.mdex.api.OptionalDirectHolder;

import net.minecraft.core.Holder;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.block.Block;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.List;

public final class FallenTreeConfiguration
    extends ModdedFeatureConfiguration
{
    private ResourceLocation LogTypeProviderRC;
    public Block LogTypeProvider;
    public IntProvider FallenTrunkSize;
    public Holder<PlacedFeature> VegetationPatch;
    public final float VegetationPatchPlacementProbability;

    public static Codec<FallenTreeConfiguration> GetCodec()
    {
        return CodecUtils.CreateCodecDirect(
                GetBaseCodec(),
                ResourceLocation.CODEC.fieldOf("log_block").forGetter((FallenTreeConfiguration ftc) -> ftc.LogTypeProviderRC),
                IntProvider.codec(2, 18).fieldOf("fallen_logs_count").forGetter((FallenTreeConfiguration ftc) -> ftc.FallenTrunkSize),
                PlacedFeature.CODEC.optionalFieldOf("vegetation_patch" , OptionalDirectHolder.Create(null)).forGetter((FallenTreeConfiguration ftc) -> ftc.VegetationPatch),
                CodecUtils.FLOAT_PROBABILITY.optionalFieldOf("vegetation_patch_placement_probability", 1f).forGetter((ftc) -> ftc.VegetationPatchPlacementProbability),
                FallenTreeConfiguration::new
        );
    }

    public FallenTreeConfiguration(List<String> modids, ResourceLocation LogTypeProvider , IntProvider FallenTrunkSize , Holder<PlacedFeature> cfgpatch, float cfgpatchp)
    {
        super(modids);
        this.FallenTrunkSize = FallenTrunkSize;
        this.VegetationPatch = cfgpatch;
        this.LogTypeProviderRC = LogTypeProvider;
        VegetationPatchPlacementProbability = cfgpatchp;
        Compile();
    }

    @Override
    protected void invalidateUntransformedFields() {
        LogTypeProviderRC = null;
    }

    @Override
    protected void compileConfigData()
    {
        LogTypeProvider = BlockUtils.GetBlockFromID(LogTypeProviderRC);
        BlockUtils.RequireBlockPropertyOrFail(LogTypeProvider , RotatedPillarBlock.AXIS.getName());
    }
}

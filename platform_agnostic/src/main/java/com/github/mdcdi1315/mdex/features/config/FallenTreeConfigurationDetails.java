package com.github.mdcdi1315.mdex.features.config;

import com.github.mdcdi1315.basemodslib.codecs.CodecUtils;

import com.github.mdcdi1315.mdex.block.BlockUtils;
import com.github.mdcdi1315.mdex.api.OptionalDirectHolder;

import net.minecraft.core.Holder;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.block.Block;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public final class FallenTreeConfigurationDetails
    implements IModdedFeatureConfigurationDetails
{
    private ResourceLocation LogTypeProviderRC;
    public Block LogTypeProvider;
    public IntProvider FallenTrunkSize;
    public Holder<PlacedFeature> VegetationPatch;
    public final float VegetationPatchPlacementProbability;

    public static Codec<FallenTreeConfigurationDetails> GetCodec()
    {
        return CodecUtils.CreateCodecDirect(
                ResourceLocation.CODEC.fieldOf("log_block").forGetter((FallenTreeConfigurationDetails ftc) -> ftc.LogTypeProviderRC),
                IntProvider.codec(2, 18).fieldOf("fallen_logs_count").forGetter((FallenTreeConfigurationDetails ftc) -> ftc.FallenTrunkSize),
                PlacedFeature.CODEC.optionalFieldOf("vegetation_patch" , OptionalDirectHolder.Create(null)).forGetter((FallenTreeConfigurationDetails ftc) -> ftc.VegetationPatch),
                CodecUtils.FLOAT_PROBABILITY.optionalFieldOf("vegetation_patch_placement_probability", 1f).forGetter((ftc) -> ftc.VegetationPatchPlacementProbability),
                FallenTreeConfigurationDetails::new
        );
    }

    public FallenTreeConfigurationDetails(ResourceLocation LogTypeProvider , IntProvider FallenTrunkSize , Holder<PlacedFeature> cfgpatch, float cfgpatchp)
    {
        this.FallenTrunkSize = FallenTrunkSize;
        this.VegetationPatch = cfgpatch;
        this.LogTypeProviderRC = LogTypeProvider;
        VegetationPatchPlacementProbability = cfgpatchp;
    }

    @Override
    public void Compile()
    {
        try {
            LogTypeProvider = BlockUtils.GetBlockFromID(LogTypeProviderRC);
            BlockUtils.RequireBlockPropertyOrFail(LogTypeProvider, RotatedPillarBlock.AXIS.getName());
        } finally {
            LogTypeProviderRC = null;
        }
    }
}

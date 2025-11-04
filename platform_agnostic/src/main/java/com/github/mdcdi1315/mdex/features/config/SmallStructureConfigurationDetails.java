package com.github.mdcdi1315.mdex.features.config;

import com.github.mdcdi1315.basemodslib.codecs.CodecUtils;

import com.github.mdcdi1315.mdex.features.smallstructure.StructureElementConfig;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

import java.util.ArrayList;
import java.util.List;

public final class SmallStructureConfigurationDetails
    implements IModdedFeatureConfigurationDetails
{
    public static MapCodec<SmallStructureConfigurationDetails> GetCodec()
    {
        return CodecUtils.CreateMapCodecDirect(
                StructureElementConfig.GetCodec().listOf().fieldOf("structures").forGetter((SmallStructureConfigurationDetails c) -> c.Structures),
                StructureProcessorType.LIST_CODEC.optionalFieldOf("structure_processors" , Holder.direct(new StructureProcessorList(List.of()))).forGetter((SmallStructureConfigurationDetails c) -> c.StructuresProcessors),
                PlacedFeature.LIST_CODEC.optionalFieldOf("features_to_be_generated_on_result", HolderSet.direct()).forGetter((SmallStructureConfigurationDetails c) -> c.AdditionalFeatures),
                SmallStructureConfigurationDetails::new
        );
    }

    public volatile boolean TemplatesAreCompiled;
    public HolderSet<PlacedFeature> AdditionalFeatures;
    public final List<StructureElementConfig> Structures;
    public Holder<StructureProcessorList> StructuresProcessors;

    public SmallStructureConfigurationDetails(List<StructureElementConfig> sts, Holder<StructureProcessorList> list , HolderSet<PlacedFeature> addfs)
    {
        Structures = new ArrayList<>(sts);
        StructuresProcessors = list;
        AdditionalFeatures = addfs;
    }
}

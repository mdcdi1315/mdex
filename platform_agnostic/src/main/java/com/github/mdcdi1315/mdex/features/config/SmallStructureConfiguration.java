package com.github.mdcdi1315.mdex.features.config;

import com.github.mdcdi1315.basemodslib.codecs.CodecUtils;

import com.github.mdcdi1315.mdex.features.smallstructure.StructureElementConfig;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

import java.util.ArrayList;
import java.util.List;

public final class SmallStructureConfiguration
    implements IModdedFeatureConfigurationDetails
{
    public static Codec<SmallStructureConfiguration> GetCodec()
    {
        return CodecUtils.CreateCodecDirect(
                StructureElementConfig.GetCodec().listOf().fieldOf("structures").forGetter((SmallStructureConfiguration c) -> c.Structures),
                StructureProcessorType.LIST_CODEC.optionalFieldOf("structure_processors" , Holder.direct(new StructureProcessorList(List.of()))).forGetter((SmallStructureConfiguration c) -> c.StructuresProcessors),
                PlacedFeature.LIST_CODEC.optionalFieldOf("features_to_be_generated_on_result", HolderSet.direct()).forGetter((SmallStructureConfiguration c) -> c.AdditionalFeatures),
                SmallStructureConfiguration::new
        );
    }

    public volatile boolean TemplatesAreCompiled;
    public HolderSet<PlacedFeature> AdditionalFeatures;
    public final List<StructureElementConfig> Structures;
    public Holder<StructureProcessorList> StructuresProcessors;

    public SmallStructureConfiguration(List<StructureElementConfig> sts, Holder<StructureProcessorList> list , HolderSet<PlacedFeature> addfs)
    {
        Structures = new ArrayList<>(sts);
        StructuresProcessors = list;
        AdditionalFeatures = addfs;
    }
}

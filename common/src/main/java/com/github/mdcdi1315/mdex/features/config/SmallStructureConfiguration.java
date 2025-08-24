package com.github.mdcdi1315.mdex.features.config;

import com.github.mdcdi1315.mdex.codecs.CodecUtils;
import com.github.mdcdi1315.mdex.features.smallstructure.StructureElementConfig;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.ListCodec;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.ArrayList;
import java.util.List;

public final class SmallStructureConfiguration
    extends ModdedFeatureConfiguration
{
    public static Codec<SmallStructureConfiguration> GetCodec()
    {
        return CodecUtils.CreateCodecDirect(
                GetBaseCodec(),
                new ListCodec<>(StructureElementConfig.GetCodec()).fieldOf("structures").forGetter((SmallStructureConfiguration c) -> c.Structures),
                StructureProcessorType.LIST_CODEC.optionalFieldOf("structure_processors" , Holder.direct(new StructureProcessorList(List.of()))).forGetter((SmallStructureConfiguration c) -> c.StructuresProcessors),
                PlacedFeature.LIST_CODEC.optionalFieldOf("features_to_be_generated_on_result", HolderSet.direct()).forGetter((SmallStructureConfiguration c) -> c.AdditionalFeatures),
                SmallStructureConfiguration::new
        );
    }

    public List<StructureTemplate> CompiledTemplates;
    public HolderSet<PlacedFeature> AdditionalFeatures;
    public final List<StructureElementConfig> Structures;
    public Holder<StructureProcessorList> StructuresProcessors;

    public SmallStructureConfiguration(List<String> modids, List<StructureElementConfig> sts, Holder<StructureProcessorList> list , HolderSet<PlacedFeature> addfs)
    {
        super(modids);
        Structures = new ArrayList<>(sts);
        StructuresProcessors = list;
        AdditionalFeatures = addfs;
        Compile();
    }

    @Override
    protected void compileConfigData() {
        // Not any compilation data are required, return
    }

    @Override
    protected void invalidateUntransformedFields() {

    }
}

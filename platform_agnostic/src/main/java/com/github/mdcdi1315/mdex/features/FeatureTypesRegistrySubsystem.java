package com.github.mdcdi1315.mdex.features;

import com.github.mdcdi1315.DotNetLayer.System.Func1;
import com.github.mdcdi1315.basemodslib.world.IWorldGenRegistrar;

import com.github.mdcdi1315.mdex.MDEXModInstance;
import com.github.mdcdi1315.mdex.api.teleporter.*;
import com.github.mdcdi1315.mdex.features.config.*;

import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public final class FeatureTypesRegistrySubsystem
{
    private FeatureTypesRegistrySubsystem() {}

    public static <TCONFIG extends FeatureConfiguration, TFEAT extends Feature<TCONFIG>> void RegisterCustomFeatureType(IWorldGenRegistrar wg, String name , Func1<TFEAT> feature)
    {
        MDEXModInstance.LOGGER.trace("Registering modded feature type {}" , name);
        wg.RegisterFeatureType(name , feature);
        MDEXModInstance.LOGGER.trace("Registered modded feature!!!");
    }

    public static void RegisterFeatureTypes(IWorldGenRegistrar wg)
    {
        RegisterCustomFeatureType(wg,"base_teleporter_placement", () -> new BaseTeleporterPlacementFeatureType(BaseTeleporterPlacementFeatureConfiguration.GetCodec()));
        var vegpconfig = ModdedFeatureConfiguration.GetCodec(ModdedVegetationPatchConfigurationDetails.GetCodec());
        var mc = ModdedFeatureConfiguration.GetCodec(ModdedOreFeatureConfigurationDetails.GetCodec());
        RegisterCustomFeatureType(wg,"fallen_tree" , () -> new FallenTreeFeature(ModdedFeatureConfiguration.GetCodec(FallenTreeConfigurationDetails.GetCodec())));
        RegisterCustomFeatureType(wg,"small_structure" , () -> new SmallStructureFeature(ModdedFeatureConfiguration.GetCodec(SmallStructureConfigurationDetails.GetCodec())));
        RegisterCustomFeatureType(wg,"customizable_monster_room" , () -> new CustomizableMonsterRoomFeature(ModdedFeatureConfiguration.GetCodec(CustomizableMonsterRoomConfigurationDetails.GetCodec())));
        RegisterCustomFeatureType(wg,"modded_ore" , () -> new ModdedOreFeature(mc));
        RegisterCustomFeatureType(wg,"large_stone_column" , () -> new LargeStoneColumnFeature(ModdedFeatureConfiguration.GetCodec(LargeStoneColumnFeatureConfigurationDetails.GetCodec())));
        RegisterCustomFeatureType(wg,"modded_geode" , () -> new ModdedGeodeFeature(ModdedFeatureConfiguration.GetCodec(ModdedGeodeConfigurationDetails.GetCodec())));
        RegisterCustomFeatureType(wg,"modded_scattered_ore" , () -> new ModdedScatteredOreFeature(mc));
        RegisterCustomFeatureType(wg,"simple_floating_layered_island" , () -> new SimpleFloatingIslandFeature(ModdedFeatureConfiguration.GetCodec(SimpleFloatingIslandConfigurationDetails.GetCodec())));
        RegisterCustomFeatureType(wg,"advanced_floating_layered_island" , () -> new AdvancedFloatingIslandFeature(ModdedFeatureConfiguration.GetCodec(AdvancedFloatingIslandConfigurationDetails.GetCodec())));
        RegisterCustomFeatureType(wg,"noise_generation_based_ore" , () -> new NoiseGenerationBasedOreFeature(ModdedFeatureConfiguration.GetCodec(NoiseGenerationBasedOreFeatureConfigurationDetails.GetCodec())));
        RegisterCustomFeatureType(wg,"modded_ore_vein" , () -> new ModdedOreVeinFeature(ModdedFeatureConfiguration.GetCodec(ModdedOreVeinFeatureConfigurationDetails.GetCodec())));
        RegisterCustomFeatureType(wg,"modded_legacy_ore" , () -> new ModdedLegacyOreFeature(mc));
        RegisterCustomFeatureType(wg,"modded_vegetation_patch" , () -> new ModdedVegetationPatchFeature(vegpconfig));
        RegisterCustomFeatureType(wg,"modded_waterlogged_vegetation_patch" , () -> new ModdedWaterloggedVegetationPatchFeature(vegpconfig));
        RegisterCustomFeatureType(wg,"modded_simple_block" , () -> new ModdedSimpleBlockFeature(ModdedFeatureConfiguration.GetCodec(ModdedSimpleBlockFeatureConfigurationDetails.GetCodec())));
        RegisterCustomFeatureType(wg,"modded_classic_ore_vein" , () -> new ModdedClassicOreVeinFeature(ModdedFeatureConfiguration.GetCodec(ModdedClassicOreVeinFeatureConfigurationDetails.GetClassicOreVeinCodec())));
        RegisterCustomFeatureType(wg,"modded_spring" , () -> new ModdedSpringFeature(ModdedFeatureConfiguration.GetCodec(ModdedSpringFeatureConfigurationDetails.GetCodec())));
        RegisterCustomFeatureType(wg,"create_layered_ore" , () -> new CreateLayeredOreFeature(ModdedFeatureConfiguration.GetCodec(CreateLayeredOreFeatureConfigurationDetails.GetCodec())));
    }
}

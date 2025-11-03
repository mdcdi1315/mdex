package com.github.mdcdi1315.mdex.features;

import com.github.mdcdi1315.DotNetLayer.System.Func1;
import com.github.mdcdi1315.basemodslib.world.IWorldGenRegistrar;

import com.github.mdcdi1315.mdex.MDEXModInstance;
import com.github.mdcdi1315.mdex.api.teleporter.*;
import com.github.mdcdi1315.mdex.features.config.*;

import com.mojang.serialization.Codec;

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
        /*
        Codec<ModdedVegetationPatchConfiguration> vegpconfig = ModdedVegetationPatchConfiguration.GetCodec();
        Codec<ModdedOreFeatureConfigurationDetails> mc = ModdedOreFeatureConfigurationDetails.GetCodec();
        RegisterCustomFeatureType(wg,"fallen_tree" , () -> new FallenTreeFeature(FallenTreeConfigurationDetails.GetCodec()));
        RegisterCustomFeatureType(wg,"small_structure" , () -> new SmallStructureFeature(SmallStructureConfiguration.GetCodec()));
        RegisterCustomFeatureType(wg,"customizable_monster_room" , () -> new CustomizableMonsterRoomFeature(CustomizableMonsterRoomConfigurationDetails.GetCodec()));
        RegisterCustomFeatureType(wg,"modded_ore" , () -> new ModdedOreFeature(mc));
        RegisterCustomFeatureType(wg,"large_stone_column" , () -> new LargeStoneColumnFeature(LargeStoneColumnFeatureConfigurationDetails.GetCodec()));
        RegisterCustomFeatureType(wg,"modded_geode" , () -> new ModdedGeodeFeature(ModdedGeodeConfigurationDetails.GetCodec()));
        RegisterCustomFeatureType(wg,"modded_scattered_ore" , () -> new ModdedScatteredOreFeature(mc));
        RegisterCustomFeatureType(wg,"simple_floating_layered_island" , () -> new SimpleFloatingIslandFeature(SimpleFloatingIslandConfigurationDetails.GetCodec()));
        RegisterCustomFeatureType(wg,"advanced_floating_layered_island" , () -> new AdvancedFloatingIslandFeature(AdvancedFloatingIslandConfigurationDetails.GetCodec()));
        RegisterCustomFeatureType(wg,"noise_generation_based_ore" , () -> new NoiseGenerationBasedOreFeature(NoiseGenerationBasedOreFeatureConfiguration.GetCodec()));
        RegisterCustomFeatureType(wg,"modded_ore_vein" , () -> new ModdedOreVeinFeature(ModdedOreVeinFeatureConfigurationDetails.GetCodec()));
        RegisterCustomFeatureType(wg,"modded_legacy_ore" , () -> new ModdedLegacyOreFeature(mc));
        RegisterCustomFeatureType(wg,"modded_vegetation_patch" , () -> new ModdedVegetationPatchFeature(vegpconfig));
        RegisterCustomFeatureType(wg,"modded_waterlogged_vegetation_patch" , () -> new ModdedWaterloggedVegetationPatchFeature(vegpconfig));
        RegisterCustomFeatureType(wg,"modded_simple_block" , () -> new ModdedSimpleBlockFeature(ModdedSimpleBlockFeatureConfigurationDetails.GetCodec()));
        RegisterCustomFeatureType(wg,"modded_classic_ore_vein" , () -> new ModdedClassicOreVeinFeature(ModdedClassicOreVeinFeatureConfigurationDetails.GetCodec()));
        RegisterCustomFeatureType(wg,"modded_spring" , () -> new ModdedSpringFeature(ModdedSpringFeatureConfiguration.GetCodec()));
        RegisterCustomFeatureType(wg,"create_layered_ore" , () -> new CreateLayeredOreFeature(CreateLayeredOreFeatureConfigurationDetails.GetCodec()));
         */
    }
}

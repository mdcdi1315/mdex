package com.github.mdcdi1315.mdex.features;

import com.github.mdcdi1315.mdex.MDEXBalmLayer;
import com.github.mdcdi1315.mdex.api.teleporter.BaseTeleporterPlacementFeatureConfiguration;
import com.github.mdcdi1315.mdex.api.teleporter.BaseTeleporterPlacementFeatureType;
import com.github.mdcdi1315.mdex.features.config.*;
import com.mojang.serialization.Codec;
import net.blay09.mods.balm.api.world.BalmWorldGen;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

import java.util.function.Supplier;

public final class FeatureTypesRegistrySubsystem
{
    private FeatureTypesRegistrySubsystem() {}

    public static <TCONFIG extends FeatureConfiguration, TFEAT extends Feature<TCONFIG>> void RegisterCustomFeatureType(BalmWorldGen wg, String name , Supplier<TFEAT> feature)
    {
        MDEXBalmLayer.LOGGER.trace("Registering modded feature type {}" , name);
        var p = wg.registerFeature(MDEXBalmLayer.id(name) , feature);
        MDEXBalmLayer.LOGGER.trace("Registered modded feature of type {} !!!" , p.getIdentifier().toString());
    }

    public static void RegisterFeatureTypes(BalmWorldGen wg)
    {
        Codec<ModdedOreFeatureConfiguration> mc = ModdedOreFeatureConfiguration.GetCodec();
        RegisterCustomFeatureType(wg,"fallen_tree" , () -> new FallenTreeFeature(FallenTreeConfiguration.GetCodec()));
        RegisterCustomFeatureType(wg,"small_structure" , () -> new SmallStructureFeature(SmallStructureConfiguration.GetCodec()));
        RegisterCustomFeatureType(wg,"customizable_monster_room" , () -> new CustomizableMonsterRoomFeature(CustomizableMonsterRoomConfiguration.GetCodec()));
        RegisterCustomFeatureType(wg,"modded_ore" , () -> new ModdedOreFeature(mc));
        RegisterCustomFeatureType(wg,"large_stone_column" , () -> new LargeStoneColumnFeature(LargeStoneColumnFeatureConfiguration.GetCodec()));
        RegisterCustomFeatureType(wg,"modded_geode" , () -> new ModdedGeodeFeature(ModdedGeodeConfiguration.GetCodec()));
        RegisterCustomFeatureType(wg,"modded_scattered_ore" , () -> new ModdedScatteredOreFeature(mc));
        RegisterCustomFeatureType(wg,"simple_floating_layered_island" , () -> new SimpleFloatingIslandFeature(SimpleFloatingIslandConfiguration.GetCodec()));
        RegisterCustomFeatureType(wg,"advanced_floating_layered_island" , () -> new AdvancedFloatingIslandFeature(AdvancedFloatingIslandConfiguration.GetCodec()));
        RegisterCustomFeatureType(wg,"noise_generation_based_ore" , () -> new NoiseGenerationBasedOreFeature(NoiseGenerationBasedOreFeatureConfiguration.GetCodec()));
        RegisterCustomFeatureType(wg,"modded_ore_vein" , () -> new ModdedOreVeinFeature(ModdedOreVeinFeatureConfiguration.GetCodec()));
        RegisterCustomFeatureType(wg,"modded_legacy_ore" , () -> new ModdedLegacyOreFeature(mc));
        RegisterCustomFeatureType(wg,"base_teleporter_placement", () -> new BaseTeleporterPlacementFeatureType(BaseTeleporterPlacementFeatureConfiguration.GetCodec()));
    }
}

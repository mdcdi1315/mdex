package com.github.mdcdi1315.mdex.features.placement;

import com.github.mdcdi1315.mdex.MDEXBalmLayer;

import net.blay09.mods.balm.api.BalmRegistries;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;

public final class PlacementModifierRegistrySubsystem
{
    private PlacementModifierRegistrySubsystem() {}

    public static void RegisterPlacementModifiers(BalmRegistries registries)
    {
        RegisterPlacementModifierType(registries , "place_with_exact_attempts" , PlaceTheSpecifiedTimesPlacementModifierType.INSTANCE);
        RegisterPlacementModifierType(registries , "place_only_once" , PlaceOnlyOncePlacementModifierType.INSTANCE);
        RegisterPlacementModifierType(registries , "ensure_valid_ceiling_placement" , IsValidCeilingPlacementModifierType.INSTANCE);
        RegisterPlacementModifierType(registries , "ensure_valid_surface_placement" , IsValidSurfacePlacementModifierType.INSTANCE);
        RegisterPlacementModifierType(registries , "randomize_input_position" , RandomizeInputPositionPlacementModifierType.INSTANCE);
    }

    public static <T extends AbstractModdedPlacementModifierType<? extends AbstractModdedPlacementModifier>> void RegisterPlacementModifierType(BalmRegistries regs , String name , T instance)
    {
        regs.register(BuiltInRegistries.PLACEMENT_MODIFIER_TYPE , (ResourceLocation location) -> instance, MDEXBalmLayer.id(name));
    }
}

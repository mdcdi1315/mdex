package com.github.mdcdi1315.mdex.features.placement;

import com.github.mdcdi1315.basemodslib.utils.ElementSupplier;
import com.github.mdcdi1315.basemodslib.world.IWorldGenRegistrar;

public final class PlacementModifierRegistrySubsystem
{
    private PlacementModifierRegistrySubsystem() {}

    public static void RegisterPlacementModifiers(IWorldGenRegistrar registries)
    {
        RegisterPlacementModifierType(registries , "place_with_exact_attempts" , PlaceTheSpecifiedTimesPlacementModifierType.INSTANCE);
        RegisterPlacementModifierType(registries , "place_only_once" , PlaceOnlyOncePlacementModifierType.INSTANCE);
        RegisterPlacementModifierType(registries , "ensure_valid_ceiling_placement" , IsValidCeilingPlacementModifierType.INSTANCE);
        RegisterPlacementModifierType(registries , "ensure_valid_surface_placement" , IsValidSurfacePlacementModifierType.INSTANCE);
        RegisterPlacementModifierType(registries , "randomize_input_position" , RandomizeInputPositionPlacementModifierType.INSTANCE);
    }

    public static <T extends AbstractModdedPlacementModifierType<? extends AbstractModdedPlacementModifier>> void RegisterPlacementModifierType(IWorldGenRegistrar regs , String name , T instance) {
        regs.RegisterPlacementModifierType(name , new ElementSupplier<>(instance));
    }
}

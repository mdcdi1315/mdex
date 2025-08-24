package com.github.mdcdi1315.mdex.features.placement;

import com.github.mdcdi1315.mdex.MDEXBalmLayer;
import com.github.mdcdi1315.mdex.util.MDEXInitException;

import net.blay09.mods.balm.api.BalmRegistries;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;

public final class PlacementModifierRegistrySubsystem
{
    private PlacementModifierRegistrySubsystem() {}

    public static void RegisterPlacementModifiers(BalmRegistries registries)
    {
        RegisterPlacementModifierType(registries , "is_valid_surface_placement" , IsValidSurfacePlacementModifierType.class);
        RegisterPlacementModifierType(registries , "randomize_input_position" , RandomizeInputPositionPlacementModifierType.class);
    }

    public static <T extends AbstractModdedPlacementModifierType<? extends AbstractModdedPlacementModifier>> void RegisterPlacementModifierType(BalmRegistries regs , String name , Class<T> modifiertype)
    {
        regs.register(BuiltInRegistries.PLACEMENT_MODIFIER_TYPE , (ResourceLocation location) -> {
            try {
                return (T) modifiertype.getField("INSTANCE").get(null);
            } catch (IllegalAccessException e) {
                throw new MDEXInitException(String.format("Cannot access the instance field for class %s." , modifiertype.getName()));
            } catch (NoSuchFieldException e) {
                throw new MDEXInitException(String.format("Cannot find the required INSTANCE field for class %s." , modifiertype.getName()));
            }
        } , MDEXBalmLayer.id(name));
    }
}

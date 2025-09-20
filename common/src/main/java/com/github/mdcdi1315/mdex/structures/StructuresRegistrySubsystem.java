package com.github.mdcdi1315.mdex.structures;

import com.github.mdcdi1315.mdex.MDEXBalmLayer;
import com.github.mdcdi1315.mdex.structures.customizablemineshaft.pieces.*;
import com.github.mdcdi1315.mdex.structures.customizablemineshaft.CustomizableMineshaftStructureType;

import net.blay09.mods.balm.api.BalmRegistries;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;

public final class StructuresRegistrySubsystem
{
    private StructuresRegistrySubsystem() {}

    public static void RegisterEntries(BalmRegistries registries)
    {
        RegisterCustomStructurePieceType(registries , MineShaftRoomType.INSTANCE);
        RegisterCustomStructurePieceType(registries , MineShaftStairsType.INSTANCE);
        RegisterCustomStructurePieceType(registries , MineShaftCrossingType.INSTANCE);
        RegisterCustomStructurePieceType(registries , MineShaftCorridorType.INSTANCE);
        RegisterCustomStructureType(registries ,"customizable_mineshaft" , CustomizableMineshaftStructureType.INSTANCE);
    }

    public static <TS extends AbstractStructure, TT extends AbstractStructureType<TS>> void RegisterCustomStructureType(BalmRegistries regs, String name, TT instance) {
        regs.register(BuiltInRegistries.STRUCTURE_TYPE, (ResourceLocation location) -> instance, MDEXBalmLayer.id(name));
    }

    public static <TT extends AbstractStructurePieceType> void RegisterCustomStructurePieceType(BalmRegistries regs , TT instance) {
        regs.register(BuiltInRegistries.STRUCTURE_PIECE , (ResourceLocation location) -> instance , MDEXBalmLayer.id(instance.GetNameAndClear()));
    }
}

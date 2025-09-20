package com.github.mdcdi1315.mdex.structures;

import com.github.mdcdi1315.mdex.MDEXBalmLayer;
import com.github.mdcdi1315.DotNetLayer.System.ArgumentNullException;

import net.blay09.mods.balm.api.BalmRegistries;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;

public final class StructureProcessorsRegistrySubsystem
{
    private StructureProcessorsRegistrySubsystem() {}

    public static void RegisterStructureProcessors(BalmRegistries registries)
    {
        RegisterCustomStructureProcessor(registries , "increment_block_phase" , BlockPhasesStructureProcessorType.INSTANCE);
        RegisterCustomStructureProcessor(registries , "specific_loot_appender" , SpecificLootAppenderProcessorType.INSTANCE);
        RegisterCustomStructureProcessor(registries , "loot_table_appender" , LootTableAppenderProcessorType.INSTANCE);
        RegisterCustomStructureProcessor(registries , "protected_blocks" , ModdedProtectedBlocksProcessorType.INSTANCE);
    }

    public static <SP extends AbstractModdedStructureProcessor , T extends AbstractModdedStructureProcessorType<SP>> void RegisterCustomStructureProcessor(BalmRegistries regs , String name , T instance)
    {
        ArgumentNullException.ThrowIfNull(instance , "instance");
        regs.register(BuiltInRegistries.STRUCTURE_PROCESSOR , (ResourceLocation location) -> instance , MDEXBalmLayer.id(name));
    }
}

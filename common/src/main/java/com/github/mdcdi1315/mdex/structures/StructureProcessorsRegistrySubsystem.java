package com.github.mdcdi1315.mdex.structures;

import com.github.mdcdi1315.mdex.MDEXBalmLayer;
import com.github.mdcdi1315.mdex.util.MDEXInitException;

import net.blay09.mods.balm.api.BalmRegistries;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;

public final class StructureProcessorsRegistrySubsystem
{
    private StructureProcessorsRegistrySubsystem() {}

    public static void RegisterStructureProcessors(BalmRegistries registries)
    {
        RegisterCustomStructureProcessor(registries , "loot_table_appender" , LootTableAppenderProcessorType.class);
        RegisterCustomStructureProcessor(registries , "protected_blocks" , ModdedProtectedBlocksProcessorType.class);
    }

    public static <SP extends AbstractModdedStructureProcessor , T extends AbstractModdedStructureProcessorType<SP>> void RegisterCustomStructureProcessor(BalmRegistries regs , String name , Class<T> sptcodecclass)
    {
        regs.register(BuiltInRegistries.STRUCTURE_PROCESSOR , (ResourceLocation location) -> {
            try {
                return (T) sptcodecclass.getField("INSTANCE").get(null);
            } catch (IllegalAccessException e) {
                throw new MDEXInitException(String.format("Cannot access the instance field for class %s." , sptcodecclass.getName()));
            } catch (NoSuchFieldException e) {
                throw new MDEXInitException(String.format("Cannot find the required INSTANCE field for class %s." , sptcodecclass.getName()));
            }
        } , MDEXBalmLayer.id(name));
    }
}

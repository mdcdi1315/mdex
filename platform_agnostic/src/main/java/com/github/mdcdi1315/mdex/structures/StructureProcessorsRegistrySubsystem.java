package com.github.mdcdi1315.mdex.structures;

import com.github.mdcdi1315.DotNetLayer.System.ArgumentNullException;

import com.github.mdcdi1315.basemodslib.registries.IRegistryRegistrar;
import com.github.mdcdi1315.basemodslib.registries.RegistryObjectSupplier;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

public final class StructureProcessorsRegistrySubsystem
{
    private StructureProcessorsRegistrySubsystem() {}

    public static void RegisterStructureProcessors(IRegistryRegistrar registries)
    {
        RegisterCustomStructureProcessor(registries , "increment_block_phase" , BlockPhasesStructureProcessorType.INSTANCE);
        RegisterCustomStructureProcessor(registries , "specific_loot_appender" , SpecificLootAppenderProcessorType.INSTANCE);
        RegisterCustomStructureProcessor(registries , "loot_table_appender" , LootTableAppenderProcessorType.INSTANCE);
        RegisterCustomStructureProcessor(registries , "protected_blocks" , ModdedProtectedBlocksProcessorType.INSTANCE);
    }

    private static final class StructureProcessorTypeObject
        extends RegistryObjectSupplier<StructureProcessorType<?>>
    {
        private final AbstractModdedStructureProcessorType<?> type;

        public StructureProcessorTypeObject(AbstractModdedStructureProcessorType<?> type) {
            this.type = type;
        }

        @Override
        protected StructureProcessorType<?> Get(ResourceLocation resourceLocation) {
            return type;
        }
    }


    public static <SP extends AbstractModdedStructureProcessor , T extends AbstractModdedStructureProcessorType<SP>> void RegisterCustomStructureProcessor(IRegistryRegistrar regs , String name , T instance)
    {
        ArgumentNullException.ThrowIfNull(instance , "instance");
        regs.RegisterObject(Registries.STRUCTURE_PROCESSOR , name, new StructureProcessorTypeObject(instance));
    }
}

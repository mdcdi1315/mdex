package com.github.mdcdi1315.mdex.structures;

import com.github.mdcdi1315.basemodslib.registries.IRegistryRegistrar;
import com.github.mdcdi1315.basemodslib.registries.IBulkRegistryObjectRegister;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

public final class StructureProcessorsRegistrySubsystem
{
    private StructureProcessorsRegistrySubsystem() {}

    public static void RegisterStructureProcessors(IRegistryRegistrar registries)
    {
        IBulkRegistryObjectRegister<StructureProcessorType<?>> register = registries.GetBulkRegister(Registries.STRUCTURE_PROCESSOR);
        register.Add("increment_block_phase", BlockPhasesStructureProcessorType.INSTANCE);
        register.Add("specific_loot_appender", SpecificLootAppenderProcessorType.INSTANCE);
        register.Add("loot_table_appender", LootTableAppenderProcessorType.INSTANCE);
        register.Add("protected_blocks", ModdedProtectedBlocksProcessorType.INSTANCE);
    }
}

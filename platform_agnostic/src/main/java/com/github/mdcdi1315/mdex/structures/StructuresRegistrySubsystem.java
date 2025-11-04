package com.github.mdcdi1315.mdex.structures;

import com.github.mdcdi1315.basemodslib.registries.IRegistryRegistrar;
import com.github.mdcdi1315.basemodslib.registries.RegistryObjectSupplier;
import com.github.mdcdi1315.mdex.structures.customizablemineshaft.pieces.*;
import com.github.mdcdi1315.mdex.structures.customizablemineshaft.CustomizableMineshaftStructureType;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;

public final class StructuresRegistrySubsystem
{
    private StructuresRegistrySubsystem() {}

    public static void RegisterEntries(IRegistryRegistrar registries)
    {
        RegisterCustomStructurePieceType(registries , MineShaftRoomType.INSTANCE);
        RegisterCustomStructurePieceType(registries , MineShaftStairsType.INSTANCE);
        RegisterCustomStructurePieceType(registries , MineShaftCrossingType.INSTANCE);
        RegisterCustomStructurePieceType(registries , MineShaftCorridorType.INSTANCE);
        RegisterCustomStructureType(registries ,"customizable_mineshaft" , CustomizableMineshaftStructureType.INSTANCE);
    }

    private static final class StructurePieceTypeObject<TT extends AbstractStructurePieceType>
        extends RegistryObjectSupplier<StructurePieceType>
    {
        private final TT instance;

        public StructurePieceTypeObject(TT instance) {
            this.instance = instance;
        }

        @Override
        protected StructurePieceType Get(ResourceLocation resourceLocation) {
            return instance;
        }
    }

    private static final class StructureTypeObject
        extends RegistryObjectSupplier<StructureType<?>>
    {
        private final AbstractStructureType<?> type;

        public StructureTypeObject(AbstractStructureType<?> type) { this.type = type; }

        @Override
        protected StructureType<?> Get(ResourceLocation resourceLocation) {
            return type;
        }
    }

    public static <TS extends AbstractStructure, TT extends AbstractStructureType<TS>> void RegisterCustomStructureType(IRegistryRegistrar regs, String name, TT instance) {
        regs.RegisterObject(Registries.STRUCTURE_TYPE, name , new StructureTypeObject(instance));
    }

    public static <TT extends AbstractStructurePieceType> void RegisterCustomStructurePieceType(IRegistryRegistrar regs , TT instance) {
        regs.RegisterObject(Registries.STRUCTURE_PIECE, instance.GetNameAndClear() , new StructurePieceTypeObject<>(instance));
    }
}

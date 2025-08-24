package com.github.mdcdi1315.mdex.block;

import com.github.mdcdi1315.mdex.MDEXBalmLayer;
import com.github.mdcdi1315.mdex.util.MDEXInitException;

import net.blay09.mods.balm.api.BalmRegistries;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;

public final class BlockPredicatesRegistrySubsystem
{
    private BlockPredicatesRegistrySubsystem() {}

    public static void RegisterBlockPredicates(BalmRegistries registries)
    {
        RegisterCustomBlockPredicate(registries, "matches_modded_block" , ModdedBlockPredicateType.class);
        // RegisterCustomBlockPredicate("would_survive" , ModdedBlockWouldSurvivePredicate.class);
    }

    public static <P extends AbstractModdedBlockPredicate , G extends AbstractModdedBlockPredicateType<P>> void RegisterCustomBlockPredicate(BalmRegistries regs, String name , Class<G> predicatecodecclass)
    {
        regs.register(BuiltInRegistries.BLOCK_PREDICATE_TYPE , (ResourceLocation location) -> {
            try {
                return (G) predicatecodecclass.getField("INSTANCE").get(null);
            } catch (IllegalAccessException e) {
                throw new MDEXInitException(String.format("Cannot access the instance field for class %s." , predicatecodecclass.getName()));
            } catch (NoSuchFieldException e) {
                throw new MDEXInitException(String.format("Cannot find the required INSTANCE field for class %s." , predicatecodecclass.getName()));
            }
        } , MDEXBalmLayer.id(name));
    }
}

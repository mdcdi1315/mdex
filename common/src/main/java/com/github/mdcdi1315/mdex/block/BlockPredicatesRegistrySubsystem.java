package com.github.mdcdi1315.mdex.block;

import com.github.mdcdi1315.mdex.MDEXBalmLayer;

import net.blay09.mods.balm.api.BalmRegistries;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;

public final class BlockPredicatesRegistrySubsystem
{
    private BlockPredicatesRegistrySubsystem() {}

    public static void RegisterBlockPredicates(BalmRegistries registries)
    {
        RegisterCustomBlockPredicate(registries, "matches_modded_block" , ModdedBlockPredicateType.INSTANCE);
        // RegisterCustomBlockPredicate("would_survive" , ModdedBlockWouldSurvivePredicate.class);
    }

    public static <P extends AbstractModdedBlockPredicate , G extends AbstractModdedBlockPredicateType<P>> void RegisterCustomBlockPredicate(BalmRegistries regs, String name , G instance)
    {
        regs.register(BuiltInRegistries.BLOCK_PREDICATE_TYPE , (ResourceLocation location) -> instance , MDEXBalmLayer.id(name));
    }
}

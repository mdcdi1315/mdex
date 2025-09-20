package com.github.mdcdi1315.mdex.structures;

import com.github.mdcdi1315.DotNetLayer.System.ArgumentNullException;

import com.github.mdcdi1315.mdex.MDEXBalmLayer;

import net.blay09.mods.balm.api.BalmRegistries;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;

public final class RuleTestsRegistrySubsystem
{
    private RuleTestsRegistrySubsystem() {}

    public static void RegisterRuleTests(BalmRegistries registries)
    {
        RegisterRuleTestType(registries , "any_random_blockstate_match" , RandomBlockStatesMatchRuleTestType.INSTANCE);
        RegisterRuleTestType(registries , "any_matching_tag" , AnyMatchingTagRuleTestType.INSTANCE);
    }

    public static <T extends AbstractModdedRuleTestType<? extends AbstractModdedRuleTest>> void RegisterRuleTestType(BalmRegistries regs , String name , T instance)
    {
        ArgumentNullException.ThrowIfNull(instance , "instance");
        regs.register(BuiltInRegistries.RULE_TEST , (ResourceLocation location) -> instance, MDEXBalmLayer.id(name));
    }
}

package com.github.mdcdi1315.mdex.structures;

import com.github.mdcdi1315.mdex.MDEXBalmLayer;
import com.github.mdcdi1315.mdex.util.MDEXInitException;

import net.blay09.mods.balm.api.BalmRegistries;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;

public final class RuleTestsRegistrySubsystem
{
    private RuleTestsRegistrySubsystem() {}

    public static void RegisterRuleTests(BalmRegistries registries)
    {
        RegisterRuleTestType(registries , "any_random_blockstate_match" , RandomBlockStatesMatchRuleTestType.class);
        RegisterRuleTestType(registries , "any_matching_tag" , AnyMatchingTagRuleTestType.class);
    }

    public static <T extends AbstractModdedRuleTestType<? extends AbstractModdedRuleTest>> void RegisterRuleTestType(BalmRegistries regs , String name , Class<T> modifiertype)
    {
        regs.register(BuiltInRegistries.RULE_TEST , (ResourceLocation location) -> {
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

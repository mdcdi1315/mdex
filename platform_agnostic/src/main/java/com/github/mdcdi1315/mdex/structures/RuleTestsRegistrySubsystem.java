package com.github.mdcdi1315.mdex.structures;

import com.github.mdcdi1315.DotNetLayer.System.ArgumentNullException;

import com.github.mdcdi1315.basemodslib.registries.IRegistryRegistrar;
import com.github.mdcdi1315.basemodslib.registries.RegistryObjectSupplier;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTestType;

public final class RuleTestsRegistrySubsystem
{
    private RuleTestsRegistrySubsystem() {}

    public static void RegisterRuleTests(IRegistryRegistrar registries)
    {
        RegisterRuleTestType(registries , "any_random_blockstate_match" , RandomBlockStatesMatchRuleTestType.INSTANCE);
        RegisterRuleTestType(registries , "any_matching_tag" , AnyMatchingTagRuleTestType.INSTANCE);
    }

    private static final class RuleTestTypeObject
        extends RegistryObjectSupplier<RuleTestType<?>>
    {
        private final AbstractModdedRuleTestType<?> type;

        public RuleTestTypeObject(AbstractModdedRuleTestType<?> type) {
            this.type = type;
        }

        @Override
        protected RuleTestType<?> Get(ResourceLocation resourceLocation) {
            return type;
        }
    }

    public static <T extends AbstractModdedRuleTestType<? extends AbstractModdedRuleTest>> void RegisterRuleTestType(IRegistryRegistrar regs , String name , T instance)
    {
        ArgumentNullException.ThrowIfNull(instance , "instance");
        regs.RegisterObject(Registries.RULE_TEST , name, new RuleTestTypeObject(instance));
    }
}

package com.github.mdcdi1315.mdex.structures;

import com.mojang.serialization.Codec;

import net.minecraft.tags.TagKey;
import net.minecraft.core.registries.Registries;

public final class AnyMatchingTagRuleTestType
    extends AbstractModdedRuleTestType<AnyMatchingTagRuleTest>
{
    public static final AnyMatchingTagRuleTestType INSTANCE = new AnyMatchingTagRuleTestType();

    @Override
    protected Codec<AnyMatchingTagRuleTest> GetCodecInstance() {
        return TagKey.codec(Registries.BLOCK).listOf().fieldOf("tags").xmap(AnyMatchingTagRuleTest::new , any -> any.ListOfTags).codec();
    }
}

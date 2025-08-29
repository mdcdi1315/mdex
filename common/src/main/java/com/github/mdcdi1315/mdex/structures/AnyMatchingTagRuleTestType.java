package com.github.mdcdi1315.mdex.structures;

import net.minecraft.tags.TagKey;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;

public final class AnyMatchingTagRuleTestType
    extends AbstractModdedRuleTestType<AnyMatchingTagRuleTest>
{
    public static AnyMatchingTagRuleTestType INSTANCE = new AnyMatchingTagRuleTestType();

    @Override
    protected MapCodec<AnyMatchingTagRuleTest> GetCodecInstance() {
        return TagKey.codec(Registries.BLOCK).listOf().fieldOf("tags").xmap(AnyMatchingTagRuleTest::new , (any) -> any.ListOfTags);
    }
}

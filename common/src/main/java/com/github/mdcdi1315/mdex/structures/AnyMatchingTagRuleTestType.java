package com.github.mdcdi1315.mdex.structures;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.ListCodec;

import net.minecraft.tags.TagKey;
import net.minecraft.core.registries.Registries;

public final class AnyMatchingTagRuleTestType
    extends AbstractModdedRuleTestType<AnyMatchingTagRuleTest>
{
    public static AnyMatchingTagRuleTestType INSTANCE = new AnyMatchingTagRuleTestType();

    @Override
    protected Codec<AnyMatchingTagRuleTest> GetCodecInstance() {
        return new ListCodec<>(TagKey.codec(Registries.BLOCK)).fieldOf("tags").xmap(AnyMatchingTagRuleTest::new , (any) -> any.ListOfTags).codec();
    }
}

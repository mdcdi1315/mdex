package com.github.mdcdi1315.mdex.structures;

import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTestType;

import java.util.List;

public final class AnyMatchingTagRuleTest
    extends AbstractModdedRuleTest
{
    public List<TagKey<Block>> ListOfTags;

    public AnyMatchingTagRuleTest(List<TagKey<Block>> tags)
    {
        ListOfTags = tags;
    }

    @Override
    protected boolean CompileRuleTestData() {
        return true;
    }

    @Override
    public boolean test(BlockState blockState, RandomSource randomSource)
    {
        for (var g : ListOfTags)
        {
            if (blockState.is(g))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    protected RuleTestType<?> getType() {
        return AnyMatchingTagRuleTestType.INSTANCE;
    }
}

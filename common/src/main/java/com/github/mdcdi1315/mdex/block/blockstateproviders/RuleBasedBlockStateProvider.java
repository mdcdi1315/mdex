package com.github.mdcdi1315.mdex.block.blockstateproviders;


import com.github.mdcdi1315.mdex.codecs.CodecUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;

import java.util.List;

public record RuleBasedBlockStateProvider(AbstractBlockStateProvider fallback, List<Rule> rules)
{
    //public static final Codec<RuleBasedBlockStateProvider> CODEC = RecordCodecBuilder.create((p_225939_) -> p_225939_.group(BlockStateProvider.CODEC.fieldOf("fallback").forGetter(RuleBasedBlockStateProvider::fallback), RuleBasedBlockStateProvider.Rule.CODEC.listOf().fieldOf("rules").forGetter(RuleBasedBlockStateProvider::rules)).apply(p_225939_, RuleBasedBlockStateProvider::new));

    /*
    public static RuleBasedBlockStateProvider simple(AbstractBlockStateProvider fallback) {
        return new RuleBasedBlockStateProvider(fallback, List.of());
    }

    public static RuleBasedBlockStateProvider simple(Block block) {
        return simple(AbstractBlockStateProvider.simple(block));
    }*/

    public BlockState getState(WorldGenLevel level, RandomSource random, BlockPos pos) {
        for (Rule rulebasedblockstateprovider$rule : this.rules) {
            if (rulebasedblockstateprovider$rule.ifTrue().test(level, pos)) {
                return rulebasedblockstateprovider$rule.then().getState(random, pos);
            }
        }

        return this.fallback.getState(random, pos);
    }

    public static record Rule(BlockPredicate ifTrue, AbstractBlockStateProvider then) {
        public static Codec<Rule> GetCodec() {
            return CodecUtils.CreateCodecDirect(
                    BlockPredicate.CODEC.fieldOf("if_true").forGetter(Rule::ifTrue),
                    AbstractBlockStateProvider.CODEC.fieldOf("then").forGetter(Rule::then),
                    Rule::new
            );
        }
    }
}
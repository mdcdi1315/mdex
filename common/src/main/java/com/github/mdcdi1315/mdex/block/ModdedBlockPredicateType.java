package com.github.mdcdi1315.mdex.block;

import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;
import com.github.mdcdi1315.mdex.codecs.CodecUtils;

public final class ModdedBlockPredicateType
        extends AbstractModdedBlockPredicateType<ModdedBlockMatchesBlockPredicate>
{
    public static final ModdedBlockPredicateType INSTANCE = new ModdedBlockPredicateType();

    @Override
    protected Codec<ModdedBlockMatchesBlockPredicate> GetCodecInstance() {
        return CodecUtils.CreateCodecDirect(
                GetBaseCodec(),
                ResourceLocation.CODEC.fieldOf("block").forGetter((ModdedBlockMatchesBlockPredicate inst) -> inst.BlockID),
                ModdedBlockMatchesBlockPredicate::new
        );
    }
}

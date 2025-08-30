package com.github.mdcdi1315.mdex.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.resources.ResourceLocation;
import com.github.mdcdi1315.mdex.codecs.CodecUtils;

public final class ModdedBlockPredicateType
        extends AbstractModdedBlockPredicateType<ModdedBlockMatchesBlockPredicate>
{
    public static ModdedBlockPredicateType INSTANCE = new ModdedBlockPredicateType();

    @Override
    protected MapCodec<ModdedBlockMatchesBlockPredicate> GetCodecInstance() {
        return CodecUtils.CreateMapCodecDirect(
                GetBaseCodec(),
                ResourceLocation.CODEC.fieldOf("block").forGetter((ModdedBlockMatchesBlockPredicate inst) -> inst.BlockID),
                ModdedBlockMatchesBlockPredicate::new
        );
    }
}

package com.github.mdcdi1315.mdex.block;

import net.minecraft.core.Vec3i;
import com.mojang.serialization.MapCodec;
import com.github.mdcdi1315.mdex.codecs.CodecUtils;

public final class ModdedBlockWouldSurviveType
        extends AbstractModdedBlockPredicateType<ModdedBlockWouldSurvivePredicate>
{
    public static ModdedBlockWouldSurviveType INSTANCE = new ModdedBlockWouldSurviveType();

    @Override
    protected MapCodec<ModdedBlockWouldSurvivePredicate> GetCodecInstance()
    {
        return CodecUtils.CreateMapCodecDirect(
                GetBaseCodec(),
                Vec3i.CODEC.optionalFieldOf("offset" , new Vec3i(0 , 0 , 0 )).forGetter((ModdedBlockWouldSurvivePredicate p) -> p.Offset),
                ModdedBlockWouldSurvivePredicate::new
        );
    }
}

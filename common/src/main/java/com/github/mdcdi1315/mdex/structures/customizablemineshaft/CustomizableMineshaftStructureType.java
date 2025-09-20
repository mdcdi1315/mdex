package com.github.mdcdi1315.mdex.structures.customizablemineshaft;

import com.github.mdcdi1315.mdex.codecs.CodecUtils;
import com.github.mdcdi1315.mdex.structures.AbstractStructure;
import com.github.mdcdi1315.mdex.structures.AbstractStructureType;

import com.mojang.serialization.Codec;

import net.minecraft.world.level.levelgen.structure.Structure;

public final class CustomizableMineshaftStructureType
    extends AbstractStructureType<CustomizableMineshaftStructure>
{
    public static final CustomizableMineshaftStructureType INSTANCE = new CustomizableMineshaftStructureType();

    @Override
    protected Codec<CustomizableMineshaftStructure> GetCodecInstance() {
        return CodecUtils.CreateCodecDirect(
                GetBaseCodec(),
                Structure.StructureSettings.CODEC.forGetter(AbstractStructure::GetSettings),
                CustomizableMineshaftStructureSettings.GetCodec().forGetter(s -> s.Settings),
                CustomizableMineshaftStructure::new
        );
    }
}

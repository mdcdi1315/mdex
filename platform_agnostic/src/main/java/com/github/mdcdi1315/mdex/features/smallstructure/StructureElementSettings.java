package com.github.mdcdi1315.mdex.features.smallstructure;

import com.github.mdcdi1315.basemodslib.codecs.CodecUtils;

import com.mojang.serialization.Codec;

import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;

public final class StructureElementSettings
{
    private static final byte
            STATE_SHOULD_IGNORE_ENTITIES = 1 << 0 ,
            STATE_SHOULD_KEEP_FLUIDS = 1 << 1;

    public static Codec<StructureElementSettings> GetCodec()
    {
        return CodecUtils.CreateCodecDirect(
                Mirror.CODEC.fieldOf("mirror").forGetter((StructureElementSettings c) -> c.PlacementMirror),
                Rotation.CODEC.fieldOf("rotation").forGetter((StructureElementSettings c) -> c.PlacementRotation),
                Codec.BOOL.optionalFieldOf("should_ignore_entities",true).forGetter(StructureElementSettings::GetShouldIgnoreEntities),
                Codec.BOOL.optionalFieldOf("should_keep_fluids",true).forGetter(StructureElementSettings::GetShouldKeepFluids),
                StructureElementSettings::new
        );
    }

    public Mirror PlacementMirror;
    public Rotation PlacementRotation;
    private byte additionalflags;

    public StructureElementSettings(Mirror m , Rotation r , boolean ignoreents , boolean keepfluids)
    {
        PlacementMirror = m;
        PlacementRotation = r;
        additionalflags = 0;
        if (ignoreents) {
            additionalflags |= STATE_SHOULD_IGNORE_ENTITIES;
        }
        if (keepfluids) {
            additionalflags |= STATE_SHOULD_KEEP_FLUIDS;
        }
    }

    public boolean GetShouldIgnoreEntities() {
        return (additionalflags & STATE_SHOULD_IGNORE_ENTITIES) != 0;
    }

    public boolean GetShouldKeepFluids() {
        return (additionalflags & STATE_SHOULD_KEEP_FLUIDS) != 0;
    }
}
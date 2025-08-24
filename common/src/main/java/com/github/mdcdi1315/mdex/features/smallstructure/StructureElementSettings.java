package com.github.mdcdi1315.mdex.features.smallstructure;

import com.github.mdcdi1315.mdex.codecs.CodecUtils;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;

public final class StructureElementSettings
{
    public static Codec<StructureElementSettings> GetCodec()
    {
        return CodecUtils.CreateCodecDirect(
                Mirror.CODEC.fieldOf("mirror").forGetter((StructureElementSettings c) -> c.PlacementMirror),
                Rotation.CODEC.fieldOf("rotation").forGetter((StructureElementSettings c) -> c.PlacementRotation),
                Codec.BOOL.optionalFieldOf("should_ignore_entities",true).forGetter((StructureElementSettings c) -> c.ShouldIgnoreEntities),
                Codec.BOOL.optionalFieldOf("should_keep_fluids",true).forGetter((StructureElementSettings c) -> c.ShouldKeepFluids),
                StructureElementSettings::new
        );
    }

    public Mirror PlacementMirror;
    public Rotation PlacementRotation;
    public boolean ShouldIgnoreEntities;
    public boolean ShouldKeepFluids;

    public StructureElementSettings(Mirror m , Rotation r , boolean ignoreents , boolean keepfluids)
    {
        PlacementMirror = m;
        PlacementRotation = r;
        ShouldIgnoreEntities = ignoreents;
        ShouldKeepFluids = keepfluids;
    }
}
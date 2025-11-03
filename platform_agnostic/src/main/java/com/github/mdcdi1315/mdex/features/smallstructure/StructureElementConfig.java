package com.github.mdcdi1315.mdex.features.smallstructure;

import com.github.mdcdi1315.basemodslib.codecs.CodecUtils;

import com.mojang.serialization.Codec;

import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public final class StructureElementConfig
{
    public static Codec<StructureElementConfig> GetCodec()
    {
        return CodecUtils.CreateCodecDirect(
                ResourceLocation.CODEC.fieldOf("id").forGetter((StructureElementConfig c) -> c.StructureID),
                StructureElementSettings.GetCodec().optionalFieldOf("settings" , new StructureElementSettings(Mirror.NONE , Rotation.NONE , false , true)).forGetter((StructureElementConfig c) -> c.Settings),
                StructureElementConfig::new
        );
    }

    public StructureTemplate Template; // Assigned at run-time once the feature requests that this entry should be placed down.
    public ResourceLocation StructureID;
    public StructureElementSettings Settings;

    public StructureElementConfig(ResourceLocation loc , StructureElementSettings sets)
    {
        StructureID = loc;
        Settings = sets;
    }
}
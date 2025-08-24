package com.github.mdcdi1315.mdex.features.smallstructure;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.resources.ResourceLocation;
import com.github.mdcdi1315.mdex.codecs.CodecUtils;

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

    public ResourceLocation StructureID;
    public StructureElementSettings Settings;

    public StructureElementConfig(ResourceLocation loc , StructureElementSettings sets)
    {
        StructureID = loc;
        Settings = sets;
    }

    public StructureElementConfig()
    {
        StructureID = null;
        Settings = null;
    }
}
package com.github.mdcdi1315.mdex.api.teleporter;

import com.github.mdcdi1315.mdex.block.blockstateproviders.AbstractBlockStateProvider;
import com.github.mdcdi1315.mdex.codecs.CodecUtils;
import com.github.mdcdi1315.mdex.util.Compilable;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public final class BaseTeleporterPlacementFeatureConfiguration
    implements FeatureConfiguration , Compilable
{
    //public boolean IgnoreEmptySpaceCheck;
    public AbstractBlockStateProvider Base_Plate_Provider;
    public AbstractBlockStateProvider Light_Block_Provider;

    public BaseTeleporterPlacementFeatureConfiguration(AbstractBlockStateProvider base , AbstractBlockStateProvider light)
    {
        Base_Plate_Provider = base;
        Light_Block_Provider = light;
        //IgnoreEmptySpaceCheck = false;
        Compile();
    }

    public static Codec<BaseTeleporterPlacementFeatureConfiguration> GetCodec()
    {
        return CodecUtils.CreateCodecDirect(
                AbstractBlockStateProvider.CODEC.fieldOf("base_plate_provider").forGetter((g) -> g.Base_Plate_Provider),
                AbstractBlockStateProvider.CODEC.fieldOf("light_block_provider").forGetter((g) -> g.Light_Block_Provider),
                BaseTeleporterPlacementFeatureConfiguration::new
        );
    }

    @Override
    public void Compile() {
        Base_Plate_Provider.Compile();
        Light_Block_Provider.Compile();
    }

    @Override
    public boolean IsCompiled() {
        return Base_Plate_Provider.IsCompiled() && Light_Block_Provider.IsCompiled();
    }
}
package com.github.mdcdi1315.mdex.api.teleporter;

import com.github.mdcdi1315.mdex.dco_logic.Compilable;
import com.github.mdcdi1315.basemodslib.codecs.CodecUtils;
import com.github.mdcdi1315.mdex.block.blockstateproviders.AbstractBlockStateProvider;

import com.mojang.serialization.Codec;

import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public final class BaseTeleporterPlacementFeatureConfiguration
    implements FeatureConfiguration , Compilable
{
    public IntProvider Size;
    public boolean PlaceStarterChest;
    public StarterChestPlacement ChestPlacement;
    public AbstractBlockStateProvider Base_Plate_Provider;
    public AbstractBlockStateProvider Light_Block_Provider;

    public BaseTeleporterPlacementFeatureConfiguration(AbstractBlockStateProvider base , AbstractBlockStateProvider light , IntProvider size , StarterChestPlacement p)
    {
        Size = size;
        Base_Plate_Provider = base;
        Light_Block_Provider = light;
        ChestPlacement = p;
        PlaceStarterChest = false;
        Compile();
    }

    public static Codec<BaseTeleporterPlacementFeatureConfiguration> GetCodec()
    {
        return CodecUtils.CreateCodecDirect(
                AbstractBlockStateProvider.CODEC.fieldOf("base_plate_provider").forGetter((g) -> g.Base_Plate_Provider),
                AbstractBlockStateProvider.CODEC.fieldOf("light_block_provider").forGetter((g) -> g.Light_Block_Provider),
                IntProvider.codec(1 , 6).fieldOf("size").forGetter((g) -> g.Size),
                StarterChestPlacement.GetCodec().optionalFieldOf("starter_chest_placement" , new StarterChestPlacement(null , null , 0f)).forGetter((g) -> g.ChestPlacement),
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
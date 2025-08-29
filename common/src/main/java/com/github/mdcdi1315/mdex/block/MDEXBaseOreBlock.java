package com.github.mdcdi1315.mdex.block;

import com.github.mdcdi1315.mdex.MDEXBalmLayer;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

public class MDEXBaseOreBlock
      extends DropExperienceBlock
{
    public MDEXBaseOreBlock(Properties properties , String id) {
        super(ConstantInt.of(0), properties.instrument(NoteBlockInstrument.BASEDRUM).sound(SoundType.STONE).mapColor(MapColor.STONE).overrideDescription(BlockUtils.ConstructExactDescriptionID(MDEXBalmLayer.MODID , id)).setId(ResourceKey.create(Registries.BLOCK , MDEXBalmLayer.BlockID(id))));
    }

    public MDEXBaseOreBlock(Properties properties , String id , IntProvider xpRange) {
        super(xpRange , properties.instrument(NoteBlockInstrument.BASEDRUM).sound(SoundType.STONE).mapColor(MapColor.STONE).overrideDescription(BlockUtils.ConstructExactDescriptionID(MDEXBalmLayer.MODID , id)).setId(ResourceKey.create(Registries.BLOCK , MDEXBalmLayer.BlockID(id))));
    }
}

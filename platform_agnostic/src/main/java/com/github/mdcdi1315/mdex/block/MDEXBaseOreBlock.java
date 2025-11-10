package com.github.mdcdi1315.mdex.block;

import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;

public class MDEXBaseOreBlock
        extends DropExperienceBlock
{
    public MDEXBaseOreBlock(Properties properties) {
        super(ConstantInt.of(0), properties.instrument(NoteBlockInstrument.BASEDRUM).sound(SoundType.STONE).mapColor(MapColor.STONE));
    }

    public MDEXBaseOreBlock(Properties properties, IntProvider xpRange) {
        super(xpRange, properties.instrument(NoteBlockInstrument.BASEDRUM).sound(SoundType.STONE).mapColor(MapColor.STONE));
    }
}
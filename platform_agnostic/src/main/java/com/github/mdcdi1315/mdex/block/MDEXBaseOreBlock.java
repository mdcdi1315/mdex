package com.github.mdcdi1315.mdex.block;

import com.github.mdcdi1315.mdex.MDEXModInstance;

import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;

public class MDEXBaseOreBlock
      extends DropExperienceBlock
{
    private String descid;

    public MDEXBaseOreBlock(Properties properties , String descid) {
        super(properties.instrument(NoteBlockInstrument.BASEDRUM).sound(SoundType.STONE).mapColor(MapColor.STONE));
        this.descid = BlockUtils.ConstructExactDescriptionID(MDEXModInstance.MOD_ID , descid);
    }

    public MDEXBaseOreBlock(Properties properties , String descid , IntProvider xpRange) {
        super(properties.instrument(NoteBlockInstrument.BASEDRUM).sound(SoundType.STONE).mapColor(MapColor.STONE), xpRange);
        this.descid = BlockUtils.ConstructExactDescriptionID(MDEXModInstance.MOD_ID , descid);
    }

    @Override
    public final String getDescriptionId() {
        return descid;
    }
}

package com.github.mdcdi1315.mdex.block;

import com.github.mdcdi1315.mdex.MDEXBalmLayer;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

public class MDEXBaseOreBlock
      extends DropExperienceBlock
{
    private String descid;

    public MDEXBaseOreBlock(Properties properties , String descid) {
        super(ConstantInt.of(0), properties.instrument(NoteBlockInstrument.BASEDRUM).sound(SoundType.STONE).mapColor(MapColor.STONE));
        this.descid = BlockUtils.ConstructExactDescriptionID(MDEXBalmLayer.MODID , descid);
    }

    public MDEXBaseOreBlock(Properties properties , String descid , IntProvider xpRange) {
        super(xpRange , properties.instrument(NoteBlockInstrument.BASEDRUM).sound(SoundType.STONE).mapColor(MapColor.STONE));
        this.descid = BlockUtils.ConstructExactDescriptionID(MDEXBalmLayer.MODID , descid);
    }

    @Override
    public final String getDescriptionId() {
        return descid;
    }
}

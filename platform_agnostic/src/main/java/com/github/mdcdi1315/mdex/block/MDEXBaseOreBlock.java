package com.github.mdcdi1315.mdex.block;

import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;

public class MDEXBaseOreBlock
        extends DropExperienceBlock
{
    public MDEXBaseOreBlock(Properties properties , ResourceLocation location) {
        super(
                ConstantInt.of(0),
                properties
                        .instrument(NoteBlockInstrument.BASEDRUM)
                        .sound(SoundType.STONE)
                        .mapColor(MapColor.STONE)
                        .overrideDescription(BlockUtils.ConstructExactDescriptionID(location))
                        .setId(ResourceKey.create(Registries.BLOCK , location))
        );
    }

    public MDEXBaseOreBlock(Properties properties , ResourceLocation location , IntProvider xpRange) {
        super(
                xpRange ,
                properties
                        .instrument(NoteBlockInstrument.BASEDRUM)
                        .sound(SoundType.STONE)
                        .mapColor(MapColor.STONE)
                        .overrideDescription(BlockUtils.ConstructExactDescriptionID(location))
                        .setId(ResourceKey.create(Registries.BLOCK , location))
        );
    }
}
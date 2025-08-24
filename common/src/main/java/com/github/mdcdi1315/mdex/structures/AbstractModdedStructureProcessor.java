package com.github.mdcdi1315.mdex.structures;


import com.github.mdcdi1315.mdex.MDEXBalmLayer;
import net.blay09.mods.balm.api.Balm;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public abstract class AbstractModdedStructureProcessor
   extends StructureProcessor
{
    private static final byte STATE_MODLIST_DETERMINED = 1 << 0;
    private static final byte STATE_MODLIST_ISVALID = 1 << 1;
    private static final byte STATE_IS_VALID = 1 << 2;
    private static final byte IS_VALID_AND_NOT_INVALID = STATE_MODLIST_ISVALID | STATE_IS_VALID;
    public List<String> ModIds;
    private byte state;

    public AbstractModdedStructureProcessor(List<String> modids)
    {
        ModIds = Objects.requireNonNullElseGet(modids , List::of);
        state = STATE_IS_VALID;
    }

    public boolean getModIdListIsValid()
    {
        if ((state & STATE_MODLIST_DETERMINED) != 0) {
            return ((state & STATE_MODLIST_ISVALID)) != 0;
        }
        return internalDetermineIfModListIsValid();
    }

    private boolean internalDetermineIfModListIsValid()
    {
        for (var mod : ModIds)
        {
            if (!Balm.isModLoaded(mod)) {
                state |= STATE_MODLIST_DETERMINED;
                ModIds = null;
                return false;
            }
        }
        state |= STATE_MODLIST_DETERMINED | STATE_MODLIST_ISVALID;
        ModIds = null;
        return true;
    }

    protected abstract StructureTemplate.StructureBlockInfo processModdedBlock(
            LevelReader level,
            BlockPos offset,
            BlockPos pos,
            StructureTemplate.StructureBlockInfo blockInfo,
            StructureTemplate.StructureBlockInfo relativeBlockInfo,
            StructurePlaceSettings settings
    );

    protected abstract void compileData();

    @Nullable
    public final StructureTemplate.StructureBlockInfo processBlock(LevelReader level, BlockPos offset, BlockPos pos, StructureTemplate.StructureBlockInfo blockInfo, StructureTemplate.StructureBlockInfo relativeBlockInfo, StructurePlaceSettings settings)
    {
        if (isValid()) {
            return processModdedBlock(level ,offset , pos , blockInfo , relativeBlockInfo , settings);
        }
        // This means just to 'passthrough the block data', actually.
        return relativeBlockInfo;
    }

    public final boolean isValid()
    {
        if ((state & STATE_MODLIST_DETERMINED) == 0 && internalDetermineIfModListIsValid())
        {
            try {
                compileData();
            } catch (Exception e) {
                MDEXBalmLayer.LOGGER.warn("Reporting exception during COMPILE stage for modded structure processor of type {}: \n{}" , getClass().getName() , e);
                markAsInvalid();
                return false;
            }
            return true;
        }
        return (state & IS_VALID_AND_NOT_INVALID) == IS_VALID_AND_NOT_INVALID;
    }

    public final void markAsInvalid()
    {
        state &= ~STATE_IS_VALID;
    }

}

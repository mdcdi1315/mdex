package com.github.mdcdi1315.mdex.structures;

import com.github.mdcdi1315.mdex.MDEXBalmLayer;
import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.NotNull;
import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.MaybeNull;
import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.DisallowNull;

import net.blay09.mods.balm.api.Balm;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;

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

    public final boolean GetModIdListIsValid()
    {
        if ((state & STATE_MODLIST_DETERMINED) != 0) {
            return ((state & STATE_MODLIST_ISVALID)) != 0;
        }
        return InternalDetermineIfModListIsValid();
    }

    private boolean InternalDetermineIfModListIsValid()
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

    protected abstract StructureTemplate.StructureBlockInfo ProcessModdedBlock(
            LevelReader level,
            BlockPos offset,
            BlockPos pos,
            StructureTemplate.StructureBlockInfo blockInfo,
            StructureTemplate.StructureBlockInfo relativeBlockInfo,
            StructurePlaceSettings settings
    );

    protected void CompileData() {}

    protected List<StructureTemplate.StructureBlockInfo> FinalizeModdedBlocksProcessing(
            @DisallowNull ServerLevelAccessor sla ,
            @DisallowNull BlockPos ofs ,
            @DisallowNull BlockPos pos ,
            @DisallowNull List<StructureTemplate.StructureBlockInfo> original ,
            @DisallowNull List<StructureTemplate.StructureBlockInfo> processed ,
            @DisallowNull StructurePlaceSettings settings)
    {
        return processed;
    }

    protected abstract AbstractModdedStructureProcessorType<?> GetType();

    public final StructureProcessorType<?> getType() { return GetType(); }

    @NotNull
    @Override
    public final List<StructureTemplate.StructureBlockInfo> finalizeProcessing(ServerLevelAccessor serverLevel, BlockPos offset, BlockPos pos, List<StructureTemplate.StructureBlockInfo> originalBlockInfos, List<StructureTemplate.StructureBlockInfo> processedBlockInfos, StructurePlaceSettings settings) {
        return IsValid() ? FinalizeModdedBlocksProcessing(serverLevel , offset , pos , originalBlockInfos , processedBlockInfos , settings) : processedBlockInfos;
    }

    @MaybeNull
    public final StructureTemplate.StructureBlockInfo processBlock(LevelReader level, BlockPos offset, BlockPos pos, StructureTemplate.StructureBlockInfo blockInfo, StructureTemplate.StructureBlockInfo relativeBlockInfo, StructurePlaceSettings settings)
    {
        return IsValid() ? ProcessModdedBlock(level ,offset , pos , blockInfo , relativeBlockInfo , settings) : relativeBlockInfo;
    }

    public final boolean IsValid()
    {
        if ((state & STATE_MODLIST_DETERMINED) == 0 && InternalDetermineIfModListIsValid())
        {
            try {
                CompileData();
            } catch (Exception e) {
                MDEXBalmLayer.LOGGER.warn("Reporting exception during COMPILE stage for modded structure processor of type {}: \n{}" , getClass().getName() , e);
                MarkAsInvalid();
                return false;
            }
            return true;
        }
        return (state & IS_VALID_AND_NOT_INVALID) == IS_VALID_AND_NOT_INVALID;
    }

    public final void MarkAsInvalid()
    {
        state &= ~STATE_IS_VALID;
    }
}

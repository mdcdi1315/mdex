package com.github.mdcdi1315.mdex.features.config;

import com.github.mdcdi1315.mdex.MDEXBalmLayer;
import com.github.mdcdi1315.mdex.block.BlockUtils;
import com.github.mdcdi1315.mdex.codecs.CodecUtils;
import com.github.mdcdi1315.mdex.util.MDEXException;
import com.github.mdcdi1315.mdex.util.CompilableFluidState;
import com.github.mdcdi1315.mdex.util.BlockNotFoundException;

import com.google.common.collect.ImmutableList;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.block.Block;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.ArrayList;

public final class ModdedSpringFeatureConfiguration
    extends ModdedFeatureConfiguration
{
    public static final byte
            DEFAULT_ROCK_COUNT = 4 ,
            DEFAULT_HOLE_COUNT = 1;

    public CompilableFluidState Fluid;
    public final boolean RequiresBlockBelow;
    public final byte RockCount , HoleCount; // These must be from 0 to 5.
    public List<Block> ValidBlocks;
    private List<ResourceLocation> ValidBlocksInternal;

    public static Codec<ModdedSpringFeatureConfiguration> GetCodec()
    {
        var COMMON_COUNT = CodecUtils.ByteRange(0 , 5);
        return CodecUtils.CreateCodecDirect(
                GetBaseCodec(),
                CompilableFluidState.GetCodec().fieldOf("fluid").forGetter((sf) -> sf.Fluid),
                Codec.BOOL.optionalFieldOf("requires_block_below", true).forGetter((sf) -> sf.RequiresBlockBelow),
                COMMON_COUNT.optionalFieldOf("rock_count", DEFAULT_ROCK_COUNT).forGetter((sf) -> sf.RockCount),
                COMMON_COUNT.optionalFieldOf("hole_count", DEFAULT_HOLE_COUNT).forGetter((sf) -> sf.HoleCount),
                ResourceLocation.CODEC.listOf().fieldOf("valid_blocks").forGetter((sf) -> sf.ValidBlocksInternal),
                ModdedSpringFeatureConfiguration::new
        );
    }

    public ModdedSpringFeatureConfiguration(
            List<String> modids,
            CompilableFluidState fluid,
            boolean requiresblockbelow,
            byte rc,
            byte hc,
            List<ResourceLocation> validblocks
    )
    {
        super(modids);
        Fluid = fluid;
        ValidBlocksInternal = validblocks;
        RequiresBlockBelow = requiresblockbelow;
        RockCount = rc;
        HoleCount = hc;
        Compile();
    }

    @Override
    protected void compileConfigData() {
        Fluid.Compile();
        if (!Fluid.IsCompiled()) { setConfigAsInvalid(); return; }
        ArrayList<Block> compiledblocks = new ArrayList<>(ValidBlocksInternal.size());
        for (var blockreslocation : ValidBlocksInternal)
        {
            try {
                compiledblocks.add(BlockUtils.GetBlockFromID(blockreslocation));
            } catch (BlockNotFoundException bnf) {
                MDEXBalmLayer.LOGGER.warn("MODDEDSPRINGFEATURE: Cannot find block with ID {}. This block entry will not be added to the list of valid blocks." , bnf.getID());
            }
        }
        if (compiledblocks.isEmpty()) {
            throw new MDEXException("All the valid blocks specified were failed to be found. See above log messages for more information.");
        }
        ValidBlocks = ImmutableList.copyOf(compiledblocks);
    }

    @Override
    protected void invalidateUntransformedFields() {
        ValidBlocksInternal = null;
    }
}

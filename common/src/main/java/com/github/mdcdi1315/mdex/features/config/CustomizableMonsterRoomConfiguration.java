package com.github.mdcdi1315.mdex.features.config;

import com.github.mdcdi1315.mdex.block.BlockUtils;
import com.github.mdcdi1315.mdex.features.customizablemonsterroom.ChestPlacementConfig;
import com.github.mdcdi1315.mdex.util.WeightedBlockEntry;
import com.github.mdcdi1315.mdex.util.WeightedCountedEntityEntry;
import com.github.mdcdi1315.mdex.util.WeightedEntityEntry;

import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;
import com.mojang.serialization.codecs.ListCodec;
import com.github.mdcdi1315.mdex.codecs.CodecUtils;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;

import java.util.ArrayList;
import java.util.List;

public final class CustomizableMonsterRoomConfiguration
    extends ModdedFeatureConfiguration
{
    public static Codec<CustomizableMonsterRoomConfiguration> GetCodec()
    {
        return CodecUtils.CreateCodecDirect(
                GetBaseCodec(),
                ResourceLocation.CODEC.fieldOf("spawner_block").forGetter((CustomizableMonsterRoomConfiguration cfg) -> cfg.SpawnerBlock_I),
                ResourceLocation.CODEC.fieldOf("base_stone_block").forGetter((CustomizableMonsterRoomConfiguration cfg) -> cfg.BaseStoneBlock_I),
                Codec.floatRange(0f , 1f).optionalFieldOf("rare_stone_placement_probability" , 0.5f).forGetter((CustomizableMonsterRoomConfiguration cfg) -> cfg.RareStonePlacementProbability),
                ChestPlacementConfig.GetCodec().optionalFieldOf("reward_chest_placement", new ChestPlacementConfig(BuiltInLootTables.SIMPLE_DUNGEON , ConstantInt.of(6))).forGetter((CustomizableMonsterRoomConfiguration cfg) -> cfg.ChestConfiguration),
                new ListCodec<>(WeightedBlockEntry.GetCodec()).fieldOf("rare_stone_blocks").forGetter((CustomizableMonsterRoomConfiguration cfg) -> cfg.RareStoneBlocks),
                new ListCodec<>(WeightedEntityEntry.GetCodec()).fieldOf("additional_spawns").forGetter((CustomizableMonsterRoomConfiguration cfg) -> cfg.AdditionalEntities),
                new ListCodec<>(WeightedCountedEntityEntry.GetCountedCodec()).optionalFieldOf("spawner_block_entity_candidates" , List.of()).forGetter((CustomizableMonsterRoomConfiguration cfg) -> cfg.SpawnerEntityCandidates),
                CustomizableMonsterRoomConfiguration::new
        );
    }

    private ResourceLocation SpawnerBlock_I;
    private ResourceLocation BaseStoneBlock_I;
    public BlockState SpawnerBlock;
    public BlockState BaseStoneBlock;
    public ChestPlacementConfig ChestConfiguration;
    public List<WeightedBlockEntry> RareStoneBlocks;
    public final float RareStonePlacementProbability;
    public List<WeightedEntityEntry> AdditionalEntities;
    public List<WeightedCountedEntityEntry> SpawnerEntityCandidates;

    public CustomizableMonsterRoomConfiguration(
            List<String> modids,
            ResourceLocation sbid ,
            ResourceLocation bsbid ,
            float rareplacementprobability,
            ChestPlacementConfig chestconfig,
            List<WeightedBlockEntry> rareblocks,
            List<WeightedEntityEntry> entities,
            List<WeightedCountedEntityEntry> spawnerentities)
    {
        super(modids);
        SpawnerBlock_I = sbid;
        BaseStoneBlock_I = bsbid;
        ChestConfiguration = chestconfig;
        RareStoneBlocks = rareblocks;
        AdditionalEntities = entities;
        RareStonePlacementProbability = rareplacementprobability;
        SpawnerEntityCandidates = spawnerentities;
        Compile();
    }

    @Override
    protected void invalidateUntransformedFields()
    {
        SpawnerBlock_I = null;
        BaseStoneBlock_I = null;
    }

    @Override
    protected void compileConfigData()
    {
        SpawnerBlock = BlockUtils.GetBlockFromID(SpawnerBlock_I).defaultBlockState();
        BaseStoneBlock = BlockUtils.GetBlockFromID(BaseStoneBlock_I).defaultBlockState();
        for (var b : RareStoneBlocks)
        {
            b.Compile();
            if (b.IsCompiled() == false) {
                setConfigAsInvalid();
                return;
            }
        }
        if (SpawnerEntityCandidates.isEmpty())
        {
            SpawnerEntityCandidates = new ArrayList<>(AdditionalEntities.size());
            WeightedCountedEntityEntry e2;
            for (var e : AdditionalEntities)
            {
                e2 = WeightedCountedEntityEntry.ToCountedEntry(e);
                e2.Compile();
                if (e2.IsCompiled() == false) {
                    setConfigAsInvalid();
                    return;
                }
                SpawnerEntityCandidates.add(e2);
                e.Compile();
                if (e.IsCompiled() == false) {
                    setConfigAsInvalid();
                    return;
                }
            }
        } else {
            for (var e : AdditionalEntities)
            {
                e.Compile();
                if (e.IsCompiled() == false) {
                    setConfigAsInvalid();
                    return;
                }
            }
            for (var e : SpawnerEntityCandidates)
            {
                e.Compile();
                if (e.IsCompiled() == false) {
                    setConfigAsInvalid();
                    return;
                }
            }
        }
    }

}

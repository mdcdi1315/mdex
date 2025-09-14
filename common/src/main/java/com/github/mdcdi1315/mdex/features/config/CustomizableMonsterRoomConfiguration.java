package com.github.mdcdi1315.mdex.features.config;

import com.github.mdcdi1315.mdex.block.BlockUtils;
import com.github.mdcdi1315.mdex.codecs.CodecUtils;
import com.github.mdcdi1315.mdex.util.WeightedEntityEntry;
import com.github.mdcdi1315.mdex.block.blockstateproviders.AbstractBlockStateProvider;
import com.github.mdcdi1315.mdex.features.customizablemonsterroom.ChestPlacementConfig;

import com.mojang.serialization.Codec;

import net.minecraft.resources.ResourceLocation;
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
        Codec<List<WeightedEntityEntry>> e = WeightedEntityEntry.GetCodec().listOf();
        return CodecUtils.CreateCodecDirect(
                GetBaseCodec(),
                ResourceLocation.CODEC.fieldOf("spawner_block").forGetter((CustomizableMonsterRoomConfiguration cfg) -> cfg.SpawnerBlock_I),
                AbstractBlockStateProvider.CODEC.fieldOf("stone_block_provider").forGetter((CustomizableMonsterRoomConfiguration cfg) -> cfg.StoneBlockProvider),
                ChestPlacementConfig.GetCodec().optionalFieldOf("reward_chest_placement", new ChestPlacementConfig(BuiltInLootTables.SIMPLE_DUNGEON.location(), ConstantInt.of(6))).forGetter((CustomizableMonsterRoomConfiguration cfg) -> cfg.ChestConfiguration),
                e.fieldOf("additional_spawns").forGetter((CustomizableMonsterRoomConfiguration cfg) -> cfg.AdditionalEntities),
                e.optionalFieldOf("spawner_block_entity_candidates" , List.of()).forGetter((CustomizableMonsterRoomConfiguration cfg) -> cfg.SpawnerEntityCandidates),
                CustomizableMonsterRoomConfiguration::new
        );
    }

    private ResourceLocation SpawnerBlock_I;
    public BlockState SpawnerBlock;
    public AbstractBlockStateProvider StoneBlockProvider;
    public ChestPlacementConfig ChestConfiguration;
    public List<WeightedEntityEntry> AdditionalEntities;
    public List<WeightedEntityEntry> SpawnerEntityCandidates;

    public CustomizableMonsterRoomConfiguration(
            List<String> modids,
            ResourceLocation sbid ,
            AbstractBlockStateProvider stoneprovider ,
            ChestPlacementConfig chestconfig,
            List<WeightedEntityEntry> entities,
            List<WeightedEntityEntry> spawnerentities)
    {
        super(modids);
        SpawnerBlock_I = sbid;
        StoneBlockProvider = stoneprovider;
        ChestConfiguration = chestconfig;
        AdditionalEntities = entities;
        SpawnerEntityCandidates = spawnerentities;
        Compile();
    }

    @Override
    protected void invalidateUntransformedFields()
    {
        SpawnerBlock_I = null;
    }

    @Override
    protected void compileConfigData()
    {
        SpawnerBlock = BlockUtils.GetBlockFromID(SpawnerBlock_I).defaultBlockState();
        StoneBlockProvider.Compile();
        if (!StoneBlockProvider.IsCompiled())
        {
            setConfigAsInvalid();
            return;
        }
        // TODO: Patch this and for 1.20.1
        for (var e : AdditionalEntities)
        {
            e.Compile();
            if (e.IsCompiled() == false) {
                setConfigAsInvalid();
                return;
            }
        }
        if (SpawnerEntityCandidates.isEmpty()) {
            SpawnerEntityCandidates = new ArrayList<>(AdditionalEntities);
        } else {
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

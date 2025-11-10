package com.github.mdcdi1315.mdex.features.config;

import com.github.mdcdi1315.basemodslib.codecs.CodecUtils;
import com.github.mdcdi1315.basemodslib.codecs.StrictListCodec;

import com.github.mdcdi1315.mdex.block.BlockUtils;
import com.github.mdcdi1315.mdex.util.WeightedEntityEntry;
import com.github.mdcdi1315.mdex.block.blockstateproviders.AbstractBlockStateProvider;
import com.github.mdcdi1315.mdex.features.customizablemonsterroom.ChestPlacementConfig;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;

import java.util.List;
import java.util.ArrayList;

public final class CustomizableMonsterRoomConfigurationDetails
    implements IModdedFeatureConfigurationDetails
{
    public static MapCodec<CustomizableMonsterRoomConfigurationDetails> GetCodec()
    {
        Codec<List<WeightedEntityEntry>> e = new StrictListCodec<>(WeightedEntityEntry.GetCodec());
        return CodecUtils.CreateMapCodecDirect(
                ResourceLocation.CODEC.fieldOf("spawner_block").forGetter((CustomizableMonsterRoomConfigurationDetails cfg) -> cfg.SpawnerBlock_I),
                AbstractBlockStateProvider.CODEC.fieldOf("stone_block_provider").forGetter((CustomizableMonsterRoomConfigurationDetails cfg) -> cfg.StoneBlockProvider),
                ChestPlacementConfig.GetCodec().optionalFieldOf("reward_chest_placement", new ChestPlacementConfig(BuiltInLootTables.SIMPLE_DUNGEON.location() , ConstantInt.of(6))).forGetter((CustomizableMonsterRoomConfigurationDetails cfg) -> cfg.ChestConfiguration),
                e.fieldOf("additional_spawns").forGetter((CustomizableMonsterRoomConfigurationDetails cfg) -> cfg.AdditionalEntities),
                e.optionalFieldOf("spawner_block_entity_candidates" , List.of()).forGetter((CustomizableMonsterRoomConfigurationDetails cfg) -> cfg.SpawnerEntityCandidates),
                CustomizableMonsterRoomConfigurationDetails::new
        );
    }

    private ResourceLocation SpawnerBlock_I;
    public BlockState SpawnerBlock;
    public AbstractBlockStateProvider StoneBlockProvider;
    public ChestPlacementConfig ChestConfiguration;
    public List<WeightedEntityEntry> AdditionalEntities;
    public List<WeightedEntityEntry> SpawnerEntityCandidates;

    public CustomizableMonsterRoomConfigurationDetails(
            ResourceLocation sbid ,
            AbstractBlockStateProvider stoneprovider ,
            ChestPlacementConfig chestconfig,
            List<WeightedEntityEntry> entities,
            List<WeightedEntityEntry> spawnerentities)
    {
        SpawnerBlock_I = sbid;
        StoneBlockProvider = stoneprovider;
        ChestConfiguration = chestconfig;
        AdditionalEntities = entities;
        SpawnerEntityCandidates = spawnerentities;
    }

    @Override
    public void Compile()
    {
        try {
            SpawnerBlock = BlockUtils.GetBlockFromID(SpawnerBlock_I).defaultBlockState();
            StoneBlockProvider.Compile();
            if (!StoneBlockProvider.IsCompiled()) {
                throw new FeatureCompilationFailureException();
            }
            for (var e : AdditionalEntities) {
                e.Compile();
                if (e.IsCompiled() == false) {
                    throw new FeatureCompilationFailureException();
                }
            }
            if (SpawnerEntityCandidates.isEmpty()) {
                SpawnerEntityCandidates = new ArrayList<>(AdditionalEntities);
            } else {
                for (var e : SpawnerEntityCandidates) {
                    e.Compile();
                    if (e.IsCompiled() == false) {
                        throw new FeatureCompilationFailureException();
                    }
                }
            }
        } finally {
            SpawnerBlock_I = null;
        }
    }

}

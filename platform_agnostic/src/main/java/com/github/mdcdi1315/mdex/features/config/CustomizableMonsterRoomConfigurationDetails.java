package com.github.mdcdi1315.mdex.features.config;

import com.github.mdcdi1315.DotNetLayer.System.Collections.Generic.List;

import com.github.mdcdi1315.basemodslib.codecs.CodecUtils;
import com.github.mdcdi1315.basemodslib.utils.random.IRandomLookup;
import com.github.mdcdi1315.basemodslib.utils.random.weighted.WeightedList;
import com.github.mdcdi1315.basemodslib.utils.random.weighted.WeightedListCodec;
import com.github.mdcdi1315.basemodslib.utils.random.weighted.WeightedRandomLookup;

import com.github.mdcdi1315.mdex.block.BlockUtils;
import com.github.mdcdi1315.mdex.dco_logic.DCOUtils;
import com.github.mdcdi1315.mdex.util.WeightedEntityEntry;
import com.github.mdcdi1315.mdex.util.WrappedRandomSource;
import com.github.mdcdi1315.mdex.block.blockstateproviders.AbstractBlockStateProvider;
import com.github.mdcdi1315.mdex.features.customizablemonsterroom.ChestPlacementConfig;

import com.mojang.serialization.MapCodec;

import net.minecraft.util.RandomSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;

public final class CustomizableMonsterRoomConfigurationDetails
    implements IModdedFeatureConfigurationDetails
{
    public static MapCodec<CustomizableMonsterRoomConfigurationDetails> GetCodec()
    {
        var e = new WeightedListCodec<>(WeightedEntityEntry.GetCodec());
        return CodecUtils.CreateMapCodecDirect(
                ResourceLocation.CODEC.fieldOf("spawner_block").forGetter((CustomizableMonsterRoomConfigurationDetails cfg) -> cfg.SpawnerBlock_I),
                AbstractBlockStateProvider.CODEC.fieldOf("stone_block_provider").forGetter((CustomizableMonsterRoomConfigurationDetails cfg) -> cfg.StoneBlockProvider),
                ChestPlacementConfig.GetCodec().optionalFieldOf("reward_chest_placement", new ChestPlacementConfig(BuiltInLootTables.SIMPLE_DUNGEON.location() , ConstantInt.of(6))).forGetter((CustomizableMonsterRoomConfigurationDetails cfg) -> cfg.ChestConfiguration),
                e.fieldOf("additional_spawns").forGetter((CustomizableMonsterRoomConfigurationDetails cfg) -> cfg.AdditionalEntities),
                e.optionalFieldOf("spawner_block_entity_candidates", new WeightedList<>(new List<>(0))).forGetter((CustomizableMonsterRoomConfigurationDetails cfg) -> cfg.SpawnerEntityCandidates),
                CustomizableMonsterRoomConfigurationDetails::new
        );
    }

    public BlockState SpawnerBlock;
    private ResourceLocation SpawnerBlock_I;
    public ChestPlacementConfig ChestConfiguration;
    public AbstractBlockStateProvider StoneBlockProvider;
    public WeightedList<WeightedEntityEntry> AdditionalEntities;
    public WeightedList<WeightedEntityEntry> SpawnerEntityCandidates;

    public CustomizableMonsterRoomConfigurationDetails(
            ResourceLocation sbid ,
            AbstractBlockStateProvider stoneprovider ,
            ChestPlacementConfig chestconfig,
            WeightedList<WeightedEntityEntry> entities,
            WeightedList<WeightedEntityEntry> spawnerentities)
    {
        SpawnerBlock_I = sbid;
        StoneBlockProvider = stoneprovider;
        ChestConfiguration = chestconfig;
        AdditionalEntities = entities;
        SpawnerEntityCandidates = spawnerentities;
    }

    public IRandomLookup<WeightedEntityEntry> GetSpawnerLookup(RandomSource random)
    {
        return new WeightedRandomLookup<>(new WrappedRandomSource(random), SpawnerEntityCandidates);
    }

    public IRandomLookup<WeightedEntityEntry> GetAdditionalEntitiesLookup(RandomSource random)
    {
        return new WeightedRandomLookup<>(new WrappedRandomSource(random), AdditionalEntities);
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
            if (!DCOUtils.CompileAllOrFail(AdditionalEntities)) {
                throw new FeatureCompilationFailureException();
            }
            if (SpawnerEntityCandidates.getCount() == 0) {
                SpawnerEntityCandidates = new WeightedList<>(AdditionalEntities);
            } else {
                if (!DCOUtils.CompileAllOrFail(SpawnerEntityCandidates)) {
                    throw new FeatureCompilationFailureException();
                }
            }
        } finally {
            SpawnerBlock_I = null;
        }
    }

}

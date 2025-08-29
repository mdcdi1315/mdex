package com.github.mdcdi1315.mdex.biomespawnadditions;

import com.github.mdcdi1315.mdex.util.Compilable;
import com.github.mdcdi1315.mdex.codecs.CodecUtils;
import com.github.mdcdi1315.mdex.util.weight.Weight;
import com.github.mdcdi1315.mdex.util.weight.IWeightedEntry;
import com.github.mdcdi1315.mdex.util.WeightedCountedEntityEntry;

import com.mojang.serialization.Codec;
import net.minecraft.util.random.Weighted;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;

public final class SpawnEntry
    implements IWeightedEntry, Compilable
{
    public MobCategory Category;
    public WeightedCountedEntityEntry EntryData;
    public MobSpawnSettings.MobSpawnCost SpawnCosts;

    public static Codec<SpawnEntry> GetCodec()
    {
        return CodecUtils.CreateCodecDirect(
                MobCategory.CODEC.fieldOf("category").forGetter((SpawnEntry e) -> e.Category),
                WeightedCountedEntityEntry.GetCountedCodec().fieldOf("entity").forGetter((SpawnEntry e) -> e.EntryData),
                MobSpawnSettings.MobSpawnCost.CODEC.fieldOf("spawn_costs").forGetter((SpawnEntry e) -> e.SpawnCosts),
                SpawnEntry::new
        );
    }

    public SpawnEntry(MobCategory cat, WeightedCountedEntityEntry ent , MobSpawnSettings.MobSpawnCost cost)
    {
        Category = cat;
        EntryData = ent;
        SpawnCosts = cost;
    }

    @Override
    public Weight getWeight() {
        return EntryData.getWeight();
    }

    @Override
    public void Compile() {
        EntryData.Compile();
    }

    @Override
    public boolean IsCompiled() {
        return EntryData.IsCompiled();
    }

    public Weighted<MobSpawnSettings.SpawnerData> getData()
    {
        return new Weighted<>(new MobSpawnSettings.SpawnerData(
                EntryData.Entity,
                EntryData.Count.getMinValue(),
                EntryData.Count.getMaxValue()
        ) , EntryData.getWeight().getValue());
    }
}

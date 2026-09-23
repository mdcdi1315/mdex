package com.github.mdcdi1315.mdex.aggressivespawners;

import com.github.mdcdi1315.basemodslib.codecs.CodecUtils;
import com.github.mdcdi1315.basemodslib.utils.random.weighted.IWeightedEntry;
import com.github.mdcdi1315.basemodslib.utils.random.weighted.WeightedRandomUtils;

import com.github.mdcdi1315.mdex.MDEXModInstance;
import com.github.mdcdi1315.mdex.dco_logic.Compilable;
import com.github.mdcdi1315.mdex.util.CompilableEntityType;

import com.mojang.serialization.Codec;

import net.minecraft.util.RandomSource;
import net.minecraft.core.registries.BuiltInRegistries;

public final class AggressiveSpawnerEntry
        implements Compilable, IWeightedEntry
{
    public int weight;
    public byte min_inclusive;
    public byte max_inclusive;
    public CompilableEntityType Entity;

    public static Codec<AggressiveSpawnerEntry> GetCodec()
    {
        var countcodec = CodecUtils.ByteRange(1 , 127);
        return CodecUtils.CreateCodecDirect(
                WeightedRandomUtils.GetRecommendedRecordFieldConfig(),
                countcodec.fieldOf("min_inclusive").forGetter((AggressiveSpawnerEntry e) -> e.min_inclusive),
                countcodec.fieldOf("max_inclusive").forGetter((AggressiveSpawnerEntry e) -> e.max_inclusive),
                CompilableEntityType.GetCodec().fieldOf("entity").forGetter((AggressiveSpawnerEntry e) -> e.Entity),
                AggressiveSpawnerEntry::new
        );
    }

    public AggressiveSpawnerEntry(int w, byte min_inclusive, byte max_inclusive, CompilableEntityType ent)
    {
        weight = w;
        Entity = ent;
        this.min_inclusive = min_inclusive;
        this.max_inclusive = max_inclusive;
    }

    public int GetRandomNumberOfMobsToSpawn(RandomSource random) {
        return min_inclusive + random.nextInt(1 + (max_inclusive - min_inclusive));
    }

    // It says that Optional.empty() must be used but I want to destroy the Optional itself.
    @SuppressWarnings("all")
    private void DestroyData()
    {
        Entity = null;
    }

    @Override
    public boolean IsCompiled() { return Entity != null; }

    @Override
    public void Compile()
    {
        try {
            Entity.Compile();
            if (!Entity.IsCompiled()) {
                DestroyData();
                return;
            }
        } catch (Exception e) {
            DestroyData();
            return;
        }
        if (max_inclusive < min_inclusive) {
            MDEXModInstance.LOGGER.warn("AggressiveSpawnerEntry: Entry of entity {} is invalid because the minimum count is larger than the maximum count.", BuiltInRegistries.ENTITY_TYPE.getKey(Entity.Entity));
            DestroyData();
        }
    }

    @Override
    public int GetWeight() { return weight; }
}

package com.github.mdcdi1315.mdex.biomespawnadditions;

import com.github.mdcdi1315.DotNetLayer.System.IDisposable;
import com.github.mdcdi1315.DotNetLayer.System.InvalidOperationException;

import com.github.mdcdi1315.basemodslib.codecs.CodecUtils;
import com.github.mdcdi1315.basemodslib.utils.random.weighted.IWeightedEntry;
import com.github.mdcdi1315.basemodslib.utils.random.weighted.WeightedRandomUtils;

import com.github.mdcdi1315.mdex.util.SpawnCost;
import com.github.mdcdi1315.mdex.dco_logic.Compilable;
import com.github.mdcdi1315.mdex.util.CompilableEntityType;

import com.mojang.serialization.Codec;

import java.util.Optional;

public final class BiomeEntitySpawnEntry
    implements Compilable, IWeightedEntry, IDisposable
{
    public int weight;
    public byte min_inclusive;
    public byte max_inclusive;
    // This field may be undefined.
    // If such case, the modifier will not apply spawn costs for this entry.
    public Optional<SpawnCost> costs;
    public CompilableEntityType Entity;

    @Override
    public void Compile()
    {
        try {
            Entity.Compile();
        } catch (Exception e) {
            DestroyData();
            return;
        }
        if (!Entity.IsCompiled())
        {
            DestroyData();
            return;
        }
        if (max_inclusive < min_inclusive)
        {
            DestroyData();
            throw new InvalidOperationException(
                    String.format(
                            "The number of maximum entities to place must be greater than or equal to the number of minimum entities to place!\nActual Numbers:\nMinimum inclusive count: %d\nMaximum inclusive count: %d",
                            min_inclusive,
                            max_inclusive
                    )
            );
        }
    }

    // It says that Optional.empty() must be used but I want to destroy the Optional itself.
    @SuppressWarnings("all")
    private void DestroyData()
    {
        Entity = null;
        costs = null;
    }

    @Override
    public boolean IsCompiled() { return Entity != null; }

    @Override
    public void Dispose() { DestroyData(); }

    private BiomeEntitySpawnEntry(CompilableEntityType ent , byte min , byte max , Optional<SpawnCost> c , int w)
    {
        Entity = ent;
        min_inclusive = min;
        max_inclusive = max;
        costs = c;
        weight = w;
    }

    public static Codec<BiomeEntitySpawnEntry> GetCodec()
    {
        var countcodec = CodecUtils.ByteRange(1 , 127);
        return CodecUtils.CreateCodecDirect(
                CompilableEntityType.GetCodec().fieldOf("entity_id").forGetter((c) -> c.Entity),
                countcodec.fieldOf("min_count").forGetter((c) -> c.min_inclusive),
                countcodec.fieldOf("max_count").forGetter((c) -> c.max_inclusive),
                SpawnCost.GetCodec().optionalFieldOf("spawn_costs").forGetter((c) -> c.costs),
                WeightedRandomUtils.GetRecommendedRecordFieldConfig(),
                BiomeEntitySpawnEntry::new
        );
    }

    @Override
    public int GetWeight() { return weight; }
}

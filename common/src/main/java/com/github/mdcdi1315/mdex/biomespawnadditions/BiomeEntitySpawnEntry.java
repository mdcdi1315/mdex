package com.github.mdcdi1315.mdex.biomespawnadditions;

import com.github.mdcdi1315.DotNetLayer.System.IDisposable;

import com.github.mdcdi1315.mdex.util.Compilable;
import com.github.mdcdi1315.mdex.codecs.CodecUtils;
import com.github.mdcdi1315.mdex.util.weight.Weight;
import com.github.mdcdi1315.mdex.util.CompilableEntityType;


import com.mojang.serialization.Codec;

import java.util.Optional;

public final class BiomeEntitySpawnEntry
    implements Compilable , IDisposable
{
    public CompilableEntityType Entity;
    public byte min_inclusive;
    public byte max_inclusive;
    // This field may be undefined.
    // If such case, the modifier will not apply spawn costs for this entry.
    public Optional<SpawnCost> costs;
    public Weight weight;

    @Override
    public void Compile() {
        try {
            Entity.Compile();
        } catch (Exception e) {
            DestroyData();
        }
        if (!Entity.IsCompiled()) {
            DestroyData();
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
    public boolean IsCompiled() {
        return Entity != null;
    }

    @Override
    public void Dispose() {
        DestroyData();
    }

    public record SpawnCost(double energy_budget, double charge)
    {
        // This codec only once it will be needed to for GetCodec below.
        public static Codec<SpawnCost> GetCodec()
        {
            return CodecUtils.CreateCodecDirect(
                    Codec.DOUBLE.fieldOf("energy_budget").forGetter(SpawnCost::energy_budget),
                    Codec.DOUBLE.fieldOf("charge").forGetter(SpawnCost::charge),
                    SpawnCost::new
            );
        }
    }

    private BiomeEntitySpawnEntry(CompilableEntityType ent , byte min , byte max , Optional<SpawnCost> c , Weight w)
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
                Weight.CODEC.fieldOf("weight").forGetter((c) -> c.weight),
                BiomeEntitySpawnEntry::new
        );
    }
}

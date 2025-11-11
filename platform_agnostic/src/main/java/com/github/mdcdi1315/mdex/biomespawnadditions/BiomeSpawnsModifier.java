package com.github.mdcdi1315.mdex.biomespawnadditions;

import com.github.mdcdi1315.DotNetLayer.System.ArgumentNullException;
import com.github.mdcdi1315.DotNetLayer.System.InvalidOperationException;
import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.MaybeNull;

import com.github.mdcdi1315.mdex.util.MDEXException;
import com.github.mdcdi1315.mdex.mixin.Biome_MobSpawnSettingsAccessor;

import com.google.common.collect.ImmutableMap;

import net.minecraft.util.random.Weighted;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.level.biome.MobSpawnSettings;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

public final class BiomeSpawnsModifier
{
    private Biome biometomodify;
    private Map<MobCategory , ArrayList<Entry>> entries;

    // Special class for keeping the data we are interested in
    private static class Entry
    {
        public EntityType<?> Entity;
        @MaybeNull
        public BiomeEntitySpawnEntry.SpawnCost Costs;
        public byte min , max;
        public int weight; // Decouple weight

        // Assume compiled from the caller
        public Entry(BiomeEntitySpawnEntry e)
        {
            Entity = e.Entity.Entity;
            min = e.min_inclusive;
            max = e.max_inclusive;
            weight = e.weight.getValue();
            Costs = e.costs.orElse(null);
        }
    }

    public BiomeSpawnsModifier(Biome biome)
    {
        ArgumentNullException.ThrowIfNull(biome , "biome");
        biometomodify = biome;
        entries = new HashMap<>(10);
    }

    private static ArrayList<Entry> Creater(MobCategory mc)
    {
        return new ArrayList<>(10);
    }

    public void Add(BiomeEntitySpawnList list)
    {
        if (list.IsEmpty()) {
            return; // Do not record changes if the list itself is invalid or empty
        }
        ArrayList<Entry> l = entries.computeIfAbsent(list.category() , BiomeSpawnsModifier::Creater);
        // Before passing the entries in, we need to retrieve its data and save them internally.
        // We are doing that because we cannot assume the entry's lifetime.
        var el = list.entries();
        l.ensureCapacity(l.size() + el.size());
        for (var e : el) {
            l.add(new Entry(e));
        }
    }

    public void Add(MobCategory mc, List<BiomeEntitySpawnEntry> entries)
    {
        if (entries.isEmpty()) {
            return; // Do not record changes if the list itself is invalid or empty
        }
        ArrayList<Entry> l = this.entries.computeIfAbsent(mc , BiomeSpawnsModifier::Creater);
        // Before passing the entries in, we need to retrieve its data and save them internally.
        // We are doing that because we cannot assume the entry's lifetime.
        l.ensureCapacity(l.size() + entries.size());
        for (var e : entries) {
            l.add(new Entry(e));
        }
    }

    public void ApplyChanges()
    {
        if (biometomodify == null) {
            throw new InvalidOperationException("Biome settings were already applied. No new data can be defined.");
        }
        // There is no reason to modify the biome data if there are no entries to add.
        if (entries.isEmpty()) {
            return;
        }
        Biome_MobSpawnSettingsAccessor accessor = (Biome_MobSpawnSettingsAccessor) biometomodify.getMobSettings();
        var spawners = accessor.GetSpawners();
        if (spawners instanceof ImmutableMap<MobCategory, WeightedList<MobSpawnSettings.SpawnerData>> m)
        {
            spawners = new HashMap<>(m);
        }
        // The below map object will be created only lazily on first found cost.
        Map<EntityType<?>, MobSpawnSettings.MobSpawnCost> costs = null;
        EntityType<?> ent;
        BiomeEntitySpawnEntry.SpawnCost sc;
        for (var entry : entries.entrySet())
        {
            var category = entry.getKey();
            ArrayList<Weighted<MobSpawnSettings.SpawnerData>> data = new ArrayList<>(entry.getValue().size());
            // The value(s) returned by the entry will not be null nor empty.
            // Check the Add method for more information on how this is done.
            for (var e : entry.getValue())
            {
                ent = e.Entity;
                data.add(
                       new Weighted<>(new MobSpawnSettings.SpawnerData(ent , e.min , e.max), e.weight)
                );
                sc = e.Costs;
                if (sc != null)
                {
                    // Create mob spawn costs map lazily, most users do not define these settings.
                    if (costs == null)
                    {
                        costs = accessor.GetMobSpawnCosts();
                        if (costs instanceof ImmutableMap<EntityType<?>, MobSpawnSettings.MobSpawnCost> m)
                        {
                            costs = new HashMap<>(m);
                        }
                    }
                    costs.put(ent , new MobSpawnSettings.MobSpawnCost(sc.energy_budget() , sc.charge()));
                }
            }
            var old = spawners.get(category);
            if (old != null && !old.isEmpty()) {
                data.addAll(old.unwrap());
            }
            spawners.put(category , WeightedList.of(data));
        }
        try {
            accessor.SetSpawners(spawners);
            if (costs != null)
            {
                // A mob spawn cost was added. We need to modify the mob spawn costs too, before returning.
                accessor.SetMobSpawnCosts(costs);
            }
        } catch (IllegalStateException ise) {
            throw new MDEXException(String.format("Cannot apply the changes because the 'final' modifier is possibly not removed. Please check that the AT definitions are correct.\nOriginal error message: %s" , ise.getMessage()));
        } finally {
            // Release resources
            entries = null;
            biometomodify = null;
        }
    }

}

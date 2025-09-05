package com.github.mdcdi1315.mdex.biomespawnadditions;

import com.github.mdcdi1315.DotNetLayer.System.Exception;
import com.github.mdcdi1315.DotNetLayer.System.AggregateException;
import com.github.mdcdi1315.DotNetLayer.System.ArgumentNullException;
import com.github.mdcdi1315.DotNetLayer.System.InvalidOperationException;

import com.github.mdcdi1315.mdex.util.MDEXException;
import com.github.mdcdi1315.mdex.util.EntityTypeNotFoundException;
import com.github.mdcdi1315.mdex.mixin.Biome_MobSpawnSettingsAccessor;

import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.entity.MobCategory;
import com.google.common.collect.ImmutableMap;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.level.biome.MobSpawnSettings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class MobSpawnsModifier
{
    private MobCategory category;
    private MobSpawnSettings spawnsettings;
    private ArrayList<MobSpawnSettings.SpawnerData> entities;

    public MobSpawnsModifier(Biome tobemodified , MobCategory category) throws ArgumentNullException
    {
        ArgumentNullException.ThrowIfNull(tobemodified , "tobemodified");
        ArgumentNullException.ThrowIfNull(category , "category");
        this.category = category;
        spawnsettings = tobemodified.getMobSettings();
        entities = new ArrayList<>();
    }

    public void AddEntity(BiomeSpawnerEntity entity)
            throws ArgumentNullException , EntityTypeNotFoundException , InvalidOperationException
    {
        ArgumentNullException.ThrowIfNull(entity , "entity");
        if (entities == null) {
            throw new InvalidOperationException("Cannot append biome spawner entities after all the entries are registered!!!");
        }
        entities.add(entity.getData());
    }

    public void AddEntities(List<BiomeSpawnerEntity> entities)
            throws ArgumentNullException , AggregateException , InvalidOperationException
    {
        ArgumentNullException.ThrowIfNull(entities , "entities");
        if (this.entities == null) {
            throw new InvalidOperationException("Cannot append biome spawner entities after all the entries are registered!!!");
        }
        this.entities.ensureCapacity(this.entities.size() + entities.size());
        com.github.mdcdi1315.DotNetLayer.System.Collections.Generic.List<Exception> exceptions = new com.github.mdcdi1315.DotNetLayer.System.Collections.Generic.List<>();
        for (var i : entities)
        {
            try {
                this.entities.add(i.getData());
            } catch (EntityTypeNotFoundException e) {
                exceptions.Add(e);
            }
        }
        if (exceptions.getCount() > 0)
        {
            throw new AggregateException(exceptions);
        }
    }

    public void ApplyChanges()
    {
        // Do not modify anything related with the spawners if it seems that there is no reason to.
        if (entities == null || entities.isEmpty()) { return; }
        ApplySpawnerChanges((Biome_MobSpawnSettingsAccessor) spawnsettings);
    }

    public MobCategory GetCategory() {
        return category;
    }

    private void ApplySpawnerChanges(Biome_MobSpawnSettingsAccessor accessor)
    {
        var spawners = accessor.GetSpawners();
        if (spawners instanceof ImmutableMap<MobCategory, WeightedRandomList<MobSpawnSettings.SpawnerData>> m)
        {
            spawners = new HashMap<>(m);
        }
        var old = spawners.get(category);
        if (old == null || old.isEmpty()) {
            spawners.put(category , WeightedRandomList.create(entities));
        } else {
            entities.addAll(old.unwrap());
            spawners.put(category , WeightedRandomList.create(entities));
        }
        try {
            accessor.SetSpawners(spawners);
        } catch (IllegalStateException ise) {
            throw new MDEXException(String.format("Cannot apply the changes because the 'final' modifier is possibly not removed. Please check that the AT definitions are correct.\nOriginal error message: %s" , ise.getMessage()));
        } finally {
            entities.clear();
            entities = null;
        }
    }
}

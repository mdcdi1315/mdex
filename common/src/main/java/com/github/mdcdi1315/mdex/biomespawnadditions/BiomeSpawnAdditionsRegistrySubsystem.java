package com.github.mdcdi1315.mdex.biomespawnadditions;

import com.github.mdcdi1315.DotNetLayer.System.AggregateException;
import com.github.mdcdi1315.DotNetLayer.System.ArgumentNullException;
import com.github.mdcdi1315.DotNetLayer.System.InvalidOperationException;

import net.blay09.mods.balm.api.Balm;

import com.github.mdcdi1315.mdex.MDEXBalmLayer;
import com.github.mdcdi1315.mdex.api.MDEXModAPI;
import com.github.mdcdi1315.mdex.util.MDEXException;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;

import java.util.*;

public final class BiomeSpawnAdditionsRegistrySubsystem
{
    private BiomeSpawnAdditionsRegistrySubsystem() {}

    public static final String REGISTRY_LOCATION = "worldgen/mdex_biome_spawn_additions";
    public static ResourceKey<Registry<BiomeSpawnAdditions>> REGISTRYKEY;

    public static void InitializeRegistry()
    {
        REGISTRYKEY = ResourceKey.createRegistryKey(Objects.requireNonNull(ResourceLocation.tryParse(REGISTRY_LOCATION)));
        MDEXModAPI.getMethodImplementation().CreateDatapackRegistry(REGISTRYKEY , BiomeSpawnAdditions.GetCodec());
    }

    public static void ApplyCurrentBiomeSpawnAdditions(MinecraftServer server)
    {
        ArgumentNullException.ThrowIfNull(server , "server");
        var sac = server.registryAccess();
        Optional<Registry<Biome>> biomes = sac.lookup(Registries.BIOME);
        if (biomes.isEmpty()) {
            throw new InvalidOperationException("Cannot load the biomes registry!!!");
        }
        Optional<Registry<BiomeSpawnAdditions>> bsregistry = sac.lookup(REGISTRYKEY);
        if (bsregistry.isEmpty()) {
            throw new InvalidOperationException("Cannot load the biome spawn additions registry!!!");
        }
        ApplyAdditionsInternal(biomes.get() , bsregistry.get());
    }

    private static void ApplyAdditionsInternal(Registry<Biome> biomes, Registry<BiomeSpawnAdditions> spawnadditions)
    {
        Optional<Biome> spec;
        MDEXBalmLayer.LOGGER.info("BiomeSpawnAdditions: Applying {} biome spawn addition objects" , spawnadditions.size());
        Map<ResourceLocation , List<MobSpawnsModifier>> m = new HashMap<>(spawnadditions.size() / 6);
        for (var i : spawnadditions)
        {
            boolean allloaded = true;
            for (var mod : i.ModIds)
            {
                if (!Balm.isModLoaded(mod)) {
                    allloaded = false;
                    MDEXBalmLayer.LOGGER.warn("BiomeSpawnAdditions: Cannot load entry with ID {} because the mod with ID '{}' is not present in the mod lifecycle." , spawnadditions.getKey(i) , mod);
                    break;
                }
            }
            try {
                if (allloaded) {
                    if ((spec = biomes.getOptional(i.BiomeID)).isPresent()) {
                        MobSpawnsModifier mod = ReturnOrPushCategoryIfNotExisting(m.computeIfAbsent(i.BiomeID , (R) -> new ArrayList<>()) , spec.get() , i.Category);
                        try {
                            mod.AddEntities(i.Entries);
                        } catch (AggregateException e) {
                            MDEXBalmLayer.LOGGER.warn("BiomeSpawnAdditions: Certain entities could not be applied.", e);
                        }
                    } else {
                        MDEXBalmLayer.LOGGER.warn("BiomeSpawnAdditions: Cannot find the biome with ID {}, effectively skipping this entry.", i.BiomeID);
                    }
                }
            } finally {
                i.Destroy();
            }
        }
        int applied = 0 , ab = 0;
        for (var g : m.entrySet())
        {
            var l = g.getValue();
            for (var modifier : l)
            {
                try {
                    modifier.ApplyChanges();
                } catch (MDEXException mde) {
                    MDEXBalmLayer.LOGGER.error("BiomeSpawnAdditions: Cannot apply new entities due to a runtime error. This error will be logged only once.", mde);
                    break;
                }
                applied++;
            }
            l.clear(); // Aggressive cleaning of modifiers
            ab++;
        }
        MDEXBalmLayer.LOGGER.info("BiomeSpawnAdditions: Applied {} biome spawn addition modifications to {} biomes" , applied , ab);
    }

    private static MobSpawnsModifier ReturnOrPushCategoryIfNotExisting(List<MobSpawnsModifier> m, Biome b, MobCategory c)
    {
        var name = c.getName();
        for (var i : m)
        {
            if (name.equals(i.GetCategory().getName())) {
                return i;
            }
        }
        var g = new MobSpawnsModifier(b , c);
        m.add(g);
        return g;
    }
}

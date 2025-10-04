package com.github.mdcdi1315.mdex.biomespawnadditions;

import com.github.mdcdi1315.DotNetLayer.System.ArgumentNullException;
import com.github.mdcdi1315.DotNetLayer.System.InvalidOperationException;

import com.github.mdcdi1315.mdex.MDEXBalmLayer;
import com.github.mdcdi1315.mdex.api.MDEXModAPI;
import com.github.mdcdi1315.mdex.util.EntityTypeNotFoundException;

import net.blay09.mods.balm.api.Balm; // Provides whether a mod is loaded or not.

import com.mojang.datafixers.util.Either;

import net.minecraft.core.Holder;
import net.minecraft.tags.TagKey;
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

    public static ResourceKey<Registry<BiomeSpawnAdditions>> REGISTRYKEY;

    public static void InitializeRegistry()
    {
        REGISTRYKEY = ResourceKey.createRegistryKey(Objects.requireNonNull(ResourceLocation.tryParse("worldgen/mdex_biome_spawn_additions")));
        MDEXModAPI.getMethodImplementation().CreateDatapackRegistry(REGISTRYKEY , BiomeSpawnAdditions.GetCodec());
    }

    public static void ApplyCurrentBiomeSpawnAdditions(MinecraftServer server)
    {
        ArgumentNullException.ThrowIfNull(server , "server");
        var sac = server.registryAccess();
        // We need two registries for this to work:
        // -> The biome registry for modifying the biomes we need to
        // -> The biome spawn additions registry for providing the data to modify.
        Optional<Registry<Biome>> biomes = sac.lookup(Registries.BIOME);
        if (biomes.isEmpty()) {
            throw new InvalidOperationException("Cannot load the biomes registry!!!");
        }
        Optional<Registry<BiomeSpawnAdditions>> bsregistry = sac.lookup(REGISTRYKEY);
        if (bsregistry.isEmpty()) {
            throw new InvalidOperationException("Cannot load the biome spawn additions registry!!!");
        }
        // Now we can apply our biome spawn additions!!!
        ApplyAdditionsInternal(biomes.get() , bsregistry.get());
    }

    private static void ApplyAdditionsInternal(Registry<Biome> biomes, Registry<BiomeSpawnAdditions> spawnadditions)
    {
        MDEXBalmLayer.LOGGER.info("BiomeSpawnAdditions: Applying {} biome spawn addition objects" , spawnadditions.size());
        // Create a map first keeping all the biome objects to apply:
        Map<Biome , BiomeSpawnsModifier> additionsmappings = new HashMap<>(spawnadditions.size() / 3); // A relatively good capacity to begin with, especially if we have more than 16 objects.
        boolean onemodfailed;
        Optional<TagKey<Biome>> biometag;
        int applied = 0 , appliedbiomes = 0;
        List<BiomeEntitySpawnEntry> tempentries;
        // Enumerate through all the registry objects.
        for (var entry : spawnadditions)
        {
            onemodfailed = false; // We start at the beginning with the assumption that 'all required mods are loaded'.
            for (String mod : entry.ModIds)
            {
                if (!Balm.isModLoaded(mod)) {
                    MDEXBalmLayer.LOGGER.warn("BiomeSpawnAdditions: Cannot load entry with ID {} because the mod with ID '{}' is not present in the mod lifecycle." , spawnadditions.getKey(entry) , mod);
                    onemodfailed = true; // At least one mod failed to be found, it violates our original assumption
                    break;
                }
            }
            if (onemodfailed) { entry.Dispose(); continue; } // Continue with the next entry, dispose this one and continue.
            try {
                // We need to check at this point whether ALL the entities defined in the object are themselves valid.
                if (entry.Entries.IsEmpty()) {
                    continue; // No meaning to continue with an empty list - misconfiguration maybe?
                }
                tempentries = new ArrayList<>(entry.Entries.entries().size());
                for (var e : entry.Entries.entries())
                {
                    try {
                        e.Compile();
                        // NOTE: We do not need to check for IsCompiled currently, but we may need that in the future.
                        tempentries.add(e);
                    } catch (EntityTypeNotFoundException etnf) {
                        MDEXBalmLayer.LOGGER.warn("Cannot find entity type {} for the current biome spawns addition object - skipping this inclusion directly." , etnf.GetExpectedLocation());
                    }
                }
                // There is the rare, but possible case that all the entries have failed compilation.
                // Do not apply them in such case.
                if (tempentries.isEmpty()) {
                    continue;
                }
                MobCategory mc = entry.Entries.category();
                // We are into two different cases: Either having a biome tag or a list of biomes.
                // I will check first for a biome tag.
                Either<TagKey<Biome>, List<ResourceLocation>> either = entry.Biomes;
                if ((biometag = either.left()).isPresent()) {
                    for (var b : biomes.getTagOrEmpty(biometag.get())) {
                        if (b.isBound()) // Usually the biomes returned by this will be bound, but just to be sure...
                        {
                            // Now load the entry.
                            BiomeSpawnsModifier md = additionsmappings.computeIfAbsent(b.value(), BiomeSpawnAdditionsRegistrySubsystem::Create);
                            // This modifier remembers our passed biome, just inject the data to it.
                            md.Add(mc , tempentries);
                            appliedbiomes++;
                        }
                    }
                } else {
                    // We have a biome list instead.
                    // This GET operation is safe, do not worry!
                    Optional<Holder.Reference<Biome>> t;
                    for (var i : either.right().get()) {
                        // We need to find it's associated biome object
                        // If not exists, we should emit a warning.
                        if ((t = biomes.get(i)).isPresent() && t.get().isBound()) {
                            // Now load the entry.
                            BiomeSpawnsModifier md = additionsmappings.computeIfAbsent(t.get().value(), BiomeSpawnAdditionsRegistrySubsystem::Create);
                            // This modifier remembers our passed biome, just inject the data to it.
                            md.Add(mc , tempentries);
                            appliedbiomes++;
                        } else {
                            // Emit a warning.
                            MDEXBalmLayer.LOGGER.warn("BiomeSpawnAdditions: Cannot find the biome with ID {}. Biome spawn additions will not happen for this biome object.", i);
                        }
                    }
                }
            } finally {
                // Dispose our entry, we are finished now.
                entry.Dispose();
            }
        }
        // Iterate through all the values and apply our additions!!!
        for (var i : additionsmappings.values())
        {
            try {
                i.ApplyChanges();
            } catch (Exception e) {
                MDEXBalmLayer.LOGGER.error("BiomeSpawnAdditions: Cannot apply new entities due to a runtime error. This error will be logged only once.", e);
            }
        }
        MDEXBalmLayer.LOGGER.info("BiomeSpawnAdditions: Applied {} biome spawn addition modifications to {} biomes" , applied , appliedbiomes);
    }

    private static BiomeSpawnsModifier Create(Biome biome) {
        return new BiomeSpawnsModifier(biome);
    }
}

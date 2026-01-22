package com.github.mdcdi1315.mdex.aggressivespawners;

import com.github.mdcdi1315.basemodslib.BaseModsLib;
import com.github.mdcdi1315.basemodslib.codecs.ListCodec;
import com.github.mdcdi1315.basemodslib.codecs.CodecUtils;
import com.github.mdcdi1315.basemodslib.codecs.EitherCodec;
import com.github.mdcdi1315.basemodslib.codecs.StrictListCodec;

import com.github.mdcdi1315.mdex.MDEXModInstance;
import com.github.mdcdi1315.mdex.dco_logic.Compilable;

import com.mojang.serialization.Codec;

import net.minecraft.core.Holder;
import net.minecraft.tags.TagKey;
import net.minecraft.core.Registry;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.core.registries.Registries;

import java.util.List;
import java.util.ArrayList;

public final class AggressiveSpawner
    implements Compilable
{
    public List<String> RequiredMods;
    private Object BiomeTagOrHolderList;
    public List<AggressiveSpawnerEntryList> Spawning_Entries;

    public AggressiveSpawner(List<String> req_mods, Object biomeTagOrHolderList, List<AggressiveSpawnerEntryList> spawning_entries)
    {
        RequiredMods = req_mods;
        Spawning_Entries = spawning_entries;
        BiomeTagOrHolderList = biomeTagOrHolderList;
    }

    public static Codec<AggressiveSpawner> GetCodec()
    {
        return CodecUtils.CreateCodecDirect(
                new StrictListCodec<>(Codec.STRING).optionalFieldOf("mod_ids", List.of()).forGetter((AggressiveSpawner l) -> l.RequiredMods),
                new EitherCodec<>(TagKey.codec(Registries.BIOME), new ListCodec<>(Biome.CODEC)).fieldOf("biomes").forGetter((AggressiveSpawner l) -> l.BiomeTagOrHolderList),
                new ListCodec<>(AggressiveSpawnerEntryList.GetCodec()).fieldOf("spawn_entries").forGetter((AggressiveSpawner l) -> l.Spawning_Entries),
                AggressiveSpawner::new
        );
    }

    @Override
    public void Compile()
    {
        for (String mod_id : RequiredMods) {
            if (!BaseModsLib.IsModLoaded(mod_id)) {
                MDEXModInstance.LOGGER.warn("AggressiveSpawner: Mod with ID {} cannot be found, skipping this aggressive spawner entry completely", mod_id);
                RequiredMods = null;
                Spawning_Entries = null;
                BiomeTagOrHolderList = null;
                return;
            }
        }
        AggressiveSpawnerEntryList l;
        for (int I = 0; I < Spawning_Entries.size(); I++)
        {
            (l = Spawning_Entries.get(I)).Compile();
            if (!l.IsCompiled()) { Spawning_Entries.remove(I--); }
        }
        if (Spawning_Entries.isEmpty()) {
            RequiredMods = null;
            Spawning_Entries = null;
            BiomeTagOrHolderList = null;
        }
    }

    public void Compile(Registry<Biome> biome_registry)
    {
        Compile();
        if (Spawning_Entries == null) { return; }
        ArrayList<Holder<Biome>> list = new ArrayList<>();
        try {
            TagKey<Biome> biome_tag = (TagKey<Biome>)BiomeTagOrHolderList;
            for (Holder<Biome> hb : biome_registry.getTagOrEmpty(biome_tag)) { list.add(hb); }
        } catch (ClassCastException e) {
            list.addAll((List<Holder<Biome>>)BiomeTagOrHolderList);
        }
        BiomeTagOrHolderList = list;
        list.trimToSize();
    }

    public Iterable<Holder<Biome>> GetApplicableBiomes() { return (List<Holder<Biome>>)BiomeTagOrHolderList; }

    @Override
    public boolean IsCompiled() { return Spawning_Entries != null; }
}

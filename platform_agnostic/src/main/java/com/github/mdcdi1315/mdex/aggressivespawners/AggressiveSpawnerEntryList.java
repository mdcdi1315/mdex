package com.github.mdcdi1315.mdex.aggressivespawners;

import com.github.mdcdi1315.basemodslib.codecs.EnumCodec;
import com.github.mdcdi1315.basemodslib.codecs.CodecUtils;

import com.github.mdcdi1315.mdex.MDEXModInstance;
import com.github.mdcdi1315.mdex.dco_logic.Compilable;
import com.github.mdcdi1315.mdex.util.weight.WeightedEntryList;
import com.github.mdcdi1315.mdex.util.weight.WeightedEntryListCodec;

import com.mojang.serialization.Codec;

import net.minecraft.world.entity.MobCategory;

import java.util.ArrayList;


public final class AggressiveSpawnerEntryList
    implements Compilable
{
    public MobCategory Category;
    public AggressivenessLevel Difficulty;
    public WeightedEntryList<AggressiveSpawnerEntry> Entries;

    public AggressiveSpawnerEntryList(MobCategory cat, AggressivenessLevel lv, WeightedEntryList<AggressiveSpawnerEntry> entries)
    {
        Category = cat;
        Difficulty = lv;
        Entries = entries;
    }

    public static Codec<AggressiveSpawnerEntryList> GetCodec()
    {
        return CodecUtils.CreateCodecDirect(
                MobCategory.CODEC.fieldOf("category").forGetter((AggressiveSpawnerEntryList l) -> l.Category),
                new EnumCodec<>(AggressivenessLevel.class).fieldOf("difficulty").forGetter((AggressiveSpawnerEntryList l) -> l.Difficulty),
                new WeightedEntryListCodec<>(AggressiveSpawnerEntry.GetCodec()).fieldOf("spawners").forGetter((AggressiveSpawnerEntryList l) -> l.Entries),
                AggressiveSpawnerEntryList::new
        );
    }

    @Override
    public void Compile()
    {
        AggressiveSpawnerEntry t;
        ArrayList<AggressiveSpawnerEntry> temp = new ArrayList<>(Entries);
        for (int I = 0; I < temp.size(); I++)
        {
            try {
                (t = temp.get(I)).Compile();
                if (!t.IsCompiled()) {
                    temp.remove(I--);
                }
            } catch (Exception e) {
                temp.remove(I--);
                MDEXModInstance.LOGGER.warn("AggressiveSpawnerEntryList: Cannot compile entry {}: {}", I, e);
            }
        }
        if (temp.isEmpty()) {
            Entries = null;
            MDEXModInstance.LOGGER.warn("AggressiveSpawnerEntryList: All aggressive spawner entries were not compiled, destroying the list");
        } else {
            Entries = new WeightedEntryList<>(temp);
        }
    }

    @Override
    public boolean IsCompiled() { return Entries != null; }
}

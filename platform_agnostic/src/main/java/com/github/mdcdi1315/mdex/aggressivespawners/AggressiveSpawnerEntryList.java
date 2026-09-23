package com.github.mdcdi1315.mdex.aggressivespawners;

import com.github.mdcdi1315.DotNetLayer.System.Collections.Generic.List;

import com.github.mdcdi1315.basemodslib.codecs.EnumCodec;
import com.github.mdcdi1315.basemodslib.codecs.CodecUtils;

import com.github.mdcdi1315.basemodslib.utils.random.weighted.WeightedList;
import com.github.mdcdi1315.basemodslib.utils.random.weighted.WeightedListCodec;

import com.github.mdcdi1315.mdex.MDEXModInstance;
import com.github.mdcdi1315.mdex.dco_logic.Compilable;

import com.mojang.serialization.Codec;

import net.minecraft.world.entity.MobCategory;

public final class AggressiveSpawnerEntryList
    implements Compilable
{
    public MobCategory Category;
    public AggressivenessLevel Difficulty;
    public WeightedList<AggressiveSpawnerEntry> Entries;

    public AggressiveSpawnerEntryList(MobCategory cat, AggressivenessLevel lv, WeightedList<AggressiveSpawnerEntry> entries)
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
                new WeightedListCodec<>(AggressiveSpawnerEntry.GetCodec()).fieldOf("spawners").forGetter((AggressiveSpawnerEntryList l) -> l.Entries),
                AggressiveSpawnerEntryList::new
        );
    }

    @Override
    public void Compile()
    {
        AggressiveSpawnerEntry t;
        List<AggressiveSpawnerEntry> temp = new List<>(Entries);
        for (int I = 0; I < temp.getCount(); I++)
        {
            try {
                (t = temp.getItem(I)).Compile();
                if (!t.IsCompiled()) {
                    temp.RemoveAt(I--);
                }
            } catch (Exception e) {
                temp.RemoveAt(I--);
                MDEXModInstance.LOGGER.warn("AggressiveSpawnerEntryList: Cannot compile entry {}: {}", I, e);
            }
        }
        if (temp.getCount() == 0) {
            Entries = null;
            MDEXModInstance.LOGGER.warn("AggressiveSpawnerEntryList: All aggressive spawner entries were not compiled, destroying the list");
        } else {
            Entries = new WeightedList<>(temp);
        }
    }

    @Override
    public boolean IsCompiled() { return Entries != null; }
}

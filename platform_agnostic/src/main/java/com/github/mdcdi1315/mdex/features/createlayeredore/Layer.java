package com.github.mdcdi1315.mdex.features.createlayeredore;

/*
 * Portions of code are taken from Create mod:
 *
 * MIT License
 *
 * Copyright (c) The Create Team / The Creators of Create
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

import com.github.mdcdi1315.basemodslib.codecs.CodecUtils;

import com.github.mdcdi1315.mdex.MDEXModInstance;
import com.github.mdcdi1315.mdex.util.weight.Weight;
import com.github.mdcdi1315.mdex.dco_logic.Compilable;
import com.github.mdcdi1315.mdex.util.weight.IWeightedEntry;
import com.github.mdcdi1315.mdex.util.SingleTargetBlockState;

import com.mojang.serialization.Codec;

import net.minecraft.util.RandomSource;

import java.util.ArrayList;
import java.util.List;

public final class Layer
    implements Compilable , IWeightedEntry
{

    public List<List<SingleTargetBlockState>> targets;
    public final short min_size , max_size;
    public Weight weight;

    public static Codec<Layer> GetCodec()
    {
        Codec<Short> SIZE_CODEC = CodecUtils.ShortRange(0 , 32767);
        return CodecUtils.CreateCodecDirect(
                SingleTargetBlockState.GetListCodec().listOf().fieldOf("targets").forGetter((l) -> l.targets),
                SIZE_CODEC.fieldOf("min_size").forGetter((l) -> l.min_size),
                SIZE_CODEC.fieldOf("max_size").forGetter((l) -> l.max_size),
                Weight.CODEC.fieldOf("weight").forGetter((l) -> l.weight),
                Layer::new
        );
    }

    public Layer(List<List<SingleTargetBlockState>> targets, short minSize, short maxSize, Weight weight) {
        this.targets = new ArrayList<>(targets.size());
        for (List<SingleTargetBlockState> ts : targets) {
            this.targets.add(new ArrayList<>(ts));
        }
        this.min_size = minSize;
        this.max_size = maxSize;
        this.weight = weight;
    }

    @Override // from IWeightedEntry interface
    public Weight getWeight() {
        return weight;
    }

    public List<SingleTargetBlockState> RollBlock(RandomSource random)
    {
        int size = targets.size();
        return size == 1 ? targets.get(0) : targets.get(random.nextInt(size));
    }

    @Override
    public void Compile()
    {
        try {
            for (int I = 0; I < targets.size(); I++)
            {
                SingleTargetBlockState entry;
                List<SingleTargetBlockState> inner = targets.get(I);
                for (int J = 0; J < inner.size(); J++)
                {
                    entry = inner.get(J);
                    try {
                        entry.Compile();
                        if (!entry.IsCompiled()) {
                            MDEXModInstance.LOGGER.warn("CREATELAYEREDORELAYER: Compilation failed for single target state object of hash code {}! Excluding from feature usage.", entry.hashCode());
                            inner.remove(J--);
                        }
                    } catch (Exception e) {
                        MDEXModInstance.LOGGER.warn("CREATELAYEREDORELAYER: Compilation failed for single target state object of hash code {}! \nException data: {} \nExcluding from feature usage.", entry.hashCode() , e);
                        inner.remove(J--);
                    }
                }
                if (inner.isEmpty()) {
                    targets.remove(I--);
                }
            }
        } catch (Exception e) {
            // Compilation failed, clean everything and throw back
            Cleanup();
            throw e;
        }
        if (targets.isEmpty()) { // Cleanup and fail if not at least one entry in a list is compiled.
            Cleanup();
        }
    }

    private void Cleanup()
    {
        weight = null;
        targets = null;
    }

    @Override
    public boolean IsCompiled() {
        return targets != null;
    }
}

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

import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.MaybeNull;

import com.github.mdcdi1315.mdex.util.Compilable;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;

import net.minecraft.util.RandomSource;

import java.util.List;

public final class LayerPattern
    implements Compilable
{
    public static Codec<LayerPattern> GetCodec() {
        return Layer.GetCodec().listOf().flatXmap(LayerPattern::CreateFrom , LayerPattern::Decompose);
    }

    private static DataResult<LayerPattern> CreateFrom(List<Layer> layers) {
        return DataResult.success(new LayerPattern(layers));
    }

    private static DataResult<List<Layer>> Decompose(LayerPattern p) {
        return DataResult.success(p.layers);
    }

    private List<Layer> layers;
    private int computedweight;

    public LayerPattern(List<Layer> ls) {
        layers = ls;
        computedweight = 0;
    }

    @Override
    public void Compile()
    {
        computedweight = 0;
        for (var l : layers)
        {
            l.Compile();
            if (!l.IsCompiled()) {
                layers = null;
                break;
            }
            computedweight += l.weight.getValue();
        }
    }

    @Override
    public boolean IsCompiled() {
        return layers != null;
    }

    public List<Layer> getLayers() {
        return layers;
    }

    @MaybeNull
    public Layer RollNext(RandomSource random)
    {
        int rolled = random.nextInt(computedweight);

        // Find the random entry. If we have such an entry, return it.
        for (var l : layers) {
            if ((rolled -= l.weight.getValue()) < 0) {
                return l;
            }
        }

        // Give up if we could not find such a layer.
        return null;
    }

    @MaybeNull
    public Layer RollNext(@MaybeNull Layer previous, RandomSource random)
    {
        if (previous == null) {
            // Call in the optimized workhorse method instead.
            return RollNext(random);
        }

        // The original code was computing the weight again in any case.
        // Instead, the total weight is now cached and since we do not
        // want the probability of the previous layer (the 'previous' parameter),
        // we just subtract its weight value from the total weight.
        // Finally, when weight determination is happening, the previous layer
        // is removed since it could cause unwanted inclusion problems.

        int rolled = random.nextInt(computedweight - previous.weight.getValue());

        for (var l : layers) {
            // Means 'exclude the previous layer'.
            if (previous != l && (rolled -= l.weight.getValue()) < 0) {
                return l;
            }
        }

        // Give up if we could not find such a layer.
        return null;
    }
}

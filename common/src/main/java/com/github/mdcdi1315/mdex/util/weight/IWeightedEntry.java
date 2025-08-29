package com.github.mdcdi1315.mdex.util.weight;

import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.NotNull;

public interface IWeightedEntry
{
    @NotNull
    Weight getWeight();
}

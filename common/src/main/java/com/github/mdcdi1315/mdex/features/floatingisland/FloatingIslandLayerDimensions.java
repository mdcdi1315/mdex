package com.github.mdcdi1315.mdex.features.floatingisland;

public record FloatingIslandLayerDimensions(int X , int Z)
{
    public int MINUS_X()
    {
        return -X;
    }

    public int MINUS_Z()
    {
        return -Z;
    }
}

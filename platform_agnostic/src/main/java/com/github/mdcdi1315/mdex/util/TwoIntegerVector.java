package com.github.mdcdi1315.mdex.util;


public final class TwoIntegerVector
{
    private final int x , y;

    public TwoIntegerVector()
    {
        x = 0;
        y = 0;
    }

    public TwoIntegerVector(int x , int y)
    {
        this.x = x;
        this.y = y;
    }

    public TwoIntegerVector Offset(TwoIntegerVector other)
    {
        return new TwoIntegerVector(this.x + other.x , this.y + other.y);
    }

    public TwoIntegerVector Offset(int x , int y)
    {
        return new TwoIntegerVector(this.x + x , this.y + y);
    }

    public TwoIntegerVector Above()
    {
        return new TwoIntegerVector(x , y + 1);
    }

    public TwoIntegerVector Below()
    {
        return new TwoIntegerVector(x , y - 1);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(x) + Integer.hashCode(y);
    }
}

package com.github.mdcdi1315.mdex.features.createlayeredore;

/**
 * For mod's internal use only. Do not use it.
 */
public record ResolvedLayerEntry(Layer layer, float radialThresholdMultiplier, float rampStartValue)
        implements Comparable<ResolvedLayerEntry>
{
    @Override
    public int compareTo(ResolvedLayerEntry b) {
        return Float.compare(rampStartValue, b.rampStartValue);
    }
}
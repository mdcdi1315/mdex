package com.github.mdcdi1315.mdex.aggressivespawners;

/**
 * Aggressiveness constants for aggressive spawners. <br />
 * These constants control when spawns will be done. <br />
 * Additionally, every {@link #LOW} ticks the mob spawn cap is zeroed out.
 */
public enum AggressivenessLevel
{
    /**
     * New spawn check is done in 867 ticks
     */
    LOW(867),
    /**
     * New spawn check is done in 346 ticks
     */
    MEDIUM(346),
    /**
     * New spawn check is done in 180 ticks
     */
    HIGH(180),
    /**
     * New spawn check is done in 110 ticks
     */
    VERY_HIGH(110),
    /**
     * New spawn check is done in 6440 ticks <br />
     * Special constant for mobs that you want to spawn them as bosses.
     */
    BOSS(6440);

    /**
     * The number of ticks to perform spawns on the given.
     */
    public final int Ticks;

    AggressivenessLevel(int ticks) { Ticks = ticks; }
}

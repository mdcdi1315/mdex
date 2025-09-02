package com.github.mdcdi1315.mdex.block;

import com.github.mdcdi1315.DotNetLayer.System.ArgumentNullException;
import com.github.mdcdi1315.mdex.MDEXBalmLayer;
import com.github.mdcdi1315.mdex.util.Compilable;
import net.blay09.mods.balm.api.Balm;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;

import java.util.List;

public abstract class AbstractModdedBlockPredicate
        implements BlockPredicate , Compilable
{
    private static final byte MODLIST_IS_DETERMINED = 1 << 0;
    private static final byte MODLIST_RESULT_VALID = 1 << 1;
    private static final byte STATE_COMPILED = 1 << 2;
    private byte flags;
    public List<String> ModIds;

    public AbstractModdedBlockPredicate(List<String> modids)
            throws ArgumentNullException
    {
        ArgumentNullException.ThrowIfNull(modids , "modids");
        ModIds = modids;
        flags = 0;
    }

    /**
     * Compiles data that can be compiled. <br />
     * Override this method to provide your own compilable data
     */
    public void Compile() {}

    public abstract boolean Test(WorldGenLevel level , BlockPos pos);

    public final boolean test(WorldGenLevel level , BlockPos p)
    {
        if (!getIsValid()) {
            return false;
        }
        return Test(level , p);
    }

    /**
     * Gets a value whether the current modded block predicate represents a valid instance. <br />
     * Will return true only when both the mod id list passed is valid and the specified data were successfully compiled.
     * @return A value whether this instance is valid.
     */
    public boolean getIsValid()
    {
        if ((flags & MODLIST_IS_DETERMINED) != 0) {
            return (flags & (MODLIST_RESULT_VALID | STATE_COMPILED)) != 0;
        }
        for (var mod : ModIds)
        {
            if (!Balm.isModLoaded(mod)) {
                flags |= MODLIST_IS_DETERMINED;
                return false;
            }
        }
        flags |= MODLIST_RESULT_VALID | MODLIST_IS_DETERMINED;
        try {
            Compile();
            flags |= STATE_COMPILED;
        } catch (Exception e) {
            MDEXBalmLayer.LOGGER.warn("Cannot compile the modded block predicate" , e);
        }
        return true;
    }

    /**
     * When called in the implementation of the {@link AbstractModdedBlockPredicate#Compile} implementation,
     * it invalidates the object so that to avoid accidental calls to the Test method, due to compilation errors.
     */
    public void Invalidate() {
        flags &= ~ MODLIST_RESULT_VALID;
    }

    public boolean IsCompiled() {
        return (flags & STATE_COMPILED) != 0;
    }
}

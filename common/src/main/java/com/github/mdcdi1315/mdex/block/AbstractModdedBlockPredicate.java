package com.github.mdcdi1315.mdex.block;

import com.github.mdcdi1315.DotNetLayer.System.ArgumentNullException;
import net.blay09.mods.balm.api.Balm;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;

import java.util.List;

public abstract class AbstractModdedBlockPredicate
    implements BlockPredicate
{
    private static final byte MODLIST_IS_DETERMINED = 1 << 0;
    private static final byte MODLIST_RESULT_VALID = 1 << 1;
    private byte flags;
    public List<String> ModIds;

    public AbstractModdedBlockPredicate(List<String> modids)
    {
        ArgumentNullException.ThrowIfNull(modids , "modids");
        ModIds = modids;
        flags = 0;
    }

    public boolean getModIdListIsValid()
    {
        if ((flags & MODLIST_IS_DETERMINED) != 0) {
            return (flags & MODLIST_RESULT_VALID) != 0;
        }
        for (var mod : ModIds)
        {
            if (!Balm.isModLoaded(mod)) {
                flags |= MODLIST_IS_DETERMINED;
                return false;
            }
        }
        flags |= MODLIST_RESULT_VALID | MODLIST_IS_DETERMINED;
        return true;
    }
}
